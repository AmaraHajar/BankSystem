package org.solareflare.project.BankSystemMangement.controllers;

import org.solareflare.project.BankSystemMangement.beans.Loan;
import org.solareflare.project.BankSystemMangement.bl.LoanBL;
import org.solareflare.project.BankSystemMangement.exceptions.AlreadyExistException;
import org.solareflare.project.BankSystemMangement.exceptions.LoanNotFoundException;
import org.solareflare.project.BankSystemMangement.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanBL loanBL;


    @GetMapping("get/all")
    public List<Loan> getAllLoans() {
        return loanBL.getAllLoans();
    }

    @GetMapping("/get/{id}")
    public Loan getLoanById(@PathVariable Long id) throws LoanNotFoundException {
            return this.loanBL.getLoanById(id);
    }

    @PostMapping("/create/loan")
    public Loan createLoan( Loan loan) throws AlreadyExistException, NotFoundException {
        return this.loanBL.addLoan(loan);
    }

    @DeleteMapping("/{id}")
    public void deleteLoan(@PathVariable Long id) {
        loanBL.deleteLoan(id);
    }
}
