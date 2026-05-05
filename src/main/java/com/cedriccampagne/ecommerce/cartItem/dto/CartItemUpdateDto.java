package com.cedriccampagne.ecommerce.cartItem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartItemUpdateDto(
    @NotNull(message = "La quantité est obligatoire.")
    @Min(value = 1, message = "La quantité doit être au minimum de 1.")
    Integer quantity
) {}
