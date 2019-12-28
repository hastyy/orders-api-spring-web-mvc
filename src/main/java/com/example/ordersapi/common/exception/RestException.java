package com.example.ordersapi.common.exception;

import org.springframework.http.HttpStatus;

public class RestException extends Exception {

    private final HttpStatus httpStatus;

    public RestException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
