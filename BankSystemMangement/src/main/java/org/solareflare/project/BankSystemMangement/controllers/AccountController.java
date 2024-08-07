package org.solareflare.project.BankSystemMangement.controllers;

import org.solareflare.project.BankSystemMangement.beans.*;
import org.solareflare.project.BankSystemMangement.bl.AccountBL;
import org.solareflare.project.BankSystemMangement.bl.CustomerBL;
import org.solareflare.project.BankSystemMangement.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountBL accountBL;
    @Autowired
    private CustomerBL customerBL;
    @PostMapping("/addAccount")
    public Account addAccount(@RequestBody Account account) throws AccountAlreadyExistException, AccountNotValidException, CustomerNotValidException, CustomerNotRegisteredException {
        account.setCustomer(customerBL.getCustomerById(account.getCustomer().getId()));
        return accountBL.addAccount(account);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam Long accountId, @RequestParam Double amount) {
        try {
            accountBL.deposit(accountId, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String>  withdrawal(Long accountId, Double amount) throws NotFoundException {
        try {
             accountBL.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdraw successful");

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountBL.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) throws NotFoundException {
        return this.accountBL.getAccountById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountBL.deleteAccount(id);
    }

    @PutMapping
    public Account updateAccountDetails(Account account) {
        return accountBL.updateAccount(account);
    }

//    @PostMapping("/loan")
//    public Loan requestLoan(Long accountId, Integer amount, Instant dueDate, Double monthlyRepayment) throws NotFoundException {
//        Loan loan = new Loan();
//        loan.setAccount(accountBL.getAccountById(accountId));
//        loan.setAmount(amount);
//        loan.setDueDate(dueDate);
//        loan.setMonthlyRepayment(monthlyRepayment);
//        return accountBL.requestLoan(loan);
//    }



//    @PostMapping("/payment")
//    public Payment requestPayment(Long accountId, Integer amount, Long loanId) throws LoanNotFoundException, NotFoundException {
//        Payment payment = new Payment();
//        payment.setAccount(accountBL.getAccountById(accountId));
//        payment.setAmount(amount);
//        payment.setLoan(accountBL.getLoanById(loanId));
//        return accountBL.requestPayment(payment);
//    }
//    @PostMapping("/transaction")
//    public Transaction requestTransaction(Long senderId, Long receiverId,Integer amount) throws NotFoundException {
//        Transaction transaction = new Transaction();
//        transaction.setSender(accountBL.getAccountById(senderId));
//        transaction.setReceiver(accountBL.getAccountById(receiverId));
//        transaction.setAmount(amount);
//        return accountBL.requestTransaction(transaction);
//    }



}


//@RestController
//@RequestMapping("/accounts")
//public class AccountController {
//
//    @Autowired
//    private AccountService accountService;
//
//    @PostMapping("/deposit")
//    public ResponseEntity<String> deposit(@RequestParam Long accountId, @RequestParam Double amount) {
//        accountService.deposit(accountId, amount);
//        return ResponseEntity.ok("Deposit successful");
//    }
//
//    @PostMapping("/withdraw")
//    public ResponseEntity<String> withdraw(@RequestParam Long accountId, @RequestParam Double amount) {
//        try {
//            accountService.withdraw(accountId, amount);
//            return ResponseEntity.ok("Withdrawal successful");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/transfer")
//    public ResponseEntity<String> transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam Double amount) {
//        try {
//            accountService.transfer(fromAccountId, toAccountId, amount);
//            return ResponseEntity.ok("Transfer successful");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//
//    @PostMapping("/transfer/external")
//    public ResponseEntity<String> transferToExternal(@RequestParam Long fromAccountId, @RequestParam String externalBankAccountId, @RequestParam Double amount) {
//        try {
//            accountService.transferToExternalBank(fromAccountId, externalBankAccountId, amount);
//            return ResponseEntity.ok("External transfer successful");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }
//}
