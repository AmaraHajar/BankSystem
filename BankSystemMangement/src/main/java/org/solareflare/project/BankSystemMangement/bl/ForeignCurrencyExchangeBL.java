package org.solareflare.project.BankSystemMangement.bl;



import org.solareflare.project.BankSystemMangement.beans.Account;
import org.solareflare.project.BankSystemMangement.beans.Bank;
import org.solareflare.project.BankSystemMangement.beans.ForeignCurrencyExchange;
import org.solareflare.project.BankSystemMangement.dao.BankDAO;
import org.solareflare.project.BankSystemMangement.dao.ForeignCurrencyExchangeDAO;
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
public class ForeignCurrencyExchangeBL {

    @Autowired
    private ForeignCurrencyExchangeDAO forexDAO;

    @Autowired
    private AccountBL accountBL;

    @Autowired
    private BankDAO bankDAO;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${api.currency.exchange}")
    private String apiUrl;

    public ForeignCurrencyExchangeBL(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Double> getExchangeRates() throws Exception {
        ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
        Map<String, Object> rates = (Map<String, Object>) response.getBody().get("rates");
        int index=0;

        Map<String, Double> convertedRates = new HashMap<>();
        for (Map.Entry<String, Object> entry : rates.entrySet()) {
            Object value = entry.getValue();
            Double rate;

            // Convert value to Double if it's an Integer
            if (value instanceof Integer) {
                rate = ((Integer) value).doubleValue();
            } else if (value instanceof Double) {
                rate = (Double) value;
            } else {
                // Handle unexpected value types gracefully
                throw new IllegalArgumentException("Unexpected rate type: " + value.getClass());
            }
            this.addForex(entry.getKey(), rate);
            convertedRates.put(entry.getKey(), rate);

        }
        return convertedRates;
    }


    public List<ForeignCurrencyExchange> getAllForex() {
        return forexDAO.findAll();
    }

    public ForeignCurrencyExchange getForexById(Long id){
        return forexDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Foreign Currency Exchange not found for id: " + id));

    }

    public ForeignCurrencyExchange getForexByName(String name){
        return forexDAO.findByName(name)
                .orElseThrow(() -> new RuntimeException("Foreign Currency Exchange not found for name: " + name));
    }


    public ForeignCurrencyExchange addForex(String name, Double rate) throws Exception {
        Optional<ForeignCurrencyExchange> existingForex = forexDAO.findByName(name);
        if (existingForex.isPresent()) {
            updateForex(name, rate);
        }
        ForeignCurrencyExchange forex = new ForeignCurrencyExchange();
        forex.setName(name);
        return addOrEditDetails(forex, rate);
    }

    public ForeignCurrencyExchange updateForex(String name, Double rate) throws Exception {
        ForeignCurrencyExchange existingForex = this.getForexByName(name);
        return addOrEditDetails(existingForex, rate);

    }

    private ForeignCurrencyExchange addOrEditDetails(ForeignCurrencyExchange forex, Double rate){
        forex.setBank(getBank());
        forex.setRate(rate);
        return this.saveForex(forex);
    }

    public ForeignCurrencyExchange saveForex(ForeignCurrencyExchange forex){
        System.out.println(" save here ");
        return forexDAO.save(forex);
    }

    public void deleteForexById(Long id) throws Exception {
        ForeignCurrencyExchange forex = forexDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Foreign Currency Exchange not found for id: " + id));
        forexDAO.delete(forex);
    }

    public void deleteForexByName(String name) throws Exception {
        ForeignCurrencyExchange forex = forexDAO.findByName(name)
                .orElseThrow(() -> new RuntimeException("Foreign Currency Exchange not found for name: " + name));
        forexDAO.delete(forex);
    }

    public void deleteAllForex() {
        forexDAO.deleteAll();
    }


//    // Deposit method
//    public void deposit(String currency, Double amount) throws Exception {
//        Bank bank = getBank();
//        Double currentBalance = bank.getBalances().getOrDefault(currency, 0.0);
//        bank.getBalances().put(currency, currentBalance + amount);
//        bankDAO.save(bank);
//    }
//
//    // Withdrawal method
//    public void withdraw(String currency, Double amount) throws Exception {
//        Bank bank = getBank();
//        Double currentBalance = bank.getBalances().getOrDefault(currency, 0.0);
//        if (currentBalance < amount) {
//            throw new Exception("Insufficient funds for currency: " + currency);
//        }
//        bank.getBalances().put(currency, currentBalance - amount);
//        bankDAO.save(bank);
//    }

    private Bank getBank() {
        return bankDAO.findById(Long.parseLong("1"))
                .orElseThrow(() -> new RuntimeException("Bank not found"));
    }

    public long countAllForex() {
        return forexDAO.count();
    }

    public Map<String, Double> getDailyRates() throws Exception {
        if(countAllForex() > 0 ){
            deleteAllForex();
        }
        return getExchangeRates();
    }

    public void forexProcess(String currencyName, Double currencyAmount, Account account, String processType) throws Exception {
        Double amountInILS = getAmountCurrecny(currencyName, currencyAmount);
        switch (processType){
            case "DEPOSIT": accountBL.deposit(account.getId(), amountInILS);
                break;
            case "WITHDRAW": accountBL.withdraw(account.getId(), amountInILS);
                break;
        }
        // Retrieve the bank and update its balance
        Bank bank  = getBank();
        bank.setBalance(bank.getBalance().add(BigDecimal.valueOf(amountInILS)));
        bankDAO.save(bank);
    }

    private Double getAmountCurrecny(String currencyName, Double amount){
        // Retrieve the exchange rate for Currency Type to ILS
        ForeignCurrencyExchange forex = getForexByName("USD");
        // Convert the amount from Currency Type  to ILS
        return amount *  forex.getRate();
    }
}
