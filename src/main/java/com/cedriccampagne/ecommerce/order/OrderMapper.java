package com.cedriccampagne.ecommerce.order;

import com.cedriccampagne.ecommerce.order.dto.OrderDto;
import com.cedriccampagne.ecommerce.orderItem.OrderItemMapper;

public class OrderMapper {
    
    public static OrderDto toOrderDto(Order order){
        return new OrderDto(
            order.getId(),
            order.getUser().getId(),
            order.getTotalPrice(),
            order.getCreatedAt(),
            order.getItems()
                .stream()
                .map(OrderItemMapper::toOrderItemDto)
                .toList()
        );
    }
}
