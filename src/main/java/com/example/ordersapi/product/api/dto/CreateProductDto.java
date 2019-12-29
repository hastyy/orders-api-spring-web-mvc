package com.example.ordersapi.product.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class CreateProductDto {

    @NotBlank(message = "Name is required")
    @Length(max = 80, message = "Name can have at most 80 characters")
    private String name;

    @Length(max = 255, message = "Description can have at most 255 characters")
    private String description;

    @URL(message = "ImageURL has to be an URL")
    @Length(max = 80, message = "ImageURL can have at most 80 characters")
    private String imageUrl;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be greater or equal than 0.00")
    @Digits(integer = 19, fraction = 2, message = "Price must be a valid monetary value")
    private BigDecimal price;

}
