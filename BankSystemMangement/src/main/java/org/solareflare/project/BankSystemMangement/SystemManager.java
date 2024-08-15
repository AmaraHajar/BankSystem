package org.solareflare.project.BankSystemMangement;


import org.solareflare.project.BankSystemMangement.entities.*;
import org.solareflare.project.BankSystemMangement.services.*;
import org.solareflare.project.BankSystemMangement.enums.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class SystemManager {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private BankService bankBL;

    @Value("${bank.name}")
    private String bankName;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;


    public void run() throws Exception {

        Bank bank = bankBL.getByName(bankName);
        Address address = bank.getAddress();
        ///// Add new Customer
        Customer customer = Customer.customerBuilder()
                .firstName("Hajar")
                .lastName("Amara")
                .password("password")
                .email("hajar.amara.9@gmail.com")
                .phoneNumber("+972 0522216445")
                .build();


        customer = customerService.addCustomer(customer);
        customer.setBank(bank);
        customer.setAddress(address);

        customer = customerService.updateCustomer(customer);

        /////// Create new account for the customer

        Account account = Account.builder()
                .customer(customer)
                .balance(new BigDecimal(5000))
                .bank(bank).build();

        account = accountService.addAccount(account);

        Loan loan = Loan.builder()
                .amount(2500.0)
                .startDate(Instant.now())
                .account(account)
                .status(ActionStatus.APPROVED)
                .monthlyRepayment(500.0).build();

        loan = loanService.addLoan(loan);
        account.setBalance(account.getBalance().add(new BigDecimal(loan.getAmount())));
        accountService.updateAccount(account);

        account = accountService.withdraw(account.getId(), loan.getAmount());

        Payment payment = Payment.builder()
                .date(Instant.now())
                .status(ActionStatus.APPROVED)
                .amount(500.0)
                .loan(loan)
                .build();
        loan = loanService.updateLoanPayment(payment);

        account = accountService.deposit(account.getId(), 200.0);
    }
}