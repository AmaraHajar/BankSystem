package org.solareflare.project.BankSystemMangement.config;

import lombok.RequiredArgsConstructor;
import org.solareflare.project.BankSystemMangement.entities.Bank;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    @Bean
    public RestTemplate restTemplateConfig() {
        return new RestTemplate();
    }

    @Bean
    public Bank bank() {
        return new Bank();
    }

}
