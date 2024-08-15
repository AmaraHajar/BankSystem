package org.solareflare.project.BankSystemMangement.repositories;

import org.solareflare.project.BankSystemMangement.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findByIdNumber(String idNumber);

    public Customer findByEmail(String email);

}
