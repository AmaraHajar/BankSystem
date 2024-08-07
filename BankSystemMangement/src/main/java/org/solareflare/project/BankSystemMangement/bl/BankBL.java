package org.solareflare.project.BankSystemMangement.bl;


import org.solareflare.project.BankSystemMangement.beans.*;
import org.solareflare.project.BankSystemMangement.dao.BankDAO;
import org.solareflare.project.BankSystemMangement.exceptions.BankNotValidException;
import org.solareflare.project.BankSystemMangement.exceptions.LoanNotFoundException;
import org.solareflare.project.BankSystemMangement.exceptions.NameNotValidException;
import org.solareflare.project.BankSystemMangement.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BankBL {

    @Autowired
    private BankDAO bankDAO;

    @Autowired
    private EmployeesBL employeesBL;

    @Autowired
    private AccountBL accountBL;

    @Autowired
    private PaymentBL paymentBL;

    @Autowired
    private TransactionBL transactionBL;

    @Autowired
    private LoanBL loanBL;

    @Autowired
    private NotificationService notificationService;

    public List<Bank> getBank() {
        return this.bankDAO.findAll();
    }

    public Bank getByName(String name) {
        return bankDAO.findByName(name);
    }

    public void updateBankBalance(Bank bank){
        bankDAO.save(bank);
    }

    public Bank registerBank(Bank bank) throws BankNotValidException, NameNotValidException {
        if (bank == null)
            throw new BankNotValidException();
        if (!Validation.isValidName(bank.getName(), bank.getName()))

            throw new NameNotValidException();
        if (!getBank().isEmpty())
            throw new BankNotValidException();
        bank = this.bankDAO.save(bank);
        return bank;
    }

    public List<Employee> getEmployees() {
        return employeesBL.getAllEmployees();
    }


    public void dailyEmailNotification() {
        List<Account> accounts = accountBL.getAllAccounts();

        for (Account account: accounts) {
            if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                Customer customer = account.getCustomer();
                String name = customer.getFirstName()+" "+ customer.getLastName();
                String msg = "Dear " +name+",\n\nYour account is overdrawn. " +
                        "Please take immediate action to resolve this issue.\n\nBest regards,\nYour Bank";
                notificationService.sendEmail(customer.getEmail(), "Overdraft Alert",msg);
            }
        }
    }

    public void monthlyEmailNotificationForDownAccounts() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        List<Payment> payments = paymentBL.getPaymentsByMonth(currentMonth, currentYear);
        List<Loan> loans = loanBL.getAllLoans();
        for(Loan loan : loans){
            for(Payment payment: payments){
                if(payment.getLoan().getId() == loan.getId()){
                    if(payment.getAmount() < loan.getMonthlyRepayment()){
                        Account account = loan.getAccount();
                        notificationService.sendEmail(account.getCustomer().getEmail(),"Overdow","Your paymnet not as expected");
                    }
                                      }
            }
        }
    }

    public void updateLoanStatus() throws LoanNotFoundException {
        List<Loan> loans = loanBL.getAllLoans();
        for(Loan loan : loans){
           if(Objects.equals(loan.getPaidPayments(), loan.getPaymentsNumber())){
               loan.setStatus(ActionStatus.CLOSED);
               loanBL.updateLoan(loan);
           }
        }

    }
    public List<Transaction> displayRecentTransactions() {
        return transactionBL.getAllTransactions();
    }

    public List<Loan> displayLoanList() {
        return loanBL.getAllLoans();
    }

    public void sendNotificationToLowAccount(){
        monthlyEmailNotificationForDownAccounts();
        // add also for withdraw ......
    }

//    /// to reimplement
//    public void updateAccountStatus(Long accountId) throws AccountNotFoundException {
//        Account account = accountDAO.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId+""));
//        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
//            account.setStatus(StatusInSystem.IRREGULAR);
//            Customer customer = accountDAO.findCustomerById(accountId);
//            NotificationMessage.notifyMessage(customer.getEmail().toString(), "ACCOUNT STATUS","Your account is overdrawn.");
//        }
//        accountDAO.save(account);
//    }
}
