package org.solareflare.project.BankSystemMangement.repositories;

import org.solareflare.project.BankSystemMangement.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t ORDER BY t.date DESC")
    public List<Transaction> findRecentTransactions();

    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :accountId OR t.receiver.id = :accountId")
    public List<Transaction> findTransactionsByAccountId(Long accountId);





}
