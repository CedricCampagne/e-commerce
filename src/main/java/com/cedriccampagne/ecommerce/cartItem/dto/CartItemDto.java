package com.cedriccampagne.ecommerce.cartItem.dto;

public record CartItemDto (
    Long id,
    Long userId,
    Long productId,
    Integer quantity
) {}
