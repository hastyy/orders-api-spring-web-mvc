package com.example.ordersapi.product.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Digits;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
public class UpdateProductDto {

    @Length(min = 1, max = 80, message = "Name cannot be blank and can have at most 80 characters")
    private String name;

    @Length(max = 255, message = "Description can have at most 255 characters")
    private String description;

    @URL(message = "ImageURL has to be an URL")
    @Length(max = 80, message = "ImageURL can have at most 80 characters")
    private String imageUrl;

    @PositiveOrZero(message = "Price must be greater or equal than 0.00")
    @Digits(integer = 19, fraction = 2, message = "Price must be a valid monetary value")
    private BigDecimal price;

}
