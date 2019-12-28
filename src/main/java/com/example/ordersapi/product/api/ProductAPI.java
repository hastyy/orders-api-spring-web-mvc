package com.example.ordersapi.product.api;

import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;

@RequestMapping(ProductAPI.BASE_URL)
public interface ProductAPI {

    String BASE_URL = "/api/v1/products";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Set<Product> getAllProducts();

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    Product getProductById(@PathVariable Integer id) throws ProductNotFoundException;

}
