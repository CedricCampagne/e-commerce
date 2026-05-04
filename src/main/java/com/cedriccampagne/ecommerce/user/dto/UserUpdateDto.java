package com.cedriccampagne.ecommerce.user.dto;

import jakarta.validation.constraints.Email;

public record UserUpdateDto (
    String username,
    String password,

    @Email(message = "Email invalide")
    String email
) {}
