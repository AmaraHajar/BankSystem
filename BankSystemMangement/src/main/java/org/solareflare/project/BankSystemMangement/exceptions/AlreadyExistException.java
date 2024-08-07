package org.solareflare.project.BankSystemMangement.exceptions;

public class AlreadyExistException extends Exception{

    private Object existingEntity;

    public <T> AlreadyExistException(T existingEntity) {
        this.existingEntity = existingEntity;
    }

    @SuppressWarnings("unchecked")
    public <T> T getExistingEntity() {
        return (T) this.existingEntity;
    }
}
