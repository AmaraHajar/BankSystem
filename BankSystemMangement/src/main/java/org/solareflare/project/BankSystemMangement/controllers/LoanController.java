package org.solareflare.project.BankSystemMangement.controllers;

import org.solareflare.project.BankSystemMangement.entities.Loan;
import org.solareflare.project.BankSystemMangement.exceptions.AlreadyExistException;
import org.solareflare.project.BankSystemMangement.exceptions.LoanNotFoundException;
import org.solareflare.project.BankSystemMangement.exceptions.NotFoundException;
import org.solareflare.project.BankSystemMangement.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;


    @GetMapping("get/all")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/get/{id}")
    public Loan getLoanById(@PathVariable Long id) throws LoanNotFoundException {
            return this.loanService.getLoanById(id);
    }

    @PostMapping("/create/loan")
    public Loan createLoan( Loan loan) throws AlreadyExistException, NotFoundException {
        return this.loanService.addLoan(loan);
    }

    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id) throws LoanNotFoundException {
        loanService.deleteLoan(id);
    }
}
