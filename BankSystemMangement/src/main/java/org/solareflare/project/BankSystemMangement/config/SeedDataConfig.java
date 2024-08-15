package org.solareflare.project.BankSystemMangement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solareflare.project.BankSystemMangement.entities.Employee;
import org.solareflare.project.BankSystemMangement.repositories.UserRepository;
import org.solareflare.project.BankSystemMangement.services.UserService;
import org.solareflare.project.BankSystemMangement.enums.Position;
import org.solareflare.project.BankSystemMangement.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            Employee admin = Employee.employeeBuilder()
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@admin.com")
                    .password(passwordEncoder.encode("password"))
                    .build();
            admin.setRole(Role.ROLE_EMPLOYEE);
            admin.setPosition(Position.BRANCH_MANAGER);

            userService.saveUser(admin);
        }
    }

}