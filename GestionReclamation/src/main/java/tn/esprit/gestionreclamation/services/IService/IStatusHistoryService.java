package tn.esprit.gestionreclamation.services.IService;

import tn.esprit.gestionreclamation.models.StatusHistory;

import java.util.List;
import java.util.Optional;

public interface IStatusHistoryService {
    List<StatusHistory> getAllStatusHistory();
    Optional<StatusHistory> getStatusHistoryById(Long id);
    Optional<StatusHistory> getStatusHistoryByReclamationId(Long id);
    Optional<StatusHistory> getStatusHistoryByLastModifierId(Long id);
    StatusHistory addStatusHistory(StatusHistory statusHistory);
    StatusHistory updateStatusHistory(StatusHistory statusHistory);
    void deleteStatusHistory(Long id);
}
