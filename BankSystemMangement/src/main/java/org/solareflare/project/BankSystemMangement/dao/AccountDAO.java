package org.solareflare.project.BankSystemMangement.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.solareflare.project.BankSystemMangement.beans.Account;
import org.solareflare.project.BankSystemMangement.beans.Bank;
import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.utils.StatusInSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AccountDAO extends JpaRepository<Account, Long> {

    // Find all accounts by a given bank
    public List<Account> findAccountsByBank(Bank bank);

    // Find accounts by status
    public List<Account> findAccountsByStatus(StatusInSystem status);

    // Find accounts with balance greater than a specific amount
    public List<Account> findAccountsByBalanceGreaterThan(BigDecimal amount);

    // Find accounts with balance less than a specific amount
    public List<Account> findAccountsByBalanceLessThan(BigDecimal amount);

    // Find accounts by customer and status
    public List<Account> findAccountsByCustomerAndStatus(Customer customer, StatusInSystem status);

    // Find accounts by bank and status
    public List<Account> findAccountsByBankAndStatus(Bank bank, StatusInSystem status);

    // Custom query to find accounts by customer's last name
    @Query("SELECT a FROM Account a WHERE a.customer.lastName = :lastName")
    public List<Account> findAccountsByCustomerLastName(@Param("lastName") String lastName);

    // Custom query to find accounts by balance range
    @Query("SELECT a FROM Account a WHERE a.balance BETWEEN :minBalance AND :maxBalance")
    public List<Account> findAccountsByBalanceRange(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);

    @Query("SELECT DISTINCT a FROM Account a JOIN a.loans l WHERE a.status = :status")
    public List<Account> findAccountsByStatusWithLoans(@Param("status") StatusInSystem status);

//    @Query("SELECT DISTINCT a FROM Account a JOIN a.transactions t WHERE a.status = :status")
//    List<Account> findAccountsByStatusWithTransactions(@Param("status") StatusInSystem status);

    // Count accounts by customer
    public long countByCustomer(Customer customer);

    // Count accounts by bank
    public long countByBank(Bank bank);

    // Count active accounts
    public long countByStatus(StatusInSystem status);

    // Delete accounts by customer
    public void deleteByCustomer(Customer customer);

    // Delete accounts by bank
    public void deleteByBank(Bank bank);

    public Account findAccountById(Long accountId);

    public Customer findCustomerById(Long id);





}
