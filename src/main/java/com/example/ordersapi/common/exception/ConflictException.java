package com.example.ordersapi.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends RestException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
