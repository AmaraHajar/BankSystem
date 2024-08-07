package org.solareflare.project.BankSystemMangement.beans;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@DiscriminatorValue("CUSTOMER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"address", "accounts"})
public class Customer extends User {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    private String phoneNumber;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Account> accounts;

    @Builder(builderMethodName = "customerBuilder")
    public Customer(Long id, String firstName, String lastName, String email, String password, Address address, String phoneNumber) {
        super(firstName, lastName, email, password);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", address=" + (address != null ? address.getId() : null) +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}