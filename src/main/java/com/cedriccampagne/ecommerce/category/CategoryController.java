package com.cedriccampagne.ecommerce.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cedriccampagne.ecommerce.category.dto.CategoryCreateDto;
import com.cedriccampagne.ecommerce.category.dto.CategoryDto;
import com.cedriccampagne.ecommerce.category.dto.CategoryUpdateDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryCreateDto dto) {
        return categoryService.createCategory(dto);
    }
    
    @PatchMapping("/{id}")
    public CategoryDto updateCategory(
        @PathVariable Long id,
        @Valid @RequestBody CategoryUpdateDto dto
    ){
        return categoryService.updateCategory(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
