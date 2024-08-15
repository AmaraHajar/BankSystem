package org.solareflare.project.BankSystemMangement.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.solareflare.project.BankSystemMangement.enums.ActionStatus;
import org.solareflare.project.BankSystemMangement.enums.TransactionType;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "transactions")
@Builder
@Data
@ToString
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @JsonIgnoreProperties({"loans", "payments", "transactions"})
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIgnoreProperties({"loans", "payments", "transactions"})
    private Account receiver;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "date")
    private Instant date;

    @Enumerated(EnumType.STRING)
    private ActionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
}
