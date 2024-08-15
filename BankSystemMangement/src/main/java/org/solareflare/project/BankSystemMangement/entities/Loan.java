package org.solareflare.project.BankSystemMangement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.solareflare.project.BankSystemMangement.enums.ActionStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;


@Entity
@Table(name = "loans")
@Builder
@Data
@ToString(exclude = {"account", "payments"})
public class Loan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "due_date")
    private Instant dueDate;

    private Instant paidDate;

    @Column(name = "monthly_repayment")
    private Double monthlyRepayment;

    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private List<Payment> payments;

    @Column(name = "is_paid")
    private boolean isPaid;

    private Integer paymentsNumber;
    private Integer paidPayments;

    @Enumerated(EnumType.STRING)
    private ActionStatus status;

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", startDate=" + startDate +
                ", dueDate=" + dueDate +
                ", monthlyRepayment=" + monthlyRepayment +
                ", isPaid=" + isPaid +
                ", paymentsNumber=" + paymentsNumber +
                ", paidPayments=" + paidPayments +
                ", status=" + status +
                '}';
    }
}

