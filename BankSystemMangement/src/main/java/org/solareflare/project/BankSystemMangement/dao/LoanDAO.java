package org.solareflare.project.BankSystemMangement.dao;

import org.solareflare.project.BankSystemMangement.beans.Loan;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanDAO extends JpaRepository<Loan, Long> {

    // Find loans by account ID
    List<Loan> findByAccountId(Long accountId);

    // Find loans by status
    List<Loan> findByStatus(ActionStatus status);

    // Find loans by isPaid
    List<Loan> findByIsPaid(boolean isPaid);

    // Find by is paid false
    public List<Loan>  findByIsPaidFalse();

    // Find loans by account ID and status
    List<Loan> findByAccountIdAndStatus(Long accountId, ActionStatus status);

    // Find loans by account ID and isPaid
    List<Loan> findByAccountIdAndIsPaid(Long accountId, boolean isPaid);

    // Find loans by customer ID
    @Query("SELECT l FROM Loan l WHERE l.account.customer.id = :customerId")
    List<Loan> findByCustomerId(Long customerId);
}
