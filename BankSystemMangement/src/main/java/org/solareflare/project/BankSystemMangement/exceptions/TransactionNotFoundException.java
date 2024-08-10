package org.solareflare.project.BankSystemMangement.exceptions;


public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
