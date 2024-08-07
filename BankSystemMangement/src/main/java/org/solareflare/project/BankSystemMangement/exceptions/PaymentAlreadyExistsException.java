package org.solareflare.project.BankSystemMangement.exceptions;

import org.solareflare.project.BankSystemMangement.beans.Payment;

public class PaymentAlreadyExistsException extends Throwable {

    private Payment existingPayment;

    public PaymentAlreadyExistsException(Payment payment) {
        this.existingPayment = payment;
    }

    public Payment getExistingPayment() {
        return existingPayment;
    }
}
