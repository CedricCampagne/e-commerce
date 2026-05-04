package com.cedriccampagne.ecommerce.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDto (
    @NotBlank(message = "le nom de la catégorie est obligatoire ")
    String name
) {}