package com.cedriccampagne.ecommerce.cartItem;

import com.cedriccampagne.ecommerce.cartItem.dto.*;

public class CartItemMapper {
    
    public static CartItemDto toCartItemDto(CartItem cartItem) {
        return new CartItemDto(
            cartItem.getId(),
            cartItem.getUser().getId(),
            cartItem.getProduct().getId(),
            cartItem.getQuantity()
        );
    }

    public static CartItem toEntityCartItem(CartItemCreateDto dto) {
        return CartItem.builder()
            .quantity(dto.quantity())
            .build();
    }

    
}
