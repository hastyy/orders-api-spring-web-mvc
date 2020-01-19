package com.example.ordersapi.order.api;

import com.example.ordersapi.order.api.dto.OrderDto;
import com.example.ordersapi.order.entity.Order;
import com.example.ordersapi.order.exception.DuplicateOrderEntryException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@RequestMapping(OrderAPI.BASE_URL)
public interface OrderAPI {

    String BASE_URL = "/api/v1/orders";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Order create(Authentication authContext, @Valid @RequestBody OrderDto orderDto)
            throws ProductNotFoundException, DuplicateOrderEntryException;

}
