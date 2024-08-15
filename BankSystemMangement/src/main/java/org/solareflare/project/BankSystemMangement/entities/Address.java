package org.solareflare.project.BankSystemMangement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "addresses")
@Builder
@Data
@ToString(exclude = {"bank", "customer"})
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @OneToOne(mappedBy = "address")
    private Bank bank;

    @OneToOne(mappedBy = "address")
    private Customer customer;

}

