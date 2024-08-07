package org.solareflare.project.BankSystemMangement;


import org.solareflare.project.BankSystemMangement.beans.*;
import org.solareflare.project.BankSystemMangement.bl.*;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class SystemManager {

    @Autowired
    private CustomerBL customerBL;

    @Autowired
    private AccountBL accountBL;

    @Autowired
    private LoanBL loanBL;

    @Autowired
    private BankBL bankBL;

    @Value("${bank.name}")
    private String bankName;

    public void run() throws Exception {

        Bank bank =  bankBL.getByName(bankName);
        Address address =  bank.getAddress();
        ///// Add new Customer
        Customer customer = Customer.customerBuilder()
                .firstName("Hajar")
                .lastName("Amara")
                .password("password")
                .email("hajar.amara.9@gmail.com")
                .phoneNumber("+972 0522216445")
                .build();


        customer = customerBL.addCustomer(customer);
        customer.setBank(bank);
        customer.setAddress(address);

        customer = customerBL.updateCustomer(customer);

        /////// Create new account for the customer

        Account account = Account.builder()
                .customer(customer)
                .balance(new BigDecimal(5000))
                .bank(bank).build();

        account = accountBL.addAccount(account);

        Loan loan = Loan.builder()
                .amount(2500.0)
                .startDate(Instant.now())
                .account(account)
                .status(ActionStatus.APPROVED)
                .monthlyRepayment(500.0).build();

        loan = loanBL.addLoan(loan);
        account.setBalance(account.getBalance().add(new BigDecimal(loan.getAmount())));
        accountBL.updateAccount(account);

        account = accountBL.withdraw(account.getId(), loan.getAmount());

        Payment payment = Payment.builder()
                .date(Instant.now())
                .status(ActionStatus.APPROVED)
                .amount(500.0)
                .loan(loan)
                .build();
        loan = loanBL.updateLoanPayment(payment);

        account = accountBL.deposit(account.getId(),200.0);



    }


}
