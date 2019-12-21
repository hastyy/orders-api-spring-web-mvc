package com.example.ordersapi.product.service.impl;

import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.repository.ProductRepository;
import com.example.ordersapi.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Set<Product> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());
    }
}
