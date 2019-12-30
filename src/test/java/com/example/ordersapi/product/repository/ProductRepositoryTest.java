package com.example.ordersapi.product.repository;

import com.example.ordersapi.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    void findByName_should_find_product_by_name() {
        // given
        final String NAME = "Product Name";
        final BigDecimal PRICE = BigDecimal.valueOf(2.95);

        Product product = new Product();
        product.setName(NAME);
        product.setPrice(PRICE);

        // when
        productRepository.saveAndFlush(product);
        Optional<Product> foundProduct = productRepository.findByName(NAME);

        // then
        assertThat(foundProduct.isPresent());
    }
}