package com.example.ordersapi.product.controller;

import com.example.ordersapi.product.api.ProductAPI;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductAPI {

    private final ProductService productService;

    @Override
    public Set<Product> getAllProducts() {

        log.info("Listing products");

        Set<Product> products = productService.getAllProducts();

        log.info("Returning #{} products", products.size());
        log.debug("Products: {}", products);

        return products;
    }
}
