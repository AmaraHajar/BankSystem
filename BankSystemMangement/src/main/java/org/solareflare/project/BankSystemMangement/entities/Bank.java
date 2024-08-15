package org.solareflare.project.BankSystemMangement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "bank")
public class Bank implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankId;

    @org.springframework.beans.factory.annotation.Value("${bank.name}")
    @Column(name = "name")
    private String name;

    private BigDecimal balance;

//    @Value("${bank.address}")
//    @Column(name = "address")
//    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER)
    private List<ForeignCurrencyExchange> exchanges;


    @ElementCollection
    @CollectionTable(name = "bank_balance", joinColumns = @JoinColumn(name = "bank_id"))
    @MapKeyColumn(name = "currency_name")
    @Column(name = "balance")
    private Map<String, Double> balances;

    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER)
    private List<Employee> employees;


    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER)
    private List<Account> accounts;


    @OneToMany(mappedBy = "bank")
    private List<Customer> customers;

}
