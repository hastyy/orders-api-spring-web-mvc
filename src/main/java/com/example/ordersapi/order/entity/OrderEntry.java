package com.example.ordersapi.order.entity;

import com.example.ordersapi.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "order_entry")
@IdClass(OrderEntryId.class)
@ToString(exclude = {"order"})
public class OrderEntry {

    @Id
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;


}
