package org.solareflare.project.BankSystemMangement.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.io.Serializable;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
