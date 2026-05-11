package com.cedriccampagne.ecommerce.product.dto;

import java.math.BigDecimal;

public record ProductListDto (
    Long id,
    String name,
    String imageUrl,
    BigDecimal price,
    Integer stock,
    Long categoryId,
    String categoryName
) {}
