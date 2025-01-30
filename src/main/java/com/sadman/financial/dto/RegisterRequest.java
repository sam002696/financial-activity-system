package com.sadman.financial.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {


    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

//    private String role;
//
//    @NotBlank(message = "PhoneNo is required")
//    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;


}
