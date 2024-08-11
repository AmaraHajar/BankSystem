package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.beans.Loan;
import org.solareflare.project.BankSystemMangement.beans.Payment;
import org.solareflare.project.BankSystemMangement.dao.LoanDAO;
import org.solareflare.project.BankSystemMangement.dao.PaymentDAO;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.solareflare.project.BankSystemMangement.utils.NotificationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private LoanDAO loanDAO;


    public Payment addPayment(Payment payment) throws AlreadyExistException, NotFoundException {
        try {
            if (payment.getId() != null) {
                Optional<Loan> existingLoan = this.loanDAO.findById(payment.getLoan().getId());
                if (existingLoan.isPresent()) {
                    throw new AlreadyExistException("Payment with this ID already exists.");
                }
            }
            payment = Payment.builder()
                    .amount(payment.getAmount())
                    .date(Instant.now())
                    .loan(payment.getLoan())
                    .status(ActionStatus.PENDING)
                    .paymentNumber(getPaymentsNumberByLoanId(payment.getLoan().getId()) + 1)
                    .build();
            return savePayment(payment);
        } catch (Exception e) {
            throw new NotFoundException("Loan not found for Payment ID: " + payment.getLoan().getId(), e);
        }
    }

    public Payment getPaymentById(Long id) throws PaymentNotFoundException {
        try{
            Optional<Payment> payment = this.paymentDAO.findById(id);
                return payment.get();
        }catch (Exception e){
            throw new PaymentNotFoundException("Payment not found for ID: " + id,e);
        }
    }


    public List<Payment> getPaymentsByLoan(Loan loan) throws PaymentNotFoundException {
        return this.paymentDAO.findPaymentByLoanId(loan.getId());
    }

    public List<Payment> getPaymentsByMonth(int month, int year){
        return paymentDAO.findPaymentsByMonth(month,year);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.findAll();
    }

    public Payment savePayment(Payment payment) {
        return paymentDAO.save(payment);
    }

    public Integer getPaymentsNumberByLoanId(Long loanId){
        return paymentDAO.findPaymentByLoanId(loanId).size();
    }

    public List<Payment> getPaymentsByLoanId(Long loanId) throws PaymentNotFoundException {
        try {
            return this.paymentDAO.findPaymentByLoanId(loanId);
        } catch (Exception e) {
            throw new PaymentNotFoundException("Payments not found for Loan ID: " + loanId, e);
        }
    }


    public void deletePayment(Long id) {
        paymentDAO.deleteById(id);
    }

    public void followUpOnUnpaidLoans() {
        try {
            List<Loan> unpaidLoans = loanDAO.findByIsPaidFalse();
            unpaidLoans.forEach(loan -> {
                if (loan.getDueDate().isBefore(Instant.now())) {
                    NotificationMessage.notifyMessage(
                            loan.getAccount().getCustomer().getEmail(),
                            "Loan Payment Status",
                            "Your loan payment is overdue."
                    );
                }
            });
        } catch (Exception e) {
            // Handle and log any unexpected exception that might occur during follow-up
            throw new RuntimeException("An error occurred during follow-up on unpaid loans.", e);
        }
    }

    public Payment requestPayment(Payment payment) {
        try {
            payment.setStatus(ActionStatus.PENDING);
            payment.setDate(Instant.now());
            return paymentDAO.save(payment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to request payment for Payment ID: " + payment.getId(), e);
        }
    }


}