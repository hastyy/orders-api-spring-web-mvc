package com.example.ordersapi.product.repository;

import com.example.ordersapi.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findByName(String name);

    Set<Product> findAllByNameIn(Set<String> names);

}
