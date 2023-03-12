package tn.esprit.gestionreclamation.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tn.esprit.gestionreclamation.dto.UserResponse;
import tn.esprit.gestionreclamation.models.*;
import tn.esprit.gestionreclamation.repositories.*;
import tn.esprit.gestionreclamation.services.*;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StartUpConfig {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   RoleRepository roleRepository,
                                   ReclamationRepository reclamationRepository,
                                   ReclamationTypeRepository reclamationTypeRepository,
                                   AccessFlowRepository accessFlowRepository) {
        return args -> {
            Role admin = Role.builder().name("ADMIN").build();
            Role user = Role.builder().name("USER").build();
            Role moderator = Role.builder().name("MODERATOR").build();
            Role student = Role.builder().name("STUDENT").build();
            Role teacher = Role.builder().name("TEACHER").build();
            roleRepository.saveAllAndFlush(List.of(admin, user, moderator, student, teacher));
            Users adminUser = Users.builder()
                    .email("admin@esprit.tn")
                    .password(passwordEncoder.encode("admin"))
                    .firstName("Admin")
                    .lastName("Admin")
                    .role(admin)
                    .build();
            Users userUser = Users.builder()
                    .email("user@esprit.tn")
                    .password(passwordEncoder.encode("user"))
                    .firstName("User")
                    .lastName("User")
                    .role(user)
                    .build();
            Users moderatorUser = Users.builder()
                    .email("moderator@essprit.tn")
                    .password(passwordEncoder.encode("moderator"))
                    .firstName("Moderator")
                    .lastName("Moderator")
                    .role(moderator)
                    .build();
            Users studentUser = Users.builder()
                    .email("student@esprit.tn")
                    .password(passwordEncoder.encode("student"))
                    .firstName("Student")
                    .lastName("Student")
                    .role(student)
                    .build();
            Users teacherUser = Users.builder()
                    .email("teacher@esprit.tn")
                    .password(passwordEncoder.encode("teacher"))
                    .firstName("Teacher")
                    .lastName("Teacher")
                    .role(teacher)
                    .build();
            userRepository.saveAllAndFlush(List.of(adminUser, userUser, moderatorUser, studentUser, teacherUser));

            log.info("Users created");

            ReclamationType bug = ReclamationType.builder().typeName("Bug").build();
            ReclamationType feature = ReclamationType.builder().typeName("Feature").build();
            ReclamationType task = ReclamationType.builder().typeName("Task").build();
            reclamationTypeRepository.saveAllAndFlush(List.of(bug, feature, task));

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
            accessFlowRepository.saveAllAndFlush(List.of(accessFlowBug, accessFlowFeature, accessFlowTask));

            log.info("Access flows created");

            Reclamation reclamation = Reclamation.builder()
                    .subject("Bug 1")
                    .description("Bug 1 description")
                    .type(bug)
                    .author(studentUser)
                    .progress(Progress.WAITING)
                    .build();
            reclamationRepository.save(reclamation);

            log.info("Reclamations created");

        };
    }
}
