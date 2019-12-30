package com.example.ordersapi.product.exception;

import com.example.ordersapi.common.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {

    public ProductNotFoundException(Integer id) {
        super(String.format("Unable to find product with id: %d", id));
    }

    public ProductNotFoundException(String name) {
        super(String.format("Unable to find product with name: %s", name));
    }

}
