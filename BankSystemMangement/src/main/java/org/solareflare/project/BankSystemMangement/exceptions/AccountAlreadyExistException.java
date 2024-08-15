package org.solareflare.project.BankSystemMangement.exceptions;

import org.solareflare.project.BankSystemMangement.entities.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountAlreadyExistException extends Exception {
    private Account existingAccount;

    public AccountAlreadyExistException(Account account) {
        this.existingAccount = account;
    }

    public Account getExistingAccount() {
        return existingAccount;
    }
}
