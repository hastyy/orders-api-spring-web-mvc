package com.example.ordersapi.user.api.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {

    @Email(message = "Field must be an email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Length(min = 6, max = 128, message = "Password length must be between 6 and 128 characters")
    private String password;

}
