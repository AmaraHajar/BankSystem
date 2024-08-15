package org.solareflare.project.BankSystemMangement.exceptions;

import org.solareflare.project.BankSystemMangement.entities.Customer;

public class InvalidCustomerOperationException extends CustomException {
    public InvalidCustomerOperationException(String message) {
        super(Customer.class, message);
    }
}