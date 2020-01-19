package com.example.ordersapi.order.exception;

import com.example.ordersapi.common.exception.ConflictException;

public class DuplicateOrderEntryException extends ConflictException {

    public DuplicateOrderEntryException() {
        super("Your order cannot have the same product specified twice");
    }
}
