package tn.esprit.gestionreclamation.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.dto.CalendarResponse;
import tn.esprit.gestionreclamation.dto.ReclamationRequest;
import tn.esprit.gestionreclamation.dto.UpdateProgressRequest;
import tn.esprit.gestionreclamation.exceptions.BadRequestException;
import tn.esprit.gestionreclamation.models.*;
import tn.esprit.gestionreclamation.repositories.ReclamationRepository;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class ReclamationService {

    private final ReclamationRepository reclamationRepository;
    private final AccessFlowService accessFlowService;
    private final ReclamationTypeService reclamationTypeService;
    private final UserService userService;
    @Value("${FRONT_URL}")
    private String frontUrl;
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    public List<Reclamation> getAllAllowedReclamations(Authentication authentication) throws AccessDeniedException {
        var allowedTypes = accessFlowService.findAllowedToConsultTypes(authentication);
        return reclamationRepository.findAllByTypeIn(allowedTypes);
    }

    public Optional<Reclamation> getReclamationById(Long id) {
        return reclamationRepository.findById(id);
    }

    public Reclamation saveReclamation(ReclamationRequest reclamation, Authentication authentication) throws AccessDeniedException {
        return reclamationRepository.saveAndFlush(Reclamation.builder()
                .author(authentication.getProfile())
                .subject(reclamation.getObject())
                .progress(Progress.WAITING)
                .type(reclamationTypeService.getReclamationTypeById(reclamation.getTypeId()))
                .description(reclamation.getDescription())
                .build()
        );
    }

    public List<Reclamation> saveReclamations(List<Reclamation> reclamations) {
        return reclamationRepository.saveAllAndFlush(reclamations);
    }

    public Reclamation updateReclamation(Long id, Reclamation reclamation) {
        Reclamation reclamationToUpdate = reclamationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found"));
        reclamationToUpdate.setType(reclamation.getType());
        reclamationToUpdate.setSubject(reclamation.getSubject());
        reclamationToUpdate.setDescription(reclamation.getDescription());
        reclamationToUpdate.setAuthor(reclamation.getAuthor());
        reclamationToUpdate.setProgress(reclamation.getProgress());
        return reclamationRepository.saveAndFlush(reclamationToUpdate);
    }

    public Reclamation updateProgress(Long id, Authentication authentication, UpdateProgressRequest updateProgressRequest){
        var reclamation = reclamationRepository.findById(id);
        if(reclamation.isEmpty()) throw new EntityNotFoundException("Reclamation Not Found");
        var accessFlow = accessFlowService.getAccessFlowByReclamation(reclamation.get()).get();
        var updatedReclamation = reclamation.get();
        switch (updateProgressRequest.getProgress().toLowerCase()){
            case "waiting":
                if(userService.isAdmin(authentication) || userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getApprove()) || userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getValidate())){
                    updatedReclamation.setProgress(Progress.WAITING);
                }
                break;
            case "processing":
                if(userService.isAdmin(authentication) || userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getApprove())){
                    updatedReclamation.setProgress(Progress.PROCESSING);
                }
                break;
            case "done":
                if(userService.isAdmin(authentication) || userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getValidate())){
                    updatedReclamation.setProgress(Progress.DONE);
                }
                break;
            case "cancelled":
                if(userService.isAdmin(authentication) || userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getApprove()) || userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getValidate())){
                    updatedReclamation.setProgress(Progress.CANCELLED);
                }
                break;
            case "validated":
                if(userService.isAdmin(authentication) || userService.isAuthorized(authentication.getProfile().getRole(), accessFlow.getValidate())){
                    updatedReclamation.setProgress(Progress.VALIDATED);
                }
                break;
            default:
                throw new IllegalArgumentException("Can't set progress to: '" + updateProgressRequest.getProgress() + "'");
        }
        reclamationRepository.saveAndFlush(updatedReclamation);
        return updatedReclamation;
    }

    public void deleteReclamation(Long id) {
        Reclamation reclamationToDelete = reclamationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found"));
        reclamationRepository.deleteById(reclamationToDelete.getId());
    }

    public void switchToArchive(Long id) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reclamation not found"));
        reclamation.setArchived(!reclamation.getArchived());
        reclamationRepository.saveAndFlush(reclamation);
    }



    @Async
    public CompletableFuture<ArrayList<CalendarResponse>> getCalendarData(LocalDate start, LocalDate end){
        ArrayList<CalendarResponse> dailyCount = new ArrayList<>();
        List<Reclamation> reclams = reclamationRepository.findReclamationsByDateCreationIsBetween(start.atStartOfDay(), end.atStartOfDay().plusDays(1).minusSeconds(1));
        reclams.sort(Comparator.comparing(Reclamation::getDateCreation));
        start.datesUntil(end).forEach(
                d ->{
                    dailyCount.add(CalendarResponse.builder()
                            .value(reclams.stream().filter(reclamation -> {
                                return reclamation.getDateCreation().toLocalDate().atStartOfDay().equals(d.atStartOfDay());
                            }).count()
                    ).day(d).build());
                }
        );
        return CompletableFuture.completedFuture(dailyCount);
    }


    public String getFrontUrl(Long id){
        return frontUrl + "/reclamation/" + id.toString();
    }

    @Async
    public CompletableFuture<Integer> getCountByUser(Authentication authentication){
        return CompletableFuture.completedFuture(reclamationRepository.countAllByAuthor(authentication.getProfile()));
    }

    @Async
    public CompletableFuture<Integer> getStateCountByUser(Authentication authentication, String state){
        return CompletableFuture.completedFuture(switch (state.toLowerCase()) {
            case "waiting" ->
                    reclamationRepository.countAllByAuthorAndProgress(authentication.getProfile(), Progress.WAITING);
            case "processing" ->
                    reclamationRepository.countAllByAuthorAndProgress(authentication.getProfile(), Progress.PROCESSING);
            case "validated" ->
                    reclamationRepository.countAllByAuthorAndProgress(authentication.getProfile(), Progress.VALIDATED);
            case "done" ->
                    reclamationRepository.countAllByAuthorAndProgress(authentication.getProfile(), Progress.DONE);
            case "cancelled" ->
                    reclamationRepository.countAllByAuthorAndProgress(authentication.getProfile(), Progress.CANCELLED);
            default -> throw new BadRequestException("Invalid State");
        });

    }

    public List<Reclamation> getMine(Users author) {
        return reclamationRepository.findByAuthor(author);
    }

    public List<Reclamation> getNeeded(Authentication authentication){
        var accessFlows = accessFlowService.getByApproveAndValidateIn(authentication.getProfile().getRole());
        List<ReclamationType> typesTable = List.of();
        accessFlows.forEach(accessFlow -> {
            typesTable.add(accessFlow.getReclamationType());
        });
        return reclamationRepository.findReclamationsByTypeIsInAndProgressIsNotIn(typesTable, List.of(Progress.DONE, Progress.CANCELLED));
    }

    public Boolean canSetState(Authentication authentication, Long id){
        if(userService.isAdmin(authentication)) return true;
        Reclamation reclamation = getReclamationById(id).orElseThrow();
        AccessFlow accessFlow = accessFlowService.getAccessFlowByReclamation(reclamation).orElseThrow();
        return accessFlow.getApprove().contains(authentication.getProfile().getRole()) || accessFlow.getValidate().contains(authentication.getProfile().getRole());
    }
}
