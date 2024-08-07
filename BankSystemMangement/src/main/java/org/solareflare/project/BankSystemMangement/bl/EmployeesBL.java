package org.solareflare.project.BankSystemMangement.bl;




import org.solareflare.project.BankSystemMangement.beans.*;
import org.solareflare.project.BankSystemMangement.dao.AccountDAO;
import org.solareflare.project.BankSystemMangement.dao.EmployeeDAO;
import org.solareflare.project.BankSystemMangement.dto.TransferRequest;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.solareflare.project.BankSystemMangement.utils.StatusInSystem;
import org.solareflare.project.BankSystemMangement.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class EmployeesBL {


    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private AccountBL accountBL;

    public Employee addEmployee(Employee employee) throws EmployeeNotValidException, IdNumberNotValidException, NameNotValidException, EmailNotValidException {

        return saveEmployee(validateEmployee(employee));
    }

    public Employee updateEmployee(Employee employee){
        return saveEmployee(employee);
    }

    public Employee getEmployeeById(Long id) {
        return this.employeeDAO.findById(id).isEmpty() ? null : this.employeeDAO.findById(id).get();
    }

    public Employee getEmployeeByIdNumber(String employeeId) {
        return employeeDAO.findByIdNumber(employeeId);
    }

    public List<Employee> getAllEmployees() {
        return this.employeeDAO.findAll();
    }


    public Employee saveEmployee(Employee employee){
        return  employeeDAO.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeDAO.deleteById(id);
    }

    public Employee validateEmployee(Employee employee) throws EmployeeNotValidException, EmailNotValidException, IdNumberNotValidException, NameNotValidException{
        if (employee == null) throw new EmployeeNotValidException();
        if (!Validation.isValidEmail(employee.getEmail()))
            throw new EmailNotValidException();
        if (!Validation.isValidIdNumber(employee.getIdNumber()))
            throw new IdNumberNotValidException();
        if (!Validation.isValidName(employee.getFirstName(), employee.getLastName()))
            throw new NameNotValidException();
        return employee;
    }

    public void suspendAccount(Long accountId) {
        Account account = accountDAO.findAccountById(accountId);
        account.setStatus(StatusInSystem.IRREGULAR);
        accountDAO.save(account);
    }

    public void restrictAccount(Long accountId) {
        Account account = accountDAO.findAccountById(accountId);
        account.setStatus(StatusInSystem.RESTRICTED);
        accountDAO.save(account);
    }

    public void manageCustomerDeposit(Long employeeId, Long customerId, Long accountId, Double amount) throws Exception {
        // Employee-specific validation and logging
        accountBL.deposit(accountId, amount);
    }

    public void manageCustomerWithdrawal(Long employeeId, Long customerId, Long accountId, Double amount) throws Exception {
        // Employee-specific validation and logging
        accountBL.withdraw(accountId, amount);
    }

    public void manageCustomerTransfer(Long employeeId, Long customerId, Long fromAccountId, Long toAccountId, Double amount) throws Exception {
        // Employee-specific validation and logging
        accountBL.transfer(fromAccountId, toAccountId, amount);
    }

    public void transferToExternalBank(Long fromAccountId, String externalBankAccountId, Double amount) throws Exception {
        accountBL.transferToExternalBank(fromAccountId,externalBankAccountId,amount);
    }
}


//    public Loan processLoan(Loan loan) throws LoanNotFoundException {
//        loan = accountBL.getLoanById(loan.getId());
//        if(loan.getAccount().getStatus() == StatusInSystem.REGULAR &&
//           Double.valueOf(loan.getAmount()).compareTo(calculatePayments(loan.getStartDate(),loan.getDueDate(),loan.getMonthlyRepayment())) == 0) {
//            loan.getAccount().setBalance(loan.getAccount().getBalance().add(BigDecimal.valueOf(loan.getAmount())));
//            loan.setPaid(false);
//            try {
//                loan.setStatus(ActionStatus.APPROVED);
//                loan.setAccount(accountBL.updateAccount(loan.getAccount()));
//                return accountBL.updateLoan(loan);
//            } catch (LoanNotFoundException e) {
//                loan.setStatus(ActionStatus.REJECTED);
//                loan.getAccount().setBalance(loan.getAccount().getBalance().subtract(BigDecimal.valueOf(loan.getAmount())));
//                return loan;
//            }
//        }
//        loan.setStatus(ActionStatus.REJECTED);
//        return accountBL.updateLoan(loan);
//    }

