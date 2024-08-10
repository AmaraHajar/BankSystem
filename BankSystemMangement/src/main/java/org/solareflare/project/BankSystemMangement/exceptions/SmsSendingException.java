package org.solareflare.project.BankSystemMangement.exceptions;

import com.twilio.exception.ApiException;

public class SmsSendingException extends RuntimeException {
    public SmsSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
