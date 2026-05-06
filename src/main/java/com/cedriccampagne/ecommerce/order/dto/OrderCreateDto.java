package com.cedriccampagne.ecommerce.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderCreateDto (
    // On crée une commande a partir d'un panier
    @NotNull(message = "L'identifiant utilisateur est obligatoire")
    Long userId
) {}
