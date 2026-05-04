package com.cedriccampagne.ecommerce.category;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Méthodes custom si besoins
    
}
