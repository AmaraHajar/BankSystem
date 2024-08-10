package org.solareflare.project.BankSystemMangement.exceptions;


import org.solareflare.project.BankSystemMangement.beans.Customer;

public class CustomException extends RuntimeException {
    private final String typeName;
    private final String message;

    public <T> CustomException(Class<T> type, String message) {
        this.typeName = type.getSimpleName();
        this.message = typeName + message;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String getMessage() {
        return message;
    }
}


//
//public class CustomerNotRegisteredException extends CustomException {
//    public CustomerNotRegisteredException() {
//        super(Customer.class, "Customer not registered or does not exist");
//    }
//}
//