//    public Withdrawal processWithdrawal(Withdrawal withdrawal) {
//        if (withdrawal.getAccount().getStatus() == StatusInSystem.REGULAR &&
//            withdrawal.getAccount().getBalance().compareTo(BigDecimal.valueOf(withdrawal.getAmount())) >= 0) {
//            withdrawal.getAccount().setBalance(withdrawal.getAccount().getBalance().subtract(BigDecimal.valueOf(withdrawal.getAmount())));
//            withdrawal.setStatus(ActionStatus.APPROVED);
//            withdrawal.setAccount(accountBL.updateAccount(withdrawal.getAccount()));
//            return accountBL.updateWithdrawal(withdrawal);
//        }
//        withdrawal.setStatus(ActionStatus.REJECTED);
//        return accountBL.updateWithdrawal(withdrawal);
//    }


//    public Deposit processDeposit(Deposit deposit) throws NotFoundException {
//        deposit = accountBL.getDepositById(deposit.getId());
//        if(deposit.getAccount().getStatus() == StatusInSystem.REGULAR) {
//            deposit.getAccount().setBalance(deposit.getAccount().getBalance().add(BigDecimal.valueOf(deposit.getAmount())));
//            deposit.setAccount(accountBL.updateAccount(deposit.getAccount()));
//            deposit.setStatus(ActionStatus.APPROVED);
//            return accountBL.updateDeposit(deposit);
//        }
//        deposit.setStatus(ActionStatus.REJECTED);
//        return accountBL.updateDeposit(deposit);
//    }

//    public Payment processPayment(Payment payment){
//        if(payment.getAccount().getStatus() == StatusInSystem.REGULAR) {
//            payment.getAccount().setBalance(payment.getAccount().getBalance().subtract(BigDecimal.valueOf(payment.getAmount())));
//            payment.setStatus(ActionStatus.APPROVED);
//            payment.setAccount(accountBL.updateAccount(payment.getAccount()));
//            return accountBL.updatePayment(payment);
//        }
//        payment.setStatus(ActionStatus.REJECTED);
//        return accountBL.updatePayment(payment);
//    }

//    public void transferFunds(TransferRequest transferRequest) throws InsufficientFundsException, AccountNotFoundException {
//        Account senderAccount = accountDAO.findById(transferRequest.getSenderAccountId())
//                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));
//        Account recipientAccount = accountDAO.findById(transferRequest.getRecipientAccountId())
//                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found"));
//
//        if (senderAccount.getBalance().compareTo( new BigDecimal(transferRequest.getAmount()))<0) {
//            throw new InsufficientFundsException("Insufficient funds in sender's account");
//        }
//
//        // Deduct the amount from the sender's account
//        senderAccount.setBalance(senderAccount.getBalance().subtract( new BigDecimal (transferRequest.getAmount())));
//
//        // Add the amount to the recipient's account
//        recipientAccount.setBalance(recipientAccount.getBalance().add(new BigDecimal(transferRequest.getAmount())));
//
//        // Save the updated accounts
//        accountDAO.save(senderAccount);
//        accountDAO.save(recipientAccount);
//    }

//    public Transaction processTransaction(Transaction transaction) {
//        if (transaction.getSender().getStatus() == StatusInSystem.REGULAR &&
//                transaction.getReceiver().getStatus() == StatusInSystem.REGULAR &&
//                transaction.getSender().getBalance().compareTo(BigDecimal.valueOf(transaction.getAmount())) >= 0) {
//            transaction.getSender().setBalance(transaction.getSender().getBalance().subtract(BigDecimal.valueOf(transaction.getAmount())));
//            transaction.getReceiver().setBalance(transaction.getReceiver().getBalance().add(BigDecimal.valueOf(transaction.getAmount())));
//            transaction.setSender(accountBL.updateAccount(transaction.getSender()));
//            transaction.setReceiver(accountBL.updateAccount(transaction.getReceiver()));
//            return accountBL.saveTransaction(transaction);
//        }
//        transaction.setStatus(ActionStatus.REJECTED);
//        return accountBL.saveTransaction(transaction);
//    }
//
//private Double calculatePayments(Instant from, Instant to, Double payment){
//    // Convert Instants to LocalDate
//    LocalDate date1 = from.atZone(ZoneId.systemDefault()).toLocalDate();
//    LocalDate date2 = to.atZone(ZoneId.systemDefault()).toLocalDate();
//
//    // Calculate the number of months between the two dates
//    long monthsBetween = ChronoUnit.MONTHS.between(date1, date2);
//    return payment * monthsBetween;
//}