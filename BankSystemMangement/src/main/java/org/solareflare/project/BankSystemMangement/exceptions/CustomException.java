package org.solareflare.project.BankSystemMangement.exceptions;




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


