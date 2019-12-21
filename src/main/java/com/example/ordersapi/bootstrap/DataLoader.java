package com.example.ordersapi.bootstrap;

import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    public static final int NUMBER_OF_PRODUCTS = 10;

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("Loading products to the database...");

        loadProducts();

        log.info("Finished loading products.");

    }

    private void loadProducts() {
        List<Product> products = new ArrayList<>(NUMBER_OF_PRODUCTS);

        for (int i = 0; i < NUMBER_OF_PRODUCTS; i++) {
            Product product = new Product();

            product.setName(String.format("Product %d", i+1));
            product.setDescription(String.format("This is the description for product %d", i+1));
            product.setImageUrl(String.format("http://this-should-point-to-a-bucket.xyz/%d", i+1));
            product.setPrice(new BigDecimal(i+1));

            products.add(product);
        }

        productRepository.saveAll(products);
    }
}
