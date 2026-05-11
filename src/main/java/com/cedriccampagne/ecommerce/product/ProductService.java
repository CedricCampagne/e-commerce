package com.cedriccampagne.ecommerce.product;

import java.math.BigDecimal;
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

    public PaginationResponse<ProductListDto> getAllProducts(
        int page,
        int size,
        String sort,
        Long categoryId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        String search,
        Boolean inStock
        ){

        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        String sortDirection = sortParams.length > 1 ? sortParams[1] : "asc";

        Sort sorting = sortDirection.equalsIgnoreCase("desc")
            ? Sort.by(sortField).descending()
            : Sort.by(sortField).ascending();

        // Construire l'objet Pageable
        Pageable pageable = PageRequest.of(
            page,
            size,
            sorting
        );

        
        Page<Product> result;

        boolean stockFilter = (inStock != null && inStock);

        // 1) Search + catégorie + prix + stock
        if (stockFilter && search != null && categoryId != null && minPrice != null && maxPrice != null) {
            result = productRepository.findByStockGreaterThanAndNameContainingIgnoreCaseAndCategoryIdAndPriceBetween(
                0, search, categoryId, minPrice, maxPrice, pageable);
        }
        // 2) Search + catégorie + prix
        else if (search != null && categoryId != null && minPrice != null && maxPrice != null) {
            result = productRepository.findByNameContainingIgnoreCaseAndCategoryIdAndPriceBetween(
                search, categoryId, minPrice, maxPrice, pageable);
        }

        // 3) Search + catégorie + stock
        else if (stockFilter && search != null && categoryId != null) {
            result = productRepository.findByStockGreaterThanAndNameContainingIgnoreCaseAndCategoryId(
                0, search, categoryId, pageable);
        }
        // 4) Search + catégorie
        else if (search != null && categoryId != null) {
            result = productRepository.findByNameContainingIgnoreCaseAndCategoryId(
                search, categoryId, pageable);
        }

        // 5) Search + prix + stock
        else if (stockFilter && search != null && minPrice != null && maxPrice != null) {
            result = productRepository.findByStockGreaterThanAndNameContainingIgnoreCaseAndPriceBetween(
                0, search, minPrice, maxPrice, pageable);
        }
        // 6) Search + prix
        else if (search != null && minPrice != null && maxPrice != null) {
            result = productRepository.findByNameContainingIgnoreCaseAndPriceBetween(
                search, minPrice, maxPrice, pageable);
        }

        // 7) Search + stock
        else if (stockFilter && search != null) {
            result = productRepository.findByStockGreaterThanAndNameContainingIgnoreCase(
                0, search, pageable);
        }
        // 8) Search seul
        else if (search != null) {
            result = productRepository.findByNameContainingIgnoreCase(search, pageable);
        }

        // 9) Catégorie + prix + stock
        else if (stockFilter && categoryId != null && minPrice != null && maxPrice != null) {
            result = productRepository.findByStockGreaterThanAndCategoryIdAndPriceBetween(
                0, categoryId, minPrice, maxPrice, pageable);
        }
        // 10) Catégorie + prix
        else if (categoryId != null && minPrice != null && maxPrice != null) {
            result = productRepository.findByCategoryIdAndPriceBetween(
                categoryId, minPrice, maxPrice, pageable);
        }

        // 11) Catégorie + stock
        else if (stockFilter && categoryId != null) {
            result = productRepository.findByStockGreaterThanAndCategoryId(
                0, categoryId, pageable);
        }
        // 12) Catégorie seule
        else if (categoryId != null) {
            result = productRepository.findByCategoryId(categoryId, pageable);
        }

        // 13) Prix + stock
        else if (stockFilter && minPrice != null && maxPrice != null) {
            result = productRepository.findByStockGreaterThanAndPriceBetween(
                0, minPrice, maxPrice, pageable);
        }
        // 14) Prix seul
        else if (minPrice != null && maxPrice != null) {
            result = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);
        }

        // 15) Stock seul
        else if (stockFilter) {
            result = productRepository.findByStockGreaterThan(0, pageable);
        }

        // 16) Aucun filtre
        else {
            result = productRepository.findAll(pageable);
        }


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
