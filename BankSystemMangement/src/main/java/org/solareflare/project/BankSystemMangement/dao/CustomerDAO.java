package org.solareflare.project.BankSystemMangement.dao;

import org.solareflare.project.BankSystemMangement.beans.Bank;
import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDAO extends JpaRepository<Customer, Long> {

    public Customer findByIdNumber(String idNumber);

    public Customer findByEmail(String email);

//    @Query("SELECT a FROM users a  a.role = 'CUSTOMER'")
//    public List<Customer> findCustomers();


}
