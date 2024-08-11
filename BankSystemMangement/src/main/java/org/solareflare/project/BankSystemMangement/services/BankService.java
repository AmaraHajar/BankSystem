package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.beans.*;
import org.solareflare.project.BankSystemMangement.dao.BankDAO;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BankService {

    @Autowired
    private BankDAO bankDAO;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private NotificationService notificationService;

    public List<Bank> getBank() {
        try {
            return this.bankDAO.findAll();
        } catch (Exception e) {
            throw new CustomException(Bank.class, "Failed to retrieve banks");
        }
    }

    public Bank getByName(String name) {
        Bank bank;
        try {
            bank = bankDAO.findByName(name);
            return bank;
        } catch (Exception e) {
            throw new CustomException(Bank.class, "Failed to retrieve bank by name");
        }
    }

    public void updateBankBalance(Bank bank) {
        try {
            bankDAO.save(bank);
        } catch (Exception e) {
            throw new CustomException(Bank.class, "Failed to update bank balance");
        }
    }

    public Bank registerBank(Bank bank) throws AlreadyExistException {
        Bank currentBank;
        try{
            currentBank = bankDAO.findByName(bank.getName());
        }catch (Exception e){
            throw  new AlreadyExistException(bank);
        }
        return this.bankDAO.save(bank);
    }

    public List<Employee> getEmployees() {
        try {
            return employeeService.getAllEmployees();
        } catch (Exception e) {
            throw new CustomException(Employee.class, "Failed to retrieve employees");
        }
    }

    public void dailyEmailNotification() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            for (Account account : accounts) {
                if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                    Customer customer = account.getCustomer();
                    String name = customer.getFirstName() + " " + customer.getLastName();
                    String msg = "Dear " + name + ",\n\nYour account is overdrawn. " +
                            "Please take immediate action to resolve this issue.\n\nBest regards,\nYour Bank";
                    notificationService.sendEmail(customer.getEmail(), "Overdraft Alert", msg);
                }
            }
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to send daily email notifications");
        }
    }

    public void monthlyEmailNotificationForDownAccounts() {
        try {
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();
            List<Payment> payments = paymentService.getPaymentsByMonth(currentMonth, currentYear);
            List<Loan> loans = loanService.getAllLoans();
            for (Loan loan : loans) {
                for (Payment payment : payments) {
                    if (payment.getLoan().getId().equals(loan.getId())) {
                        if (payment.getAmount().compareTo(loan.getMonthlyRepayment()) < 0) {
                            Account account = loan.getAccount();
                            notificationService.sendEmail(account.getCustomer().getEmail(), "Overdue Payment", "Your payment was not as expected.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to send monthly email notifications for down accounts");
        }
    }

    public void updateLoanStatus() throws LoanNotFoundException {
        try {
            List<Loan> loans = loanService.getAllLoans();
            for (Loan loan : loans) {
                if (Objects.equals(loan.getPaidPayments(), loan.getPaymentsNumber())) {
                    loan.setStatus(ActionStatus.CLOSED);
                    loanService.updateLoan(loan);
                }
            }
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to update loan status");
        }
    }

    public List<Transaction> displayRecentTransactions() {
        try {
            return transactionService.getAllTransactions();
        } catch (Exception e) {
            throw new CustomException(Transaction.class, "Failed to retrieve recent transactions");
        }
    }

    public List<Loan> displayLoanList() {
        try {
            return loanService.getAllLoans();
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to retrieve loan list");
        }
    }

    public void sendNotificationToLowAccount() {
        try {
            monthlyEmailNotificationForDownAccounts();
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to send notifications to low accounts");
        }
    }
}
