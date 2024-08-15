package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.entities.Account;
import org.solareflare.project.BankSystemMangement.entities.Bank;
import org.solareflare.project.BankSystemMangement.entities.ForeignCurrencyExchange;
import org.solareflare.project.BankSystemMangement.repositories.BankRepository;
import org.solareflare.project.BankSystemMangement.repositories.ForeignCurrencyExchangeRepository;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ForeignCurrencyExchangeService {

    @Autowired
    private ForeignCurrencyExchangeRepository  forexRepository;

    @Autowired
    private AccountService accountBL;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.currency.exchange}")
    private String apiUrl;

    public ForeignCurrencyExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> getExchangeRates() throws Exception {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
            Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");

            Map<String, Double> convertedRates = new HashMap<>();
            for (Map.Entry<String, Object> entry : rates.entrySet()) {
                Object value = entry.getValue();
                Double rate;

                if (value instanceof Integer) {
                    rate = ((Integer) value).doubleValue();
                } else if (value instanceof Double) {
                    rate = (Double) value;
                } else {
                    throw new IllegalArgumentException("Unexpected rate type: " + value.getClass());
                }

                this.addForex(entry.getKey(), rate);
                convertedRates.put(entry.getKey(), rate);
            }
            return convertedRates;
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to retrieve exchange rates: " + e.getMessage());
        }
    }

    public List<ForeignCurrencyExchange> getAllForex() throws CustomException {
        try {
            return forexRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to retrieve all foreign exchanges");
        }
    }

    public ForeignCurrencyExchange getForexById(Long id) throws ForeignCurrencyExchangeNotFoundException {
        return forexRepository.findById(id)
                .orElseThrow(() -> new ForeignCurrencyExchangeNotFoundException("Foreign Currency Exchange not found for id: " + id));
    }

    public ForeignCurrencyExchange getForexByName(String name) throws ForeignCurrencyExchangeNotFoundException {
        return forexRepository.findByName(name)
                .orElseThrow(() -> new ForeignCurrencyExchangeNotFoundException("Foreign Currency Exchange not found for name: " + name));
    }

    public ForeignCurrencyExchange addForex(String name, Double rate) throws Exception {
        try {
            Optional<ForeignCurrencyExchange> existingForex = forexRepository.findByName(name);
            if (existingForex.isPresent()) {
                return updateForex(name, rate);
            }
            ForeignCurrencyExchange forex = ForeignCurrencyExchange.builder().name(name).build();
            return addOrEditDetails(forex, rate);
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to add forex: " + e.getMessage());
        }
    }

    public ForeignCurrencyExchange updateForex(String name, Double rate) throws Exception {
        try {
            ForeignCurrencyExchange existingForex = this.getForexByName(name);
            return addOrEditDetails(existingForex, rate);
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to update forex: " + e.getMessage());
        }
    }

    private ForeignCurrencyExchange addOrEditDetails(ForeignCurrencyExchange forex, Double rate) throws CustomException {
        try {
            forex.setBank(getBank());
            forex.setRate(rate);
            return this.saveForex(forex);
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to add or edit forex details: " + e.getMessage());
        }
    }

    public ForeignCurrencyExchange saveForex(ForeignCurrencyExchange forex) throws CustomException {
        try {
            return forexRepository.save(forex);
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to save forex: " + e.getMessage());
        }
    }

    public void deleteForexById(Long id) throws ForeignCurrencyExchangeNotFoundException, CustomException {
        try {
            ForeignCurrencyExchange forex = this.getForexById(id);
            forexRepository.delete(forex);
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to delete forex by ID: " + e.getMessage());
        }
    }

    public void deleteForexByName(String name) throws ForeignCurrencyExchangeNotFoundException, CustomException {
        try {
            ForeignCurrencyExchange forex = this.getForexByName(name);
            forexRepository.delete(forex);
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to delete forex by name: " + e.getMessage());
        }
    }

    public void deleteAllForex() throws CustomException {
        try {
            forexRepository.deleteAll();
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to delete all forex records: " + e.getMessage());
        }
    }

    private Bank getBank() throws BankNotFoundException {
        return bankRepository.findById(1L)
                .orElseThrow(() -> new BankNotFoundException("Bank not found"));
    }

    public long countAllForex() throws CustomException {
        try {
            return forexRepository.count();
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to count all forex records: " + e.getMessage());
        }
    }

    public Map<String, Double> getDailyRates() throws Exception {
        try {
            if (countAllForex() > 0) {
                deleteAllForex();
            }
            return getExchangeRates();
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to get daily rates: " + e.getMessage());
        }
    }

    public void forexProcess(String currencyName, Double currencyAmount, Account account, String processType) throws Exception {
        try {
            Double amountInILS = getAmountCurrency(currencyName, currencyAmount);
            switch (processType) {
                case "DEPOSIT":
                    accountBL.deposit(account.getId(), amountInILS);
                    break;
                case "WITHDRAW":
                    accountBL.withdraw(account.getId(), amountInILS);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid process type: " + processType);
            }

            // Retrieve the bank and update its balance
            Bank bank = getBank();
            bank.setBalance(bank.getBalance().add(BigDecimal.valueOf(amountInILS)));
            bankRepository.save(bank);
        } catch (Exception e) {
            throw new CustomException(ForeignCurrencyExchange.class, "Failed to process forex transaction: " + e.getMessage());
        }
    }

    private Double getAmountCurrency(String currencyName, Double amount) throws ForeignCurrencyExchangeNotFoundException {
        // Retrieve the exchange rate for Currency Type to ILS
        ForeignCurrencyExchange forex = getForexByName(currencyName);
        // Convert the amount from Currency Type to ILS
        return amount * forex.getRate();
    }
}
