package tn.esprit.gestionreclamation.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.repositories.AccessFlowRepository;
import tn.esprit.gestionreclamation.services.IService.IAccessFlowService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccessFlowService implements IAccessFlowService {

    private final AccessFlowRepository accessFlowRepository;

    @Override
    public List<AccessFlow> getAllAccessFlow() {
        return accessFlowRepository.findAll();
    }

    @Override
    public Optional<AccessFlow> getAccessFlowById(Long id) {
        return accessFlowRepository.findById(id);
    }

    @Override
    public AccessFlow saveAccessFlow(AccessFlow accessFlow) {
        return accessFlowRepository.save(accessFlow);
    }

    @Override
    public List<AccessFlow> saveAccessFlows(List<AccessFlow> accessFlows) {
        return accessFlowRepository.saveAllAndFlush(accessFlows);
    }

    @Override
    public AccessFlow updateAccessFlow(AccessFlow accessFlow) {
        return accessFlowRepository.save(accessFlow);
    }

    @Override
    public void deleteAccessFlow(Long id) {
        accessFlowRepository.deleteById(id);
    }
}
