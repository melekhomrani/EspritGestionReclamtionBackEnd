package tn.esprit.gestionreclamation.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.esprit.gestionreclamation.models.AccessFlow;
import tn.esprit.gestionreclamation.models.ReclamationType;
import tn.esprit.gestionreclamation.models.Role;
import tn.esprit.gestionreclamation.repositories.ReclamationTypeRepository;
import tn.esprit.gestionreclamation.repositories.RoleRepository;
import tn.esprit.gestionreclamation.services.AccessFlowService;
import tn.esprit.gestionreclamation.services.ReclamationTypeService;
import tn.esprit.gestionreclamation.services.RoleService;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StartUpConfig {
    @Bean
    CommandLineRunner commandLineRunner(ReclamationTypeRepository reclamationTypeRepository, RoleRepository roleRepository, RoleService roleService, AccessFlowService accessFlowService, ReclamationTypeService reclamationTypeService){
        return args -> {
            var checkRole = roleRepository.findByName("Admin");
            if(checkRole.isEmpty()){
                var newRole = roleService.saveRole(Role.builder().name("Admin").build());
                log.info("first saved role id: {}", newRole);
                var checkType = reclamationTypeRepository.findByTypeName("Note Etudiant");
                if(checkType.isEmpty()){
                    var newType = reclamationTypeService.saveReclamationType(ReclamationType.builder().typeName("Note Etudiant").build());
                    var newAccessFlow = accessFlowService.saveAccessFlow(
                            AccessFlow.builder()
                                    .approve(List.of(newRole))
                                    .consult(List.of(newRole))
                                    .create(List.of(newRole))
                                    .validate(List.of(newRole))
                                    .notify(List.of(newRole))
                                    .reclamationType(newType)
                                    .build()
                    );
                }
            }
        };
    }
}
