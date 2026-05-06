package com.cedriccampagne.ecommerce.orderItem;

import java.math.BigDecimal;

import com.cedriccampagne.ecommerce.orderItem.dto.OrderItemDto;

public class OrderItemMapper {

    public static OrderItemDto toOrderItemDto(OrderItem item) {
        return new OrderItemDto(
            item.getProduct().getId(),
            item.getProduct().getName(),
            item.getPriceAtPurchase(),
            item.getQuantity(),
            item.getPriceAtPurchase().multiply(
                BigDecimal.valueOf(item.getQuantity())
            )
        );
    }
}

