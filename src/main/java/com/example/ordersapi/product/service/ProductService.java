package com.example.ordersapi.product.service;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.api.dto.UpdateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;

import java.util.Set;

public interface ProductService {

    Set<Product> getAllProducts();

    Product getOneProduct(Integer id) throws ProductNotFoundException;

    Product getOneProduct(String name) throws ProductNotFoundException;

    Product createProduct(CreateProductDto productDto) throws ProductAlreadyExistsException;

    Product updateProduct(Integer id, UpdateProductDto productDto)
            throws ProductNotFoundException,ProductAlreadyExistsException;

    Product deleteProduct(Integer id) throws ProductNotFoundException;
}
