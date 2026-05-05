package com.cedriccampagne.ecommerce.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    //Méthodes custopn si besoins
    List<Product> findByCategoryId(Long categoryId);

}
