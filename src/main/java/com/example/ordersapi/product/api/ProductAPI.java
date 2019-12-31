package com.example.ordersapi.product.api;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.api.dto.UpdateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@Validated
@RequestMapping(ProductAPI.BASE_URL)
public interface ProductAPI {

    String BASE_URL = "/api/v1/products";

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<Product> getAllProducts(@Positive @RequestParam(defaultValue = "1") Integer page,
                                       @Positive @RequestParam(defaultValue = Integer.MAX_VALUE+"") Integer size);

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    Product getProductById(@PathVariable Integer id) throws ProductNotFoundException;

    @GetMapping("name/{name}")
    @ResponseStatus(HttpStatus.OK)
    Product getProductByName(@PathVariable String name) throws ProductNotFoundException;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Product createProduct(@Valid @RequestBody CreateProductDto productDto) throws ProductAlreadyExistsException;

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    Product updateProduct(@PathVariable Integer id, @Valid @RequestBody UpdateProductDto productDto)
            throws ProductNotFoundException, ProductAlreadyExistsException;

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    Product deleteProduct(@PathVariable Integer id) throws ProductNotFoundException;

}
