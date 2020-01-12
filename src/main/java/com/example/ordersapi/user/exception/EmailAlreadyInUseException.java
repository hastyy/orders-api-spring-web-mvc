package com.example.ordersapi.user.exception;

import com.example.ordersapi.common.exception.ConflictException;

public class EmailAlreadyInUseException extends ConflictException {

    public EmailAlreadyInUseException(String email) {
        super(String.format("The email \"%s\" has already been registered", email));
    }
}
