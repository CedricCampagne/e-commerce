package com.cedriccampagne.ecommerce.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cedriccampagne.ecommerce.category.CategoryRepository;
import com.cedriccampagne.ecommerce.common.pagination.PaginationResponse;
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

    // public List<ProductDto> getAllProducts(){
    //     return productRepository.findAll()
    //         .stream()
    //         .map(ProductMapper::toProductDto)
    //         .toList();
    // }

    public PaginationResponse<ProductListDto> getAllProducts(int page, int size, String sort){

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        String sortDirection = sortParams[1];

        Sort sorting = sortDirection.equalsIgnoreCase("desc")
            ? Sort.by(sortField).descending()
            : Sort.by(sortField).ascending();

        // Construire l'objet Pageable
        Pageable pageable = PageRequest.of(
            page,
            size,
            sorting
        );

        // Appek=ler repo avec pagination
        Page<Product> result = productRepository.findAll(pageable);

        // Mapper en ProductListDto
        List<ProductListDto> items = result.getContent()
            .stream()
            .map(ProductMapper::toListDto)
            .toList();

        // Construire la reponse pagination
        return new PaginationResponse<>(
            items,
            result.getNumber(),
            result.getSize(),
            // result.getTotalElements() renvoie un long dto attends un int on doit caster en int
            (int) result.getTotalElements(),
            result.getTotalPages(),
            result.hasNext(),
            result.hasPrevious()
        );
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
