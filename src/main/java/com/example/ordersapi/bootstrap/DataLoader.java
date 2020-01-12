package com.example.ordersapi.bootstrap;

import com.example.ordersapi.order.OrderRepository;
import com.example.ordersapi.order.entity.Order;
import com.example.ordersapi.order.entity.OrderEntry;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.repository.ProductRepository;
import com.example.ordersapi.user.entity.User;
import com.example.ordersapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    public static final int NUMBER_OF_PRODUCTS = 10;

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String... args) throws Exception {
        List<Product> products = loadProducts();
        List<User> users = loadUsers();

        log.info("Loading orders to the database...");

        OrderEntry entry = new OrderEntry();
        entry.setProduct(products.get(0));
        entry.setQuantity(42);

        Order order = new Order();
        order.setUser(users.get(0));
        order.setOrderEntries(Collections.singleton(entry));

        entry.setOrder(order);

        orderRepository.saveAndFlush(order);

        log.info("Finished loading orders.");
    }

    private List<Product> loadProducts() {
        log.info("Loading products to the database...");

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

        log.info("Finished loading products.");

        return products;
    }

    private List<User> loadUsers() {
        log.info("Loading users to the database...");

        User user = new User();

        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("test_password"));

        userRepository.saveAndFlush(user);

        log.info("Finished loading users.");

        return Collections.singletonList(user);
    }

}
