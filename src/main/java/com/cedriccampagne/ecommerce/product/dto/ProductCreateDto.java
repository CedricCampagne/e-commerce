package com.cedriccampagne.ecommerce.product.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductCreateDto (
    @NotBlank(message = "Le nom du produit est obligatoire")
    String name,

    @NotBlank(message = "La description du produit est obligatoire")
    String description,

    @NotNull(message = "Le prix du produit est obligatoire")
    BigDecimal price,

    @PositiveOrZero(message = "Le prix doit être positif ou égal à zéro")
    Integer stock,

    @NotBlank(message = "L'imageUrl du produit est obligatoire")
    String imageUrl,

    @NotNull(message = "L'id de la catégorie du produit est obligatoire")
    Long categoryId
) {}
