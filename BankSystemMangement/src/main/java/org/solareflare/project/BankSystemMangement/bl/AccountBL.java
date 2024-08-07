package org.solareflare.project.BankSystemMangement.bl;



import org.solareflare.project.BankSystemMangement.beans.*;
import org.solareflare.project.BankSystemMangement.dao.*;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.solareflare.project.BankSystemMangement.utils.NotificationMessage;
import org.solareflare.project.BankSystemMangement.utils.StatusInSystem;
import org.solareflare.project.BankSystemMangement.utils.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class AccountBL {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ExternalBankBL externalBankBL; // Service to handle external bank transactions

    @Autowired
    private NotificationService notifyService;

    @Autowired
    private TransactionBL transactionBL;

    public Account addAccount(Account account) throws AccountAlreadyExistException, AccountNotValidException, CustomerNotRegisteredException, CustomerNotValidException {
        if (account == null)
            throw new AccountNotValidException();

        if (account.getId() != null && this.accountDAO.existsById(account.getId()))
            throw new AccountAlreadyExistException(account);

        System.out.println("Adding account " + account);
        return this.accountDAO.save(account);
    }

    public Account getAccountById(Long id) throws NotFoundException {
        Optional<Account> account = this.accountDAO.findById(id);
        if (account.isPresent())
            return account.get();
        try {
            throw new NotFoundException();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Account> getAllAccounts() {
        return this.accountDAO.findAll();
    }


    public Account updateAccount(Account account) {
        return this.accountDAO.save(account);
    }

    public void deleteAccount(Long id) {
        this.accountDAO.deleteById(id);
    }

    public BigDecimal getAccountBalance(Long accountId) throws AccountNotFoundException {
        Account account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);
        return account.getBalance();
    }

        public void updateAccountStatus(Long accountId) throws AccountNotFoundException {
        Account account = accountDAO.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId+""));
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            account.setStatus(StatusInSystem.IRREGULAR);
            Customer customer = accountDAO.findCustomerById(accountId);
            String name = customer.getFirstName()+" "+customer.getLastName();
            String msg = "Dear " +name+",\n\nYour account is overdrawn. " +
                    "Please take immediate action to resolve this issue.\n\nBest regards,\nYour Bank";

            NotificationMessage.notifyMessage(customer.getEmail().toString(), "ACCOUNT STATUS",msg);
        }
        accountDAO.save(account);
    }

    public void suspendAccount(Long accountId) throws AccountNotFoundException {
        Account account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);
        account.setStatus(StatusInSystem.SUSPENDED);
        accountDAO.save(account);
    }

    public void restrictAccount(Long accountId) throws AccountNotFoundException {
        Account account = accountDAO.findById(accountId).orElseThrow(AccountNotFoundException::new);
        account.setStatus(StatusInSystem.RESTRICTED);
        accountDAO.save(account);
    }

    public Account deposit(Long accountId, Double amount) throws Exception {

        Account account = accountDAO.findAccountById(accountId);
        System.out.println("BALANCE IS "+ account.toString());
        account.setBalance(account.getBalance().subtract(new BigDecimal(amount)));
         updateAccount(account);

        // RECORD TRANSACTION
        transactionBL.addTransaction(TransactionType.WITHDRAW, amount,account, account);

        // Notify customer
        notifyAccountHolder(account,"Deposit", amount);
        return account;
    }

    public Account withdraw(Long accountId, Double amount) throws Exception {
        Account account = accountDAO.findAccountById(accountId);
        if (account == null) {
            throw new Exception("Account not found");
        }
        if (account.getBalance().compareTo( new BigDecimal(amount))<0) {
            throw new Exception("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(new BigDecimal(amount)));
        updateAccount(account);

        // RECORD TRANSACTION
        transactionBL.addTransaction(TransactionType.WITHDRAW, amount,account, account);

        // Notify customer
        notifyAccountHolder(account,"withdrawal", amount);
        return account;
    }

    public void transfer(Long fromAccountId, Long toAccountId, Double amount) throws Exception {
        Account fromAccount = accountDAO.findAccountById(fromAccountId);
        Account toAccount = accountDAO.findAccountById(toAccountId);

        if (fromAccount == null || toAccount == null) {
            throw new Exception("One or both accounts not found");
        }

        withdraw(fromAccountId, amount);
        deposit(toAccountId, amount);

        //RECORD TRANSACTION
        transactionBL.addTransaction(TransactionType.TRANSFER, amount, fromAccount, toAccount);

        notifyAccountHolder(fromAccount, "Transfer Out", amount);
        notifyAccountHolder(toAccount, "Transfer In", amount);
    }

    public void transferToExternalBank(Long fromAccountId, String externalBankAccountId, Double amount) throws Exception {
        withdraw(fromAccountId, amount);
        externalBankBL.transferToExternalBank(externalBankAccountId, amount);
    }

    public void notifyAccountHolder(Account account, String transactionType, Double amount) {
        String message = String.format("A %s of $%.2f has been made to your account. Current balance: $%.2f",
                transactionType, amount, account.getBalance());
        notifyService.sendEmail(account.getCustomer().getEmail(), transactionType + " Notification", message);
//        notifyService.sendSms(account.getCustomer().getPhoneNumber(), message); //TO DO LATER
    }
}
