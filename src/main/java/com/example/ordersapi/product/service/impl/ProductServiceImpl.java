package com.example.ordersapi.product.service.impl;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.mapper.ProductMapper;
import com.example.ordersapi.product.repository.ProductRepository;
import com.example.ordersapi.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Set<Product> getAllProducts() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());
    }

    @Override
    public Product getOneProduct(Integer id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product createProduct(CreateProductDto productDto) throws ProductAlreadyExistsException {
        try {
            Product product = productMapper.createProductDtoToProduct(productDto);
            Product savedProduct = productRepository.save(product);

            return savedProduct;
        } catch (DataIntegrityViolationException ex) {
            throw new ProductAlreadyExistsException(productDto.getName());
        }
    }
}
