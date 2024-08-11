package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.beans.Loan;
import org.solareflare.project.BankSystemMangement.beans.Payment;
import org.solareflare.project.BankSystemMangement.dao.AccountDAO;
import org.solareflare.project.BankSystemMangement.dao.LoanDAO;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanDAO loanDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private NotificationService notificationService;

    public Loan addLoan(Loan loan) throws AlreadyExistException, NotFoundException {
        try {
            if (loan.getId() != null) {
                Optional<Loan> existingLoan = loanDAO.findById(loan.getId());
                if (existingLoan.isPresent()) {
                    throw new AlreadyExistException("Loan already exists with ID: " + loan.getId());
                }
            }
            return this.saveLoan(loan);
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to add loan: " + e.getMessage());
        }
    }

    public Loan updateLoanPayment(Payment payment) throws LoanNotFoundException, NotFoundException, PaymentNotFoundException {
        try {
            paymentService.addPayment(payment);
            payment.setStatus(ActionStatus.APPROVED);
            payment = paymentService.savePayment(payment);

            Loan loan = payment.getLoan();
            if (loan == null) {
                throw new LoanNotFoundException("Loan not found for payment: " + payment.getId());
            }

            loan.setAmount(loan.getAmount() - payment.getAmount());
            loan.setPaymentsNumber((int) (loan.getAmount() / loan.getMonthlyRepayment()));
            loan.setPaidPayments(paymentService.getPaymentsByLoan(loan).size());
            loan.setPaid(loan.getAmount() <= 0);

            return saveLoan(loan);
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to update loan payment: " + e.getMessage());
        }
    }

    public Loan updateLoan(Loan loan) throws LoanNotFoundException {
        try {
            Optional<Loan> existingLoan = this.loanDAO.findById(loan.getId());
            if (existingLoan.isEmpty()) {
                throw new LoanNotFoundException("Loan not found with ID: " + loan.getId());
            }
            return this.saveLoan(loan);
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to update loan: " + e.getMessage());
        }
    }

    public Loan saveLoan(Loan loan) throws CustomException {
        try {
            return loanDAO.save(loan);
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to save loan: " + e.getMessage());
        }
    }

    public Loan getLoanById(Long id) throws LoanNotFoundException {
        return loanDAO.findById(id)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with ID: " + id));
    }

    public List<Loan> getAllLoans() throws CustomException {
        try {
            return loanDAO.findAll();
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to retrieve all loans: " + e.getMessage());
        }
    }

    public void deleteLoan(Long id) throws LoanNotFoundException, CustomException {
        try {
            if (!loanDAO.existsById(id)) {
                throw new LoanNotFoundException("Loan not found with ID: " + id);
            }
            loanDAO.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to delete loan: " + e.getMessage());
        }
    }

    public Loan grantLoan(Long accountId, Double amount, Instant startDate, Instant dueDate) throws AlreadyExistException, NotFoundException {
        try {
            Loan loan = Loan.builder()
                    .account(accountDAO.findAccountById(accountId))
                    .amount(amount)
                    .startDate(startDate)
                    .dueDate(dueDate)
                    .build();
            return addLoan(loan);
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to grant loan: " + e.getMessage());
        }
    }

    public void calculateMonthlyRepayment() throws LoanNotFoundException, NotFoundException, PaymentNotFoundException, CustomException {
        try {
            LocalDate currentDate = LocalDate.now();
            int currentMonth = currentDate.getMonthValue();
            int currentYear = currentDate.getYear();
            List<Payment> payments = paymentService.getPaymentsByMonth(currentMonth, currentYear);

            for (Payment payment : payments) {
                if (payment.getLoan().getMonthlyRepayment().compareTo(payment.getAmount()) < 0) {
                    Customer customer = payment.getAccount().getCustomer();
                    String name = customer.getFirstName() + " " + customer.getLastName();
                    String msg = "Dear " + name + ",\n\nYour account is overdrawn. " +
                            "Please take immediate action to resolve this issue.\n\nBest regards,\nYour Bank";

                    notificationService.sendEmail(customer.getEmail(), "Overdraft Alert", msg);
                }
            }
        } catch (Exception e) {
            throw new CustomException(Loan.class, "Failed to calculate monthly repayment: " + e.getMessage());
        }
    }
}
