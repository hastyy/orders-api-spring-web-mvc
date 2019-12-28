package com.example.ordersapi.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
