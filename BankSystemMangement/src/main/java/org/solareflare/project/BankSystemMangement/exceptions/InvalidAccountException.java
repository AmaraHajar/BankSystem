package org.solareflare.project.BankSystemMangement.exceptions;

public class InvalidAccountException extends RuntimeException {
    public InvalidAccountException(String message) {
        super(message);
    }
}
