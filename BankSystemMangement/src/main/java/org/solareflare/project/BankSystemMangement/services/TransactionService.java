package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.beans.Account;
import org.solareflare.project.BankSystemMangement.beans.Transaction;
import org.solareflare.project.BankSystemMangement.dao.AccountDAO;
import org.solareflare.project.BankSystemMangement.dao.TransactionDAO;
import org.solareflare.project.BankSystemMangement.dto.TransferRequest;
import org.solareflare.project.BankSystemMangement.exceptions.InsufficientFundsException;
import org.solareflare.project.BankSystemMangement.exceptions.InvalidAccountException;
import org.solareflare.project.BankSystemMangement.exceptions.TransactionNotFoundException;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.solareflare.project.BankSystemMangement.utils.TransactionType;
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
    private TransactionDAO transactionDao;

    @Autowired
    private AccountDAO accountDAO;

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
        return transactionDao.findAll();
    }

    public Transaction getTransactionById(Long id) throws TransactionNotFoundException {
        Optional<Transaction> transaction = this.transactionDao.findById(id);
        if (transaction.isPresent()) {
            return transaction.get();
        }
        throw new TransactionNotFoundException("");
    }




    public Transaction saveTransaction(Transaction transaction) {
        return transactionDao.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionDao.deleteById(id);
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionDao.findTransactionsByAccountId(accountId);
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
        Account senderAccount = accountDAO.findById(transferRequest.getSenderAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found for ID: " + transferRequest.getSenderAccountId()));
        Account recipientAccount = accountDAO.findById(transferRequest.getRecipientAccountId())
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
        accountDAO.save(senderAccount);
        accountDAO.save(recipientAccount);

        // Optionally, create a transaction record here
    }


}
