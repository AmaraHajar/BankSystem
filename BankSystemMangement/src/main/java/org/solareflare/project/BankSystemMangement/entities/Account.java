package org.solareflare.project.BankSystemMangement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.solareflare.project.BankSystemMangement.enums.StatusInSystem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Builder
@Data
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @Column(name = "balance")
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private StatusInSystem status;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Loan> loans;

    @OneToMany(mappedBy = "account",  fetch = FetchType.EAGER)
    private List<Payment> payments;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    private List<Transaction> sentTransactions;// transfer funds to others

    @OneToMany(mappedBy = "receiver", fetch = FetchType.EAGER)
    private List<Transaction> receivedTransactions;

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customer=" + customer +
                ", bank=" + bank +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
