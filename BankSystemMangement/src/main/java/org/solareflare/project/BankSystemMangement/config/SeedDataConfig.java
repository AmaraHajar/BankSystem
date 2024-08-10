package org.solareflare.project.BankSystemMangement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solareflare.project.BankSystemMangement.beans.Employee;
import org.solareflare.project.BankSystemMangement.dao.UserDAO;
import org.solareflare.project.BankSystemMangement.services.UserService;
import org.solareflare.project.BankSystemMangement.utils.Position;
import org.solareflare.project.BankSystemMangement.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeedDataConfig implements CommandLineRunner {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userDAO.count() == 0) {

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