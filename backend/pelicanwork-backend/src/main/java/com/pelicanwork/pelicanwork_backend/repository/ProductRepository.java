package com.pelicanwork.pelicanwork_backend.repository;

import com.pelicanwork.pelicanwork_backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByIsActiveTrue();
    List<Product> findByNameContainingIgnoreCase(String keyword);
}
