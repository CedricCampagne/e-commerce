package com.cedriccampagne.ecommerce.product.dto;

import java.math.BigDecimal;


public record ProductUpdateDto (
    String name,
    String description,
    BigDecimal price,
    Integer stock,
    String imageUrl,
    Long categoryId
) {}
