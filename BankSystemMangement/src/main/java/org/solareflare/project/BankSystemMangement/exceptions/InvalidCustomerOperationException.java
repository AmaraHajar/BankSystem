package org.solareflare.project.BankSystemMangement.exceptions;

import org.solareflare.project.BankSystemMangement.beans.Customer;

public class InvalidCustomerOperationException extends CustomException {
    public InvalidCustomerOperationException(String message) {
        super(Customer.class, message);
    }
}