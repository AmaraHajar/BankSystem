package org.solareflare.project.BankSystemMangement.config;



import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.solareflare.project.BankSystemMangement.beans.Address;
import org.solareflare.project.BankSystemMangement.beans.Bank;
import org.solareflare.project.BankSystemMangement.bl.ForeignCurrencyExchangeBL;
import org.solareflare.project.BankSystemMangement.dao.AddressDAO;
import org.solareflare.project.BankSystemMangement.dao.BankDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
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
    private  BankDAO bankDAO;

    @Autowired
    private  AddressDAO addressDAO;

    @Autowired
    private ForeignCurrencyExchangeBL foreignCurrencyExchangeBL;

    @PostConstruct
    @Transactional
    public void init() throws Exception {

        Address address ;
        address = Address.builder()
                .city(this.bankCity)
                .street(this.bankStreet)
                .state(this.bankState)
                .build();

        if (bankDAO.findByName(bankName) == null) {
            Bank bank = new Bank();
            bank.setName(bankName);
            bank.setAddress(address);
            bankDAO.save(bank);
        }

        fetchDailyExchangeRates();

    }

    public void fetchDailyExchangeRates() throws Exception {
        Map<String, Double> rates = foreignCurrencyExchangeBL.getDailyRates();
    }
}