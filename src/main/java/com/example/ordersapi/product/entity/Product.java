package com.example.ordersapi.product.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 80)
    private String name;

    @Column
    private String description;

    @Column(length = 80)
    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

}
