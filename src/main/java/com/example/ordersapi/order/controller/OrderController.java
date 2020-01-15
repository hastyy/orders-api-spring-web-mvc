package com.example.ordersapi.order.controller;

import com.example.ordersapi.order.api.OrderAPI;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController implements OrderAPI {

    @GetMapping
    public String hello() {
        return "Hello from OrderController !";
    }

}
