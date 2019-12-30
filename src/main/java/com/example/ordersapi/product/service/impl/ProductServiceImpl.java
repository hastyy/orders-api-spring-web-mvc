package com.example.ordersapi.product.service.impl;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.api.dto.UpdateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.mapper.ProductMapper;
import com.example.ordersapi.product.repository.ProductRepository;
import com.example.ordersapi.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Product product = productMapper.createProductDtoToProduct(productDto);
        Product savedProduct = saveProduct(product);

        return savedProduct;
    }

    private Product saveProduct(Product product) throws ProductAlreadyExistsException {
        try {
            return productRepository.saveAndFlush(product);
        } catch (DataIntegrityViolationException ex) {
            throw new ProductAlreadyExistsException(product.getName());
        }
    }

    /**
     * Method needs to be wrapped in a transaction because we are making two database queries:
     *  1. Finding the Product by id (read)
     *  2. Updating found product (write)
     *
     *  Other database clients might perform a write operation over the same entity between our read and write,
     *  which would cause inconsistencies in the system. Thus, we have to operate over a snapshot of the database and
     *  commit or rollback (and probably re-attempt the operation?) depending if its state has changed meanwhile.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product updateProduct(Integer id, UpdateProductDto productDto) throws ProductNotFoundException,
            ProductAlreadyExistsException {

        Product product = getOneProduct(id);

        if (update(product, productDto)) {
            saveProduct(product);
        }

        return product;
    }

    private boolean update(Product product, UpdateProductDto productDto) {
        boolean productWasUpdated = false;

        if (productDto.getName() != null && !productDto.getName().equals(product.getName())) {
            product.setName(productDto.getName());
            productWasUpdated = true;
        }

        if (productDto.getDescription() != null && !productDto.getDescription().equals(product.getDescription())) {
            product.setDescription(productDto.getDescription());
            productWasUpdated = true;
        }

        if (productDto.getImageUrl() != null && !productDto.getImageUrl().equals(product.getImageUrl())) {
            product.setImageUrl(productDto.getImageUrl());
            productWasUpdated = true;
        }

        if (productDto.getPrice() != null && !productDto.getPrice().equals(product.getPrice())) {
            product.setPrice(productDto.getPrice());
            productWasUpdated = true;
        }

        return productWasUpdated;
    }

    /**
     * Method needs to be wrapped in a transaction because we are making two database queries:
     *  1. Finding the Product by id (read)
     *  2. Delete found product (write)
     *
     *  Other database clients might perform a write operation over the same entity between our read and write,
     *  which would cause inconsistencies in the system. Thus, we have to operate over a snapshot of the database and
     *  commit or rollback (and probably re-attempt the operation?) depending if its state has changed meanwhile.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product deleteProduct(Integer id) throws ProductNotFoundException {
        Product product = getOneProduct(id);
        productRepository.delete(product);

        return product;
    }

}
