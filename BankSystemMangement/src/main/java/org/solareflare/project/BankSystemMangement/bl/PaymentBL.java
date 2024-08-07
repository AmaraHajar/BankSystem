package org.solareflare.project.BankSystemMangement.bl;

import com.twilio.twiml.voice.Pay;
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
public class PaymentBL {

    @Autowired
    private PaymentDAO paymentDAO;


    @Autowired
    private LoanDAO loanDAO;



    public Payment addPayment(Payment payment) throws LoanNotFoundException, NotFoundException {
        if(payment.getId() != null){
            try{
                Optional<Loan> existingLoan = this.loanDAO.findById(payment.getId());
                if (existingLoan.isPresent()) {
                    throw new AlreadyExistException(payment);
                }
            }catch (Exception e){
                throw new NotFoundException();
            }
        }
        payment =Payment.builder()
                        .amount(payment.getAmount())
                        .date(Instant.now())
                        .loan(payment.getLoan())
                        .status(ActionStatus.PENDING)
                        .paymentNumber(getPaymentsNumberByLoanId(payment.getLoan().getId())+1)
                        .build();
        return savePayment(payment);

    }

    public Payment getPaymentById(Long id) throws PaymentNotFoundException {
        Optional<Payment> payment = this.paymentDAO.findById(id);
        if (payment.isPresent())
            return payment.get();
        throw new PaymentNotFoundException();
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

    public void deletePayment(Long id) {
        paymentDAO.deleteById(id);
    }

    public void followUpOnUnpaidLoans() {
        List<Loan> unpaidLoans = loanDAO.findByIsPaidFalse();
        unpaidLoans.forEach(loan -> {
            if (loan.getDueDate().isBefore(Instant.now())) {
                NotificationMessage.notifyMessage(loan.getAccount().getCustomer().getEmail().toString(), "Loan Payment Status","Your loan payment is overdue.");
            }
        });
    }

    public Payment requestPayment(Payment payment){
        payment.setStatus(ActionStatus.PENDING);
        payment.setDate(Instant.now());




        return paymentDAO.save(payment);
    }

}