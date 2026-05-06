package com.cedriccampagne.ecommerce.orderItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Méthodes custom si besoins
    
}
