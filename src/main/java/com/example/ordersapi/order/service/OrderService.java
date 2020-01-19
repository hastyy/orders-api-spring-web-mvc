package com.example.ordersapi.order.service;

import com.example.ordersapi.order.api.dto.OrderDto;
import com.example.ordersapi.order.entity.Order;
import com.example.ordersapi.order.exception.DuplicateOrderEntryException;
import com.example.ordersapi.product.exception.ProductNotFoundException;
import com.example.ordersapi.user.entity.User;

public interface OrderService {

    Order createOrder(User user, OrderDto orderDto) throws ProductNotFoundException, DuplicateOrderEntryException;

}
