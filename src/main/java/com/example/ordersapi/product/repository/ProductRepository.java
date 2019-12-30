package com.example.ordersapi.product.repository;

import com.example.ordersapi.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
