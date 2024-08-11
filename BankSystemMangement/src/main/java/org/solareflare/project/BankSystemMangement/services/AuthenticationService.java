package org.solareflare.project.BankSystemMangement.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.solareflare.project.BankSystemMangement.beans.Customer;
import org.solareflare.project.BankSystemMangement.dao.BankDAO;
import org.solareflare.project.BankSystemMangement.dao.CustomerDAO;
import org.solareflare.project.BankSystemMangement.dto.JwtAuthenticationResponse;
import org.solareflare.project.BankSystemMangement.dto.SignInRequest;
import org.solareflare.project.BankSystemMangement.dto.SignUpRequest;
import org.solareflare.project.BankSystemMangement.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService implements Serializable {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private BankDAO bankDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;



    public JwtAuthenticationResponse signup(SignUpRequest request) {

        Customer customer = Customer.customerBuilder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        customer.setRole(Role.ROLE_CUSTOMER);
        customer.setBank(bankDAO.findById(1L).get());
        customer.setCreatedAt(LocalDateTime.now());
        customer = customerDAO.save(customer);
        var jwt = jwtService.generateToken(customer);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
//        log.info("Signin request received for email: {}", request.getEmail());
        System.out.println("Test num 1 => User retrieved: " + request.getEmail() + " password is: " + request.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var user = this.customerDAO.findByEmail(request.getEmail());
            System.out.println("Test num 2 => User : " + user.getEmail() + " password is: " + user.getPassword());
//            log.debug("User retrieved: {}", user);
            var jwt = jwtService.generateToken(user);
            System.out.println("JWT generated for email: " + request.getEmail());
            return JwtAuthenticationResponse.builder().token(jwt).build();
        } catch (BadCredentialsException e) {
//            log.error("Invalid credentials provided for email: {}", request.getEmail());
            throw new IllegalArgumentException("Invalid email or password.");
        }


    }

}
