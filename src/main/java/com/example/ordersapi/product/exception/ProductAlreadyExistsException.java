package com.example.ordersapi.product.exception;

import com.example.ordersapi.common.exception.ConflictException;

public class ProductAlreadyExistsException extends ConflictException {

    public ProductAlreadyExistsException(String productName) {
        super(String.format("Product with name \"%s\" already exists", productName));
    }
}
