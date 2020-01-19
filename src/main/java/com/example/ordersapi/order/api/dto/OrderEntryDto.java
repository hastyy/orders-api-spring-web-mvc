package com.example.ordersapi.order.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class OrderEntryDto {

    @NotBlank(message = "Product Name is required")
    @Length(max = 80, message = "Product Name can have at most 80 characters")
    private String product;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Product quantity must be higher than 0")
    private Integer quantity;

}
