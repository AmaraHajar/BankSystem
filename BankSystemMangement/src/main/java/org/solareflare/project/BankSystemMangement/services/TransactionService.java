package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.entities.Account;
import org.solareflare.project.BankSystemMangement.entities.Transaction;
import org.solareflare.project.BankSystemMangement.repositories.AccountRepository;
import org.solareflare.project.BankSystemMangement.dto.TransferRequest;
import org.solareflare.project.BankSystemMangement.exceptions.InsufficientFundsException;
import org.solareflare.project.BankSystemMangement.exceptions.InvalidAccountException;
import org.solareflare.project.BankSystemMangement.exceptions.TransactionNotFoundException;
import org.solareflare.project.BankSystemMangement.enums.ActionStatus;
import org.solareflare.project.BankSystemMangement.enums.TransactionType;
import org.solareflare.project.BankSystemMangement.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Transaction addTransaction(TransactionType transactionType, Double amount, Account fromAccount, Account toAccount) throws Exception {
        try {
            Transaction transaction = Transaction.builder()
                    .amount(amount)
                    .date(Instant.now())
                    .sender(fromAccount)
                    .receiver(toAccount)
                    .transactionType(transactionType)
                    .status(ActionStatus.APPROVED)
                    .build();
            return saveTransaction(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add transaction", e);
        }
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) throws TransactionNotFoundException {
        Optional<Transaction> transaction = this.transactionRepository.findById(id);
        if (transaction.isPresent()) {
            return transaction.get();
        }
        throw new TransactionNotFoundException("");
    }




    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findTransactionsByAccountId(accountId);
    }

    public Transaction requestTransaction(Transaction transaction){
        transaction.setStatus(ActionStatus.PENDING);
        transaction.setDate(Instant.now());
        return this.saveTransaction(transaction);
    }


//    public void transferFunds(TransferRequest transferRequest) throws Exception {
//        Account senderAccount = accountDAO.findById(transferRequest.getSenderAccountId())
//                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));
//        Account recipientAccount = accountDAO.findById(transferRequest.getRecipientAccountId())
//                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found"));
//
//        if( senderAccount.equals(recipientAccount)){
//            throw new Exception("you have to type a correct account, not ypur own one!");
//        }
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

    public void transferFunds(TransferRequest transferRequest) throws AccountNotFoundException, InsufficientFundsException, InvalidAccountException {
        Account senderAccount = accountRepository.findById(transferRequest.getSenderAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found for ID: " + transferRequest.getSenderAccountId()));
        Account recipientAccount = accountRepository.findById(transferRequest.getRecipientAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Recipient account not found for ID: " + transferRequest.getRecipientAccountId()));

        if (senderAccount.equals(recipientAccount)) {
            throw new InvalidAccountException("Cannot transfer funds to the same account");
        }

        if (senderAccount.getBalance().compareTo(new BigDecimal(transferRequest.getAmount())) < 0) {
            throw new InsufficientFundsException("Insufficient funds in sender's account");
        }

        // Deduct the amount from the sender's account
        senderAccount.setBalance(senderAccount.getBalance().subtract(new BigDecimal(transferRequest.getAmount())));

        // Add the amount to the recipient's account
        recipientAccount.setBalance(recipientAccount.getBalance().add(new BigDecimal(transferRequest.getAmount())));

        // Save the updated accounts
        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        // Optionally, create a transaction record here
    }


}
