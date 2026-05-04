package com.cedriccampagne.ecommerce.category;

import com.cedriccampagne.ecommerce.category.dto.*;

public class CategoryMapper {
    
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
            category.getId(),
            category.getName()
        );
    }

    public static Category toEntityCategory(CategoryCreateDto dto) {
        return Category.builder()
            .name(dto.name())
            .build();
    }

    public static void updateEntityCategory(Category category, CategoryUpdateDto dto) {
        if (dto == null) return;

        if (dto.name() != null && !dto.name().isBlank()) {
            category.setName(dto.name());
        }
    }
}
