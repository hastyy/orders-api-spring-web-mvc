package com.example.ordersapi.order.controller;

import com.example.ordersapi.authentication.model.Principal;
import com.example.ordersapi.order.api.OrderAPI;
import com.example.ordersapi.order.api.dto.OrderDto;
import com.example.ordersapi.order.entity.Order;
import com.example.ordersapi.order.exception.DuplicateOrderEntryException;
import com.example.ordersapi.order.service.OrderService;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderController implements OrderAPI {

    private final OrderService orderService;

    @GetMapping
    public String hello() {
        return "Hello from OrderController !";
    }

    @Override
    public Order create(Authentication authContext, @Valid OrderDto orderDto)
            throws ProductNotFoundException, DuplicateOrderEntryException {

        User user = ((Principal) authContext.getPrincipal()).getUserEntity();

        log.info("Creating order for user: {}", user);

        Order order = orderService.createOrder(user, orderDto);

        log.info("Created order: {}", order);

        return order;
    }
}
