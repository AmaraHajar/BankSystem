package org.solareflare.project.BankSystemMangement.bl;

import org.solareflare.project.BankSystemMangement.beans.Account;
import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.beans.Loan;
import org.solareflare.project.BankSystemMangement.beans.Payment;
import org.solareflare.project.BankSystemMangement.dao.AccountDAO;
import org.solareflare.project.BankSystemMangement.dao.LoanDAO;
import org.solareflare.project.BankSystemMangement.dao.PaymentDAO;
import org.solareflare.project.BankSystemMangement.exceptions.AlreadyExistException;
import org.solareflare.project.BankSystemMangement.exceptions.LoanNotFoundException;
import org.solareflare.project.BankSystemMangement.exceptions.NotFoundException;
import org.solareflare.project.BankSystemMangement.exceptions.PaymentNotFoundException;
import org.solareflare.project.BankSystemMangement.utils.ActionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LoanBL {

    @Autowired
    private LoanDAO loanDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private PaymentBL paymentBL;

    @Autowired
    private NotificationService notificationService;


    public Loan addLoan(Loan loan) throws AlreadyExistException, NotFoundException {

        if(loan.getId() != null){
            try{
                Optional<Loan> existingLoan = loanDAO.findById(loan.getId());
                if (existingLoan.isPresent()) {
                    throw new AlreadyExistException(loan);// to change
                }
            }catch (Exception e){
                throw new NotFoundException();
            }
        }
        return this.saveLoan(loan);
    }
    public  Loan updateLoanPayment(Payment payment) throws LoanNotFoundException, NotFoundException, PaymentNotFoundException {

        paymentBL.addPayment(payment);
        payment.setStatus(ActionStatus.APPROVED);
        payment = paymentBL.savePayment(payment);
        Loan loan = payment.getLoan();

        loan.setAmount(loan.getAmount()-payment.getAmount());
        loan.setPaymentsNumber((int) (loan.getAmount()/loan.getMonthlyRepayment()));
        loan.setPaidPayments(paymentBL.getPaymentsByLoan(loan).size());
        loan.setPaid(true);
        return saveLoan(loan);
    }

    public Loan updateLoan(Loan loan) throws LoanNotFoundException {
        Optional<Loan> existingLoan = this.loanDAO.findById(loan.getId());
        if (existingLoan.isEmpty()) {
            throw new LoanNotFoundException();
        }
        return this.saveLoan(loan);
    }

    public Loan saveLoan(Loan loan) {
        return loanDAO.save(loan);
    }

    public Loan getLoanById(Long id) throws LoanNotFoundException {
        Optional<Loan> loan = this.loanDAO.findById(id);
        if (loan.isPresent()) {
            return loan.get();
        }
        throw new LoanNotFoundException();
    }

    public List<Loan> getAllLoans() {
        return loanDAO.findAll();
    }

    public void deleteLoan(Long id) {
        loanDAO.deleteById(id);
    }

    public Loan grantLoan(Long accountId, Double amount, Instant startDate, Instant dueDate) throws AlreadyExistException, NotFoundException {
        Loan loan =
                Loan.builder()
                        .account(accountDAO.findAccountById(accountId))
                        .amount(amount)
                        .startDate(startDate)
                        .dueDate(dueDate).build();
        loan = addLoan(loan);
        return loan;
    }

    public void calculateMonthlyRepayment() throws LoanNotFoundException, NotFoundException, PaymentNotFoundException {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        List<Payment> payments = paymentBL.getPaymentsByMonth(currentMonth, currentYear);

       for(Payment payment : payments){
           if(payment.getLoan().getMonthlyRepayment().compareTo(payment.getAmount() ) < 0){
               Customer customer = payment.getAccount().getCustomer();
               String name = customer.getFirstName()+" "+ customer.getLastName();
               String msg = "Dear " +name+",\n\nYour account is overdrawn. " +
                       "Please take immediate action to resolve this issue.\n\nBest regards,\nYour Bank";

               notificationService.sendEmail(customer.getEmail(), "Overdraft Alert",msg);
           }
       }
    }
}
