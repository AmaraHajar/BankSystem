package org.solareflare.project.BankSystemMangement.bl;


import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.dao.BankDAO;
import org.solareflare.project.BankSystemMangement.dao.CustomerDAO;
import org.solareflare.project.BankSystemMangement.dto.UserRequest;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.Role;
import org.solareflare.project.BankSystemMangement.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerBL {
    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private BankDAO bankDAO;

    @Autowired
    private AddressBL addressBL;

    @Autowired
    private AccountBL accountBL;

    @Autowired
    private UserBL userBL;


    public Customer addCustomer(Customer customer) throws Exception {
        if(customer.getId() == null){
            customer.setRole(Role.ROLE_CUSTOMER);
            customer.setCreatedAt(LocalDateTime.now());
            return this.customerDAO.save(customer);
        }

       throw new Exception();
    }

    public Customer updateCustomer(Customer customer) {
        customer.setUpdatedAt(LocalDateTime.now());
        return customerDAO.save(customer);
    }


    public Customer updateCustomerDetails(UserRequest userRequest) {
        Customer customer = Customer.customerBuilder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .build();
        customer.setUpdatedAt(LocalDateTime.now());
        return customerDAO.save(customer);
    }

//    public boolean isExistingCustomer(Customer customer) throws CustomerNotValidException, CustomerNotRegisteredException {
//        if(customer == null)
//            throw new CustomerNotValidException();
//        if(customer.getId() == null || getCustomerById(customer.getId()) == null)
//            return false;
//        return true;
//    }

    public Customer getCustomerByIdNumber(String idNumber) {
        return customerDAO.findByIdNumber(idNumber);
    }

    public Customer getCustomerByEmail(String email) {
        System.out.println("test email user");
        return customerDAO.findByEmail(email);
    }

    public Customer getCustomerById(Long id) throws CustomerNotRegisteredException {
        Optional<Customer> customer = this.customerDAO.findById(id);
        if (customer.isPresent()) {
            return customer.get();
        }
        throw new CustomerNotRegisteredException();
    }

    public List<Customer> getAllCustomers() {
        System.out.println("test BL all customers");
        return this.customerDAO.findAll();
    }
    public void deleteCustomer(Long id) {
        customerDAO.deleteById(id);
    }


    ///// to check is needed to be here

    public void deposit(Long customerId, Double amount) throws Exception {
        // Customer-specific logic, if any
        accountBL.deposit(customerId, amount);
    }

    public void withdraw(Long customerId, Double amount) throws Exception {
        // Customer-specific logic, if any
        accountBL.withdraw(customerId, amount);
    }

    public void transfer(Long customerId, Long fromAccountId, Long toAccountId, Double amount) throws Exception {
        // Customer-specific validation
        accountBL.transfer(fromAccountId, toAccountId, amount);
    }
}
