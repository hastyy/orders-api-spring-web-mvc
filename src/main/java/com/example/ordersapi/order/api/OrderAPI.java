package com.example.ordersapi.order.api;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(OrderAPI.BASE_URL)
public interface OrderAPI {

    String BASE_URL = "/api/v1/orders";

}
