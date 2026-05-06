package com.cedriccampagne.ecommerce.cartItem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    // Méthodes custom si besoins
    List<CartItem> findByUserId(Long userId);
    
    // Très utile pour éviter les doublons dans le panier
    // Exemple : si le user ajoute 2 fois le même produit, tu incrémentes la quantité au lieu de créer une nouvelle ligne
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
}
