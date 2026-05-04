package com.cedriccampagne.ecommerce.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cedriccampagne.ecommerce.category.dto.CategoryCreateDto;
import com.cedriccampagne.ecommerce.category.dto.CategoryDto;
import com.cedriccampagne.ecommerce.category.dto.CategoryUpdateDto;

@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> getAllCategories(){
        return categoryRepository.findAll()
            .stream()
            .map(CategoryMapper::toCategoryDto)
            .toList();
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Catégorie introuvable"
            ));
        
        return CategoryMapper.toCategoryDto(category);
    }

    public CategoryDto createCategory(CategoryCreateDto dto) {
        Category category = CategoryMapper.toEntityCategory(dto);

        Category saved = categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(saved);
    }

    public CategoryDto updateCategory(Long id, CategoryUpdateDto dto) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Catégorie introuvable"
            ));
        
        CategoryMapper.updateEntityCategory(category, dto);

        Category updated = categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(updated);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Catégorie introuvable"
            );
        }
        categoryRepository.deleteById(id);
    }
}
