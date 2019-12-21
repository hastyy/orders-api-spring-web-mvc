package com.example.ordersapi.product.repository;

import com.example.ordersapi.product.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {
}
