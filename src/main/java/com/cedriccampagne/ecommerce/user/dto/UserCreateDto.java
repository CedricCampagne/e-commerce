package com.cedriccampagne.ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateDto (
    @NotBlank(message = "Le username est obligatoire")
    String username,

    @NotBlank(message = "Le mot de pass est obligatoire")
    String password,

    @Email(message = "Email invalide")
    @NotBlank(message = "L'email est obligatoire")
    String email
){}
