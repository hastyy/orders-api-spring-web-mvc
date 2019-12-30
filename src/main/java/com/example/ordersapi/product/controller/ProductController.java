package com.example.ordersapi.product.controller;

import com.example.ordersapi.product.api.ProductAPI;
import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.api.dto.UpdateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @Override
    public Product getProductById(Integer id) throws ProductNotFoundException {

        log.info("Fetching product with id {}", id);

        Product product = productService.getOneProduct(id);

        log.info("Retuning found product");
        log.debug("Product: {}", product);

        return product;
    }

    @Override
    public Product createProduct(@Valid CreateProductDto productDto) throws ProductAlreadyExistsException {

        log.info("Creating product: {}", productDto.getName());

        Product product = productService.createProduct(productDto);

        log.info("Created product: {}", product.getName());
        log.debug("Product: {}", product);

        return product;
    }

    @Override
    public Product updateProduct(Integer id, @Valid UpdateProductDto productDto) throws ProductNotFoundException,
            ProductAlreadyExistsException {

        log.info("Updating product {}", id);
        log.debug("Update Product DTO: {}", productDto);

        Product product = productService.updateProduct(id, productDto);

        log.info("Updated product {}", id);
        log.debug("Updated Product: {}", product);

        return product;
    }

}
