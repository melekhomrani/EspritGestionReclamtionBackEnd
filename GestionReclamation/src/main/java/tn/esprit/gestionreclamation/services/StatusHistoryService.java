package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.models.StatusHistory;
import tn.esprit.gestionreclamation.repositories.StatusHistoryRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StatusHistoryService {

    private final StatusHistoryRepository statusHistoryRepository;

    public List<StatusHistory> getAllStatusHistory() {
        return statusHistoryRepository.findAll();
    }

    public Optional<StatusHistory> getStatusHistoryById(Long id) {
        return statusHistoryRepository.findById(id);
    }

    public Optional<StatusHistory> getStatusHistoryByReclamationId(Long id) {
        return statusHistoryRepository.findByReclamationId(id);
    }

    public Optional<StatusHistory> getStatusHistoryByLastModifierId(Long id) {
        return statusHistoryRepository.findByLastModifierId(id);
    }

    public StatusHistory addStatusHistory(StatusHistory statusHistory) {
        return null;
    }

    public StatusHistory updateStatusHistory(StatusHistory statusHistory) {
        return null;
    }

    public void deleteStatusHistory(Long id) {

    }
}
