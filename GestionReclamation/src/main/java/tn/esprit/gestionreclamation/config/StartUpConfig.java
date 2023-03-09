package tn.esprit.gestionreclamation.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.esprit.gestionreclamation.models.*;
import tn.esprit.gestionreclamation.services.*;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StartUpConfig {
    @Bean
    CommandLineRunner initDatabase(UserService userService,
                                   PasswordEncoder passwordEncoder,
                                   RoleService roleService,
                                   ReclamationService reclamationService,
                                   ReclamationTypeService reclamationTypeService,
                                   AccessFlowService accessFlowService) {
        return args -> {
            Role admin = Role.builder().roleName("ADMIN").build();
            Role user = Role.builder().roleName("USER").build();
            Role moderator = Role.builder().roleName("MODERATOR").build();
            Role student = Role.builder().roleName("STUDENT").build();
            Role teacher = Role.builder().roleName("TEACHER").build();
            roleService.saveRoles(List.of(admin, user, moderator, student, teacher));

            Users adminUser = Users.builder()
                    .email("admin@esprit.tn")
                    .password(passwordEncoder.encode("admin"))
                    .firstName("Admin")
                    .lastName("Admin")
                    .role(admin)
                    .userRole(UserRole.ADMIN)
                    .build();
            Users userUser = Users.builder()
                    .email("user@esprit.tn")
                    .password(passwordEncoder.encode("user"))
                    .firstName("User")
                    .lastName("User")
                    .role(user)
                    .userRole(UserRole.USER)
                    .build();
            Users moderatorUser = Users.builder()
                    .email("moderator@essprit.tn")
                    .password(passwordEncoder.encode("moderator"))
                    .firstName("Moderator")
                    .lastName("Moderator")
                    .role(moderator)
                    .userRole(UserRole.USER)
                    .build();
            Users studentUser = Users.builder()
                    .email("student@esprit.tn")
                    .password(passwordEncoder.encode("student"))
                    .firstName("Student")
                    .lastName("Student")
                    .role(student)
                    .userRole(UserRole.USER)
                    .build();
            Users teacherUser = Users.builder()
                    .email("teacher@esprit.tn")
                    .password(passwordEncoder.encode("teacher"))
                    .firstName("Teacher")
                    .lastName("Teacher")
                    .role(teacher)
                    .userRole(UserRole.USER)
                    .build();
            userService.saveUsers(List.of(adminUser, userUser, moderatorUser, studentUser, teacherUser));

            log.info("Users created");

            ReclamationType bug = ReclamationType.builder().typeName("Bug").build();
            ReclamationType feature = ReclamationType.builder().typeName("Feature").build();
            ReclamationType task = ReclamationType.builder().typeName("Task").build();
            reclamationTypeService.saveReclamationTypes(List.of(bug, feature, task));

            log.info("Reclamation types created");

            AccessFlow accessFlowBug = AccessFlow.builder()
                    .reclamationType(bug)
                    .notify(List.of(admin, moderator))
                    .create(List.of(admin, user, moderator, student, teacher))
                    .consult(List.of(admin, moderator))
                    .approve(List.of(admin, moderator))
                    .validate(List.of(admin))
                    .build();
            AccessFlow accessFlowFeature = AccessFlow.builder()
                    .reclamationType(feature)
                    .notify(List.of(admin, moderator))
                    .create(List.of(admin, user, moderator, student, teacher))
                    .consult(List.of(admin, moderator))
                    .approve(List.of(admin, moderator))
                    .validate(List.of(admin))
                    .build();
            AccessFlow accessFlowTask = AccessFlow.builder()
                    .reclamationType(task)
                    .notify(List.of(admin, moderator))
                    .create(List.of(admin, user, moderator, student, teacher))
                    .consult(List.of(admin, moderator))
                    .approve(List.of(admin, moderator))
                    .validate(List.of(admin))
                    .build();
            accessFlowService.saveAccessFlows(List.of(accessFlowBug, accessFlowFeature, accessFlowTask));

            log.info("Access flows created");

            Reclamation reclamation = Reclamation.builder()
                    .object("Bug 1")
                    .description("Bug 1 description")
                    .type(bug)
                    .author(studentUser)
                    .progress(Progress.WAITING)
                    .build();
            reclamationService.saveReclamation(reclamation);

            log.info("Reclamations created");

        };
    }
}
