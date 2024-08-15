package org.solareflare.project.BankSystemMangement.repositories;

import org.solareflare.project.BankSystemMangement.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public List<Payment> findPaymentByLoanId(Long loanId);
    @Query(value = "SELECT * FROM Payment WHERE MONTH(payment_date) = :month AND YEAR(payment_date) = :year", nativeQuery = true)
    public List<Payment> findPaymentsByMonth(@Param("month") int month, @Param("year") int year);

    public int countPaymentByLoanId(Long id);
}