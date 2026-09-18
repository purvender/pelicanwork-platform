package com.pelicanwork.pelicanwork_backend.service;

import com.pelicanwork.pelicanwork_backend.dto.ProductRequest;
import com.pelicanwork.pelicanwork_backend.dto.ProductResponse;
import com.pelicanwork.pelicanwork_backend.entity.Category;
import com.pelicanwork.pelicanwork_backend.entity.Product;
import com.pelicanwork.pelicanwork_backend.repository.CategoryRepository;
import com.pelicanwork.pelicanwork_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Create a new product
     */
    public ProductResponse createProduct(ProductRequest request) {
        // Validate name
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Product name is required");
        }

        // Validate category exists
        Long categoryId = request.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        Product product = new Product();
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        product.setCategory(category);
        product.setBasePrice(request.getBasePrice());
        product.setSku(request.getSku());
        product.setStockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0);

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    /**
     * Get all products
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get active products only
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        List<Product> products = productRepository.findByIsActiveTrue();
        return products.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get products by category
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get product by ID
     */
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return mapToResponse(product);
    }

    /**
     * Get product entity by ID (internal use)
     */
    @Transactional(readOnly = true)
    public Product getProductEntityById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Search products by name
     */
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String keyword) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        return products.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Update product
     */
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Update fields
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            product.setName(request.getName().trim());
        }

        if (request.getDescription() != null) {
            product.setDescription(request.getDescription().trim());
        }

        // Update category if provided
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            product.setCategory(category);
        }

        if (request.getBasePrice() != null) {
            product.setBasePrice(request.getBasePrice());
        }

        if (request.getSku() != null) {
            product.setSku(request.getSku());
        }

        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    /**
     * Delete product
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Activate/deactivate product
     */
    public ProductResponse updateProductStatus(Long id, Boolean isActive) {
        // FIX: Use getProductEntityById instead of getProductById
        Product product = getProductEntityById(id);
        product.setIsActive(isActive);
        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    /**
     * Map Product entity to ProductResponse DTO
     */
    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        response.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        response.setBasePrice(product.getBasePrice());
        response.setSku(product.getSku());
        response.setStockQuantity(product.getStockQuantity());
        response.setIsActive(product.getIsActive());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        return response;
    }
}
