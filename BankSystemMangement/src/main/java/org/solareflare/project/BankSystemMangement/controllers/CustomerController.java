package org.solareflare.project.BankSystemMangement.controllers;


import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.bl.AuthenticationService;
import org.solareflare.project.BankSystemMangement.bl.CustomerBL;
import org.solareflare.project.BankSystemMangement.dto.JwtAuthenticationResponse;
import org.solareflare.project.BankSystemMangement.dto.SignUpRequest;
import org.solareflare.project.BankSystemMangement.dto.UserRequest;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerBL customerBL;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/add/Customer")
    public ResponseEntity<JwtAuthenticationResponse> addCustomer(@RequestBody SignUpRequest user) {
        try {
            JwtAuthenticationResponse response = authenticationService.signup(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try {
            List<Customer> customers = customerBL.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerBL.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotRegisteredException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/idNumber/{idNumber}")
    public ResponseEntity<Customer> getCustomerByIdNumber(@PathVariable String idNumber) {
        try {
            Customer customer = customerBL.getCustomerByIdNumber(idNumber);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/email")
    public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email) {
        try {
            Customer customer = customerBL.getCustomerByEmail(email);
            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerBL.deleteCustomer(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Customer> updateCustomerDetails(@RequestBody UserRequest user) {
        try {
            Customer customer = customerBL.updateCustomerDetails(user);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
