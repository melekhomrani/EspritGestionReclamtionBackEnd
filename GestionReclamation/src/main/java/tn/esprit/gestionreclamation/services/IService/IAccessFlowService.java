package tn.esprit.gestionreclamation.services.IService;

import tn.esprit.gestionreclamation.models.AccessFlow;

import java.util.List;
import java.util.Optional;

public interface IAccessFlowService {
    List<AccessFlow> getAllAccessFlow();
    Optional<AccessFlow> getAccessFlowById(Long id);
    AccessFlow saveAccessFlow(AccessFlow accessFlow);
    List<AccessFlow> saveAccessFlows(List<AccessFlow> accessFlows);
    AccessFlow updateAccessFlow(AccessFlow accessFlow);
    void deleteAccessFlow(Long id);
}
