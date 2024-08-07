package org.solareflare.project.BankSystemMangement.dao;

import org.solareflare.project.BankSystemMangement.beans.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankDAO extends JpaRepository<Bank, Long> {

    public Bank findByName(String name);

//    // Find banks with more than a certain number of employees
//    List<Bank> findByEmployeesCountGreaterThan(int count); // there is no variable count in teh class

    // Custom query to find all banks with a specific employee
    @Query("SELECT b FROM Bank b JOIN b.employees e WHERE e.id = :employeeId")
    public List<Bank> findBanksByEmployeeId(Long employeeId);

    // Custom query to find all banks with a specific foreign currency exchange
    @Query("SELECT b FROM Bank b JOIN b.exchanges fce WHERE fce.id = :exchangeId")
    public List<Bank> findBanksByExchangeId(Long exchangeId);

    // Custom query to find all banks with a specific account
    @Query("SELECT b FROM Bank b JOIN b.accounts a WHERE a.id = :accountId")
    public List<Bank> findBanksByAccountId(Long accountId);

}
