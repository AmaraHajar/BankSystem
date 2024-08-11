package org.solareflare.project.BankSystemMangement.services;

import org.solareflare.project.BankSystemMangement.beans.Account;
import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.dao.BankDAO;
import org.solareflare.project.BankSystemMangement.dao.CustomerDAO;
import org.solareflare.project.BankSystemMangement.dto.UserRequest;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private BankDAO bankDAO;

    @Autowired
    private AccountService accountBL;

    public Customer addCustomer(Customer customer) throws InvalidCustomerOperationException {
        try {
            if (customer.getId() == null) {
                customer.setRole(Role.ROLE_CUSTOMER);
                customer.setCreatedAt(LocalDateTime.now());
                return this.customerDAO.save(customer);
            } else {
                throw new InvalidCustomerOperationException("Customer ID already exists, cannot add customer.");
            }
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to add customer");
        }
    }

    public Customer updateCustomer(Customer customer) throws InvalidCustomerOperationException {
        try {
            if (customer.getId() != null) {
                customer.setUpdatedAt(LocalDateTime.now());
                return customerDAO.save(customer);
            } else {
                throw new InvalidCustomerOperationException("Customer ID is null, cannot update customer.");
            }
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to update customer");
        }
    }

    public Customer updateCustomerDetails(UserRequest userRequest) throws CustomException {
        try {
            Customer customer = Customer.customerBuilder()
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .password(userRequest.getPassword())
                    .build();
            customer.setUpdatedAt(LocalDateTime.now());
            return customerDAO.save(customer);
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to update customer details");
        }
    }

    public Customer getCustomerByIdNumber(String idNumber) throws CustomerNotRegisteredException {
        try {
            Customer customer = customerDAO.findByIdNumber(idNumber);
            if (customer == null) {
                throw new CustomerNotRegisteredException();
            }
            return customer;
        } catch (CustomerNotRegisteredException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to retrieve customer by ID number");
        }
    }

    public Customer getCustomerByEmail(String email) throws CustomException {
        try {
            return customerDAO.findByEmail(email);
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to retrieve customer by email");
        }
    }

    public Customer getCustomerById(Long id) throws CustomerNotRegisteredException {
        try {
            Optional<Customer> customer = this.customerDAO.findById(id);
            if (customer.isPresent()) {
                return customer.get();
            } else {
                throw new CustomerNotRegisteredException();
            }
        } catch (CustomerNotRegisteredException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to retrieve customer by ID");
        }
    }

    public List<Customer> getAllCustomers() throws CustomException {
        try {
            return this.customerDAO.findAll();
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to retrieve all customers");
        }
    }

    public void deleteCustomer(Long id) throws CustomException {
        try {
            customerDAO.deleteById(id);
        } catch (Exception e) {
            throw new CustomException(Customer.class, "Failed to delete customer");
        }
    }

    public void deposit(Long customerId, Double amount) throws CustomException {
        try {
            accountBL.deposit(customerId, amount);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to deposit money");
        }
    }

    public void withdraw(Long customerId, Double amount) throws CustomException {
        try {
            accountBL.withdraw(customerId, amount);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to withdraw money");
        }
    }

    public void transfer(Long customerId, Long fromAccountId, Long toAccountId, Double amount) throws CustomException {
        try {
            accountBL.transfer(fromAccountId, toAccountId, amount);
        } catch (Exception e) {
            throw new CustomException(Account.class, "Failed to transfer money");
        }
    }
}
