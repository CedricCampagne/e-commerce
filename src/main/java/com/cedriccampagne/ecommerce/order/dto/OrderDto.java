package com.cedriccampagne.ecommerce.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cedriccampagne.ecommerce.orderItem.dto.OrderItemDto;

public record OrderDto (
    Long id,
    Long userId,
    BigDecimal totalPrice,
    LocalDateTime createdAt,
    List<OrderItemDto> items
) {}
