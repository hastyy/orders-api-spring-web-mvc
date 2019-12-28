package com.example.ordersapi.product.service.impl;

import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.repository.ProductRepository;
import com.example.ordersapi.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void getAllProducts_should_return_found_products() {
        // given
        Product product1 = new Product();
        product1.setId(1);
        Product product2 = new Product();
        product2.setId(2);

        List<Product> toReturn = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(toReturn);

        // then
        Set<Product> returnedProducts = productService.getAllProducts();
        assertEquals(toReturn.size(), returnedProducts.size());
    }

    @Test
    void getOneProduct_by_id_should_return_found_product() throws Exception {
        // given
        final int ID = 1;
        Product product = new Product();
        product.setId(ID);

        when(productRepository.findById(ID)).thenReturn(Optional.of(product));

        // then
        Product returnedProduct = productService.getOneProduct(ID);
        assertEquals(ID, returnedProduct.getId());
    }

    @Test
    void getOneProduct_by_id_should_throw_product_not_found_exception() {

        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getOneProduct(1));
    }
}