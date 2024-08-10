package org.solareflare.project.BankSystemMangement.exceptions;

import org.springframework.mail.MailException;


public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}