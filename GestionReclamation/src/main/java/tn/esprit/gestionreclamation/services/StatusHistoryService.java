package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.models.StatusHistory;
import tn.esprit.gestionreclamation.repositories.StatusHistoryRepository;
import tn.esprit.gestionreclamation.services.IService.IStatusHistoryService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatusHistoryService implements IStatusHistoryService {

    private final StatusHistoryRepository statusHistoryRepository;
    @Override
    public List<StatusHistory> getAllStatusHistory() {
        return statusHistoryRepository.findAll();
    }

    @Override
    public Optional<StatusHistory> getStatusHistoryById(Long id) {
        return statusHistoryRepository.findById(id);
    }

    @Override
    public Optional<StatusHistory> getStatusHistoryByReclamationId(Long id) {
        return statusHistoryRepository.findByReclamationId(id);
    }

    @Override
    public Optional<StatusHistory> getStatusHistoryByLastModifierId(Long id) {
        return statusHistoryRepository.findByLastModifierId(id);
    }

    @Override
    public StatusHistory addStatusHistory(StatusHistory statusHistory) {
        return null;
    }

    @Override
    public StatusHistory updateStatusHistory(StatusHistory statusHistory) {
        return null;
    }

    @Override
    public void deleteStatusHistory(Long id) {

    }
}
