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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductAPI {

    private final ProductService productService;

    @Override
    public Collection<Product> getAllProducts(@Positive Integer page, @Positive Integer size) {

        log.info("Listing products");

        Collection<Product> products = productService.getProductsPage(page, size);

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
    public Product getProductByName(String name) throws ProductNotFoundException {

        log.info("Fetching product with name {}", name);

        Product product = productService.getOneProduct(name);

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

    @Override
    public Product deleteProduct(Integer id) throws ProductNotFoundException {

        log.info("Deleting product with id {}", id);

        Product product = productService.deleteProduct(id);

        log.info("Retuning deleted product");
        log.debug("Product: {}", product);

        return product;
    }

}
