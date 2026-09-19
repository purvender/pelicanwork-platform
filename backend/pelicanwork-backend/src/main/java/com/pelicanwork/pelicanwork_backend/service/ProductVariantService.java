package com.pelicanwork.pelicanwork_backend.service;

import com.pelicanwork.pelicanwork_backend.dto.ProductVariantRequest;
import com.pelicanwork.pelicanwork_backend.dto.ProductVariantResponse;
import com.pelicanwork.pelicanwork_backend.entity.Product;
import com.pelicanwork.pelicanwork_backend.entity.ProductVariant;
import com.pelicanwork.pelicanwork_backend.repository.ProductRepository;
import com.pelicanwork.pelicanwork_backend.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductVariantService {

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Create a new product variant
     */
    public ProductVariantResponse createVariant(ProductVariantRequest request) {
        // Validate product exists
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        ProductVariant variant = new ProductVariant();
        variant.setProduct(product);
        variant.setColor(request.getColor());
        variant.setSize(request.getSize());
        variant.setPrice(request.getPrice());
        variant.setStockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0);
        variant.setSku(request.getSku());

        ProductVariant saved = productVariantRepository.save(variant);

        return mapToResponse(saved);
    }

    /**
     * Get all variants for a product
     */
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getVariantsByProductId(Long productId) {
        List<ProductVariant> variants = productVariantRepository.findByProductId(productId);
        return variants.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get variant by ID
     */
    @Transactional(readOnly = true)
    public ProductVariantResponse getVariantById(Long id) {
        ProductVariant variant = productVariantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));
        return mapToResponse(variant);
    }

    /**
     * Update variant
     */
    public ProductVariantResponse updateVariant(Long id, ProductVariantRequest request) {
        ProductVariant variant = productVariantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));

        // Update fields
        if (request.getColor() != null) {
            variant.setColor(request.getColor());
        }

        if (request.getSize() != null) {
            variant.setSize(request.getSize());
        }

        if (request.getPrice() != null) {
            variant.setPrice(request.getPrice());
        }

        if (request.getStockQuantity() != null) {
            variant.setStockQuantity(request.getStockQuantity());
        }

        if (request.getSku() != null) {
            variant.setSku(request.getSku());
        }

        ProductVariant updated = productVariantRepository.save(variant);
        return mapToResponse(updated);
    }

    /**
     * Delete variant
     */
    public void deleteVariant(Long id) {
        if (!productVariantRepository.existsById(id)) {
            throw new RuntimeException("Variant not found with id: " + id);
        }
        productVariantRepository.deleteById(id);
    }

    /**
     * Map entity to response DTO
     */
    private ProductVariantResponse mapToResponse(ProductVariant variant) {
        ProductVariantResponse response = new ProductVariantResponse();
        response.setId(variant.getId());
        response.setProductId(variant.getProduct() != null ? variant.getProduct().getId() : null);
        response.setProductName(variant.getProduct() != null ? variant.getProduct().getName() : null);
        response.setColor(variant.getColor());
        response.setSize(variant.getSize());
        response.setPrice(variant.getPrice());
        response.setStockQuantity(variant.getStockQuantity());
        response.setSku(variant.getSku());
        response.setCreatedAt(variant.getCreatedAt());
        response.setUpdatedAt(variant.getUpdatedAt());
        return response;
    }
}
