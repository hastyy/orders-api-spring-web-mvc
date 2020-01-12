package com.example.ordersapi.product.entity;

import com.example.ordersapi.order.entity.OrderEntry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@ToString(exclude = {"orderEntries"})
@EqualsAndHashCode(exclude = {"orderEntries"})
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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private Set<OrderEntry> orderEntries = new HashSet<>();

}
