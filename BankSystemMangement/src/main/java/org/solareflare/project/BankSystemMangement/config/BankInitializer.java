package org.solareflare.project.BankSystemMangement.config;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.solareflare.project.BankSystemMangement.entities.Address;
import org.solareflare.project.BankSystemMangement.entities.Bank;
import org.solareflare.project.BankSystemMangement.repositories.AddressRepository;
import org.solareflare.project.BankSystemMangement.repositories.BankRepository;
import org.solareflare.project.BankSystemMangement.services.ForeignCurrencyExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BankInitializer {

    @Value("${bank.name}")
    private String bankName;

    @Value("${bank.address}")
    private String bankAddress;

    @Value("${address.city}")
    private String bankCity;

    @Value("${address.street}")
    private String bankStreet;

    @Value("${address.state}")
    private String bankState;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private AddressRepository addressDAO;

    @Autowired
    private ForeignCurrencyExchangeService foreignCurrencyExchangeService;

    @PostConstruct
    @Transactional
    public void init() throws Exception {

        Address address ;
        address = Address.builder()
                .city(this.bankCity)
                .street(this.bankStreet)
                .state(this.bankState)
                .build();

        if (bankRepository.findByName(bankName) == null) {
            Bank bank = new Bank();
            bank.setName(bankName);
            bank.setAddress(address);
            bankRepository.save(bank);
        }

        fetchDailyExchangeRates();

    }

    public void fetchDailyExchangeRates() throws Exception {
        Map<String, Double> rates = foreignCurrencyExchangeService.getDailyRates();
    }
}