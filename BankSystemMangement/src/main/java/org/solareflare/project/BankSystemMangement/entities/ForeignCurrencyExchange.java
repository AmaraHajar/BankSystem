package org.solareflare.project.BankSystemMangement.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.io.Serializable;


@Entity
@Data
@Builder
@Table(name = "foreign_exchanges")
public class ForeignCurrencyExchange implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "name", unique = true)
    private String name;

    @NonNull
    @Column(name = "rate")
    private Double rate;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;


    /**
     * toString method to display the data for the class
     *
     * @return a string
     */
    @Override
    public String toString() {
        return "ForeignCurrencyExchange{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                ", bank=" + bank +
                '}';
    }
}
