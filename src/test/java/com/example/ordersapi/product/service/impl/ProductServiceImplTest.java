package com.example.ordersapi.product.service.impl;

import com.example.ordersapi.product.api.dto.CreateProductDto;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductAlreadyExistsException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.mapper.ProductMapper;
import com.example.ordersapi.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ProductServiceImpl productService;

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

    @Test
    void createProduct_should_return_created_product() throws Exception {
        // given
        final Integer ID = 1;
        final String NAME = "Product Name";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        CreateProductDto productDto = new CreateProductDto();
        productDto.setName(NAME);
        productDto.setPrice(PRICE);

        Product mappedProduct = new Product();
        mappedProduct.setName(NAME);
        mappedProduct.setPrice(PRICE);

        Product expectedProduct = new Product();
        expectedProduct.setId(ID);
        expectedProduct.setName(NAME);
        expectedProduct.setPrice(PRICE);

        // when
        when(productMapper.createProductDtoToProduct(productDto)).thenReturn(mappedProduct);
        when(productRepository.save(mappedProduct)).thenReturn(expectedProduct);

        // then
        Product savedProduct = productService.createProduct(productDto);
        assertThat(savedProduct.getId(), equalTo(ID));
        assertThat(savedProduct.getName(), equalTo(NAME));
        assertThat(savedProduct.getPrice(), equalTo(PRICE));
    }

    @Test
    void createProduct_should_throw_product_already_exists_when_product_name_is_already_registered() {
        // given
        CreateProductDto productDto = new CreateProductDto();
        Product mappedProduct = new Product();

        // when
        when(productMapper.createProductDtoToProduct(productDto)).thenReturn(mappedProduct);
        when(productRepository.save(mappedProduct)).thenThrow(DataIntegrityViolationException.class);

        // then
        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(productDto));
    }
}