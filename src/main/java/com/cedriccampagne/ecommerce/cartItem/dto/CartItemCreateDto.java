package com.cedriccampagne.ecommerce.cartItem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemCreateDto (

    @NotNull(message = "L'utilisateur est obligatoire.")
    Long userId,

    @NotNull(message = "Le produit est obligatoire.")
    Long productId,
    
    @NotNull(message = "La quantité est obligatoire.")
    @Min(value = 1, message = "La quantité doit être au minimum de 1.")
    Integer quantity
) {}
