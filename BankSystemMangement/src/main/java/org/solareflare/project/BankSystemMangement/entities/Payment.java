package org.solareflare.project.BankSystemMangement.entities;


import jakarta.persistence.*;
import lombok.*;
import org.solareflare.project.BankSystemMangement.enums.ActionStatus;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Builder
@Data
@ToString(exclude = {"loan"})
public class Payment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer paymentNumber;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private Instant date;

    @Enumerated(EnumType.STRING)
    private ActionStatus status;

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", paymentNumber=" + paymentNumber +
                ", amount=" + amount +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
