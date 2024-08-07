package org.solareflare.project.BankSystemMangement.config;




import org.solareflare.project.BankSystemMangement.beans.Account;
import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.beans.Loan;
import org.solareflare.project.BankSystemMangement.beans.Payment;
import org.solareflare.project.BankSystemMangement.bl.*;
import org.solareflare.project.BankSystemMangement.dao.LoanDAO;
import org.solareflare.project.BankSystemMangement.exceptions.LoanNotFoundException;
import org.solareflare.project.BankSystemMangement.exceptions.NotFoundException;
import org.solareflare.project.BankSystemMangement.exceptions.PaymentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Configuration
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private ForeignCurrencyExchangeBL foreignCurrencyExchangeBL;

    @Autowired
    private BankBL bankBL;

    @Autowired
    private LoanBL loanBL;

    @Scheduled(cron = "0 0 12 * * ?") // Runs daily at noon
    public void fetchDailyExchangeRates() throws Exception {
        Map<String, Double> rates = foreignCurrencyExchangeBL.getDailyRates();
    }

    @Scheduled(cron = "0 0 8 * * ?") // Runs every day at 8 AM
    public void sendOverdraftNotifications() {
        bankBL.dailyEmailNotification();
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the first day of every month
    public void chargeMonthlyRepayments() throws NotFoundException, PaymentNotFoundException, LoanNotFoundException {
       loanBL.calculateMonthlyRepayment();
    }

}
