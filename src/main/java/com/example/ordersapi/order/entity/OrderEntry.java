package com.example.ordersapi.order.entity;

import com.example.ordersapi.product.entity.Product;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@IdClass(OrderEntryId.class)
@Table(name = "order_entry")
public class OrderEntry {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

}
