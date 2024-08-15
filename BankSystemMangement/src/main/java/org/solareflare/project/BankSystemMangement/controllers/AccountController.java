package org.solareflare.project.BankSystemMangement.controllers;

import org.solareflare.project.BankSystemMangement.entities.*;
import org.solareflare.project.BankSystemMangement.services.AccountService;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.solareflare.project.BankSystemMangement.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/addAccount")
    public Account addAccount(@RequestBody Account account) throws AccountAlreadyExistException, AccountNotValidException, CustomerNotValidException, CustomerNotRegisteredException {
        account.setCustomer(customerService.getCustomerById(account.getCustomer().getId()));
        return accountService.addAccount(account);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long accountId, @RequestParam Double amount) {
        try {
            accountService.deposit(accountId, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawal(Long accountId, Double amount) throws NotFoundException {
        try {
            accountService.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdraw successful");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) throws NotFoundException {
        return this.accountService.getAccountById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @PutMapping
    public Account updateAccountDetails(Account account) {
        return accountService.updateAccount(account);
    }
}