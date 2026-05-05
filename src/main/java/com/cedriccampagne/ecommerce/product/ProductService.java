package com.cedriccampagne.ecommerce.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cedriccampagne.ecommerce.category.CategoryRepository;
import com.cedriccampagne.ecommerce.category.Category;
import com.cedriccampagne.ecommerce.product.dto.*;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
        ProductRepository productRepository,
        CategoryRepository categoryRepository
    ){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDto> getAllProducts(){
        return productRepository.findAll()
            .stream()
            .map(ProductMapper::toProductDto)
            .toList();
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produit introuvable"
            ));
        
        return ProductMapper.toProductDto(product);
    }

    public ProductDto createProduct(ProductCreateDto dto){

    // Charger la catégorie depuis l'id du DTO
    Category category = categoryRepository.findById(dto.categoryId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Catégorie introuvable"
        ));

    Product product = ProductMapper.toEntityProduct(dto);

    // Associer la catégorie à l'entité
    product.setCategory(category);


    Product saved = productRepository.save(product);


    return ProductMapper.toProductDto(saved);

    }

    public ProductDto updateProduct(Long id, ProductUpdateDto dto) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Produit introuvable"
        ));

        ProductMapper.updateEntityProduct(product, dto);

        // Si categoryId est fourni => charger la nouvelle catégorie
        if (dto.categoryId() != null) {
            Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Catégorie introuvable"
                ));
            product.setCategory(category);
        }
        
        Product updated = productRepository.save(product);

        return ProductMapper.toProductDto(updated);
    }

    public void deleteProduct(Long id){
        // on charge l'objet car relation
        // Si l’entité n’a pas de relations importantes => existsById() suffit
        // Si l’entité a des relations ou de la logique métier => findById() est mieux
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produit introuvable"
            ));

        productRepository.delete(product);
    }
}
