package org.solareflare.project.BankSystemMangement.exceptions;


import org.solareflare.project.BankSystemMangement.beans.User;

import java.util.Optional;

public class UserAlreadyExistException extends Exception {

    private User existingUser;

    public UserAlreadyExistException(User user) {
        this.existingUser = user;
    }

    public User getExistingUser() {
        return this.existingUser;
    }
}
