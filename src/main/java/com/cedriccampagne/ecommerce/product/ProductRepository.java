package com.cedriccampagne.ecommerce.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    //Méthodes custopn si besoins
    Page<Product> findByCategoryId(Long categoryId, Pageable page);
    Page<Product> findByPriceBetween(BigDecimal min, BigDecimal max, Pageable pageable);
    Page<Product> findByCategoryIdAndPriceBetween(
        Long categoryId,
        BigDecimal min,
        BigDecimal max,
        Pageable pageable
    );

    
}
