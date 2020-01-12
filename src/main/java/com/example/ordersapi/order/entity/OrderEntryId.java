package com.example.ordersapi.order.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderEntryId implements Serializable {

    private Integer order;
    private Integer product;

}
