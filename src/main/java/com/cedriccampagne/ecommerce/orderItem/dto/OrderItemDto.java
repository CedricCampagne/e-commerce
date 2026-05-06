package com.cedriccampagne.ecommerce.orderItem.dto;

import java.math.BigDecimal;

public record OrderItemDto(
        Long productId,
        String productName,
        BigDecimal priceAtPurchase,
        int quantity,
        BigDecimal lineTotal
) {}
