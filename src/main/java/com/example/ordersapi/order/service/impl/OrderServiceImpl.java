package com.example.ordersapi.order.service.impl;

import com.example.ordersapi.order.api.dto.OrderDto;
import com.example.ordersapi.order.api.dto.OrderEntryDto;
import com.example.ordersapi.order.entity.Order;
import com.example.ordersapi.order.entity.OrderEntry;
import com.example.ordersapi.order.exception.DuplicateOrderEntryException;
import com.example.ordersapi.order.repository.OrderRepository;
import com.example.ordersapi.order.service.OrderService;
import com.example.ordersapi.product.entity.Product;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.product.repository.ProductRepository;
import com.example.ordersapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(User user, OrderDto orderDto)
            throws ProductNotFoundException, DuplicateOrderEntryException {
        Order order = new Order();
        order.setUser(user);
        order.setEntries(new HashSet<>());

        Set<String> productNames = orderDto.getProducts().stream()
                .map(OrderEntryDto::getProduct)
                .collect(Collectors.toSet());

        if (productNames.size() < orderDto.getProducts().size()) {
            throw new DuplicateOrderEntryException();
        }

        Map<String, Product> products = productRepository.findAllByNameIn(productNames).stream()
                .collect(Collectors.toMap(Product::getName, Function.identity()));

        for (OrderEntryDto entry : orderDto.getProducts()) {
            OrderEntry orderEntry = new OrderEntry();

            Product product = Optional.ofNullable(products.get(entry.getProduct()))
                    .orElseThrow(() -> new ProductNotFoundException(entry.getProduct()));

            orderEntry.setProduct(product);
            orderEntry.setQuantity(entry.getQuantity());

            orderEntry.setOrder(order);
            order.getEntries().add(orderEntry);
        }

        return orderRepository.saveAndFlush(order);
    }

}
