package com.cedriccampagne.ecommerce.cartItem.dto;

import java.math.BigDecimal;

public record CartItemDetailDto (
    Long productId,
    String productName,
    BigDecimal price,
    int quantity,
    BigDecimal lineTotal
) {}
