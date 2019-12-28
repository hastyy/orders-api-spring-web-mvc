package com.example.ordersapi.product.service;

import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductNotFoundException;

import java.util.Set;

public interface ProductService {

    Set<Product> getAllProducts();

    Product getOneProduct(Integer id) throws ProductNotFoundException;
}
