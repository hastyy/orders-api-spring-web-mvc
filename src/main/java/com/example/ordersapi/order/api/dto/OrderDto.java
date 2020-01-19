package com.example.ordersapi.order.api.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderDto {

    @Valid
    @NotEmpty(message = "An order must specify the products")
    private List<OrderEntryDto> products;

}
