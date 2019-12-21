package com.example.ordersapi.product.controller;

import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductService productService;

    ProductController productController;

    @BeforeEach
    void setUp() {
        productController = new ProductController(productService);
    }

    @Test
    void getAllProducts_should_return_found_products() {
        // given
        Product product1 = new Product();
        product1.setId(1);
        Product product2 = new Product();
        product2.setId(2);
        Set<Product> productsToReturn = new HashSet<>();
        productsToReturn.add(product1);
        productsToReturn.add(product2);

        when(productService.getAllProducts()).thenReturn(productsToReturn);

        // then
        Set<Product> returnedProducts = productController.getAllProducts();
        assertEquals(productsToReturn.size(), returnedProducts.size());
        verify(productService, atLeastOnce()).getAllProducts();
        verify(productService, atMostOnce()).getAllProducts();
    }
}