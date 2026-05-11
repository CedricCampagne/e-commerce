package com.cedriccampagne.ecommerce.product;

import com.cedriccampagne.ecommerce.product.dto.*;

public class ProductMapper {
    public static ProductDto toProductDto(Product product) {
        return new ProductDto(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getImageUrl(),
            product.getCategory().getId()
        );
    }

    public static Product toEntityProduct(ProductCreateDto dto) {
        return Product.builder()
        .name(dto.name())
        .description(dto.description())
        .price(dto.price())
        .stock(dto.stock())
        .imageUrl(dto.imageUrl())
        // pas de categorie ici c'est le service qui chargera la categorie
        // pas de logique métier dans le mapper
        .build();
    }

    public static void updateEntityProduct(Product product, ProductUpdateDto dto) {
        if (dto == null) return;

        if (dto.name() != null && !dto.name().isBlank()) {
            product.setName(dto.name());
        }

        if (dto.description() != null && !dto.description().isBlank()) {
            product.setDescription(dto.description());
        }

        if (dto.price() != null) {
            product.setPrice(dto.price());
        }

        if (dto.stock() != null) {
            product.setStock(dto.stock());
        }

        if (dto.imageUrl() != null && !dto.imageUrl().isBlank()) {
            product.setImageUrl(dto.imageUrl());
        }
        // Le service gérera dto.categoryId()
    }

    public static ProductListDto toListDto(Product product) {
        return new ProductListDto(
            product.getId(),
            product.getName(),
            product.getImageUrl(),
            product.getPrice(),
            product.getStock()
        );
    }
    
}
