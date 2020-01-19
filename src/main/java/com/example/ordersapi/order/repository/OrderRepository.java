package com.example.ordersapi.order.repository;

import com.example.ordersapi.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
