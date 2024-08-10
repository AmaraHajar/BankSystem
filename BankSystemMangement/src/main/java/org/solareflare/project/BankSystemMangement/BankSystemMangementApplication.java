package org.solareflare.project.BankSystemMangement;

import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class BankSystemMangementApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Bank Management System");
        ApplicationContext context = SpringApplication.run(BankSystemMangementApplication.class, args);
        SystemManager manager = context.getBean(SystemManager.class);
        manager.run();
    }
}



