package com.cedriccampagne.ecommerce.product;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cedriccampagne.ecommerce.common.pagination.PaginationResponse;
import com.cedriccampagne.ecommerce.product.dto.ProductCreateDto;
import com.cedriccampagne.ecommerce.product.dto.ProductDto;
import com.cedriccampagne.ecommerce.product.dto.ProductListDto;
import com.cedriccampagne.ecommerce.product.dto.ProductUpdateDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public PaginationResponse<ProductListDto> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt,desc") String sort,
        @RequestParam(required = false) Long category,
        @RequestParam(defaultValue = "0") BigDecimal minPrice,
         @RequestParam(defaultValue = "999999") BigDecimal maxPrice
    ){
        return productService.getAllProducts(
            page,
            size,
            sort,
            category,
            minPrice,
            maxPrice
        );
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@Valid @RequestBody ProductCreateDto dto) {
        return productService.createProduct(dto);
    }

    @PatchMapping("/{id}")
    public ProductDto updateProduct(
        @PathVariable Long id,
        @Valid @RequestBody ProductUpdateDto dto
    ) {
        return productService.updateProduct(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}
