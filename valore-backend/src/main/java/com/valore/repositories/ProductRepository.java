package com.valore.repositories;

import com.valore.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByActiveTrue();
    List<Product> findByTitleContainingIgnoreCase(String title);
}
