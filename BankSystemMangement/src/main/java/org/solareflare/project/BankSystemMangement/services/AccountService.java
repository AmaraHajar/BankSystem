package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.entities.*;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.enums.StatusInSystem;
import org.solareflare.project.BankSystemMangement.enums.TransactionType;
import org.solareflare.project.BankSystemMangement.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NotificationService notifyService;

    @Autowired
    private TransactionService transactionService;

    public Account addAccount(Account account) throws AccountAlreadyExistException, AccountNotValidException, CustomerNotRegisteredException, CustomerNotValidException {
        if (account == null) throw new AccountNotValidException();

        if (account.getId() != null && this.accountRepository.existsById(account.getId())) {
            throw new AccountAlreadyExistException(account);
        }

        try {
            System.out.println("Adding account " + account);
            return this.accountRepository.save(account);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to add account");
        }
    }

    public Account getAccountById(Long id) {
        try {
            Optional<Account> account = this.accountRepository.findById(id);
            return account.orElseThrow(() -> new CustomException(Account.class, "Account Not Found"));
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to retrieve account");
        }
    }

    public List<Account> getAllAccounts() {
        try {
            return this.accountRepository.findAll();
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to retrieve accounts");
        }
    }

    public Account updateAccount(Account account) {
        try {
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to update account");
        }
    }

    public void deleteAccount(Long id) {
        try {
            this.accountRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to delete account");
        }
    }

    public BigDecimal getAccountBalance(Long accountId) throws AccountNotFoundException {
        try {
            Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
            return account.getBalance();
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to get account balance");
        }
    }

    public void updateAccountStatus(Long accountId) throws AccountNotFoundException {
        try {
            Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId + ""));
            if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                account.setStatus(StatusInSystem.IRREGULAR);
                Customer customer = accountRepository.findCustomerById(accountId);
                String name = customer.getFirstName() + " " + customer.getLastName();
                String msg = "Dear " + name + ",\n\nYour account is overdrawn. " +
                        "Please take immediate action to resolve this issue.\n\nBest regards,\nYour Bank";

               ///TO DO Notify customer
            }
            accountRepository.save(account);
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to update account status");
        }
    }

    public void suspendAccount(Long accountId) throws AccountNotFoundException {
        try {
            Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
            account.setStatus(StatusInSystem.SUSPENDED);
            accountRepository.save(account);
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to suspend account");
        }
    }

    public void restrictAccount(Long accountId) throws AccountNotFoundException {
        try {
            Account account = accountRepository.findById(accountId).orElseThrow(AccountNotFoundException::new);
            account.setStatus(StatusInSystem.RESTRICTED);
            accountRepository.save(account);
        } catch (AccountNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to restrict account");
        }
    }

    public Account deposit(Long accountId, Double amount) throws Exception {
        try {
            Account account = accountRepository.findAccountById(accountId);
            account.setBalance(account.getBalance().add(new BigDecimal(amount)));
            updateAccount(account);

            // RECORD TRANSACTION
            transactionService.addTransaction(TransactionType.DEPOSIT, amount, account, account);

            // Notify customer
            notifyAccountHolder(account, "Deposit", amount);
            return account;
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to deposit amount");
        }
    }

    public Account withdraw(Long accountId, Double amount) throws Exception {
        try {
            Account account = accountRepository.findAccountById(accountId);
            if (account == null) {
                throw new AccountNotFoundException("Account not found");
            }
            if (account.getBalance().compareTo(new BigDecimal(amount)) < 0) {
                throw new CustomException(Account.class, "Insufficient balance");
            }
            account.setBalance(account.getBalance().subtract(new BigDecimal(amount)));
            updateAccount(account);

            // RECORD TRANSACTION
            transactionService.addTransaction(TransactionType.WITHDRAW, amount, account, account);

            // Notify customer
            notifyAccountHolder(account, "Withdrawal", amount);
            return account;
        } catch (AccountNotFoundException | CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to withdraw amount");
        }
    }

    public void transfer(Long fromAccountId, Long toAccountId, Double amount) throws Exception {
        try {
            Account fromAccount = accountRepository.findAccountById(fromAccountId);
            Account toAccount = accountRepository.findAccountById(toAccountId);

            if (fromAccount == null || toAccount == null) {
                throw new CustomException(Account.class, "One or both accounts not found");
            }

            withdraw(fromAccountId, amount);
            deposit(toAccountId, amount);

            // RECORD TRANSACTION
            transactionService.addTransaction(TransactionType.TRANSFER, amount, fromAccount, toAccount);

            notifyAccountHolder(fromAccount, "Transfer Out", amount);
            notifyAccountHolder(toAccount, "Transfer In", amount);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to transfer amount");
        }
    }

    public void transferToExternalBank(Long fromAccountId, String externalBankAccountId, Double amount) throws Exception {
        try {
            withdraw(fromAccountId, amount);
//            externalBankService.transferToExternalBank(externalBankAccountId, amount); // Adding API HERE
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to transfer to external bank");
        }
    }

    public void notifyAccountHolder(Account account, String transactionType, Double amount) {
        try {
            String message = String.format("A %s of $%.2f has been made to your account. Current balance: $%.2f",
                    transactionType, amount, account.getBalance());
            notifyService.sendEmail(account.getCustomer().getEmail(), transactionType + " Notification", message);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to notify account holder");
        }
    }
}
