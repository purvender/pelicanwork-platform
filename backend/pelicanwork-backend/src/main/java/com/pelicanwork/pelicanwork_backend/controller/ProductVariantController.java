package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.ProductVariantRequest;
import com.pelicanwork.pelicanwork_backend.dto.ProductVariantResponse;
import com.pelicanwork.pelicanwork_backend.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product-variants")
@CrossOrigin(origins = "*")
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    /**
     * POST /api/product-variants
     * Create a new variant
     */
    @PostMapping
    public ResponseEntity<?> createVariant(@Valid @RequestBody ProductVariantRequest request) {
        try {
            System.out.println("Creating variant for product: " + request.getProductId() + 
                             ", color: " + request.getColor());

            ProductVariantResponse response = productVariantService.createVariant(request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Variant created successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error creating variant: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create variant: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/product-variants/product/{productId}
     * Get all variants for a product
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getVariantsByProductId(@PathVariable Long productId) {
        try {
            System.out.println("Fetching variants for product: " + productId);

            List<ProductVariantResponse> variants = productVariantService.getVariantsByProductId(productId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", variants.size());
            response.put("data", variants);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching variants: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch variants: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/product-variants/{id}
     * Get variant by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getVariantById(@PathVariable Long id) {
        try {
            System.out.println("Fetching variant with id: " + id);

            ProductVariantResponse response = productVariantService.getVariantById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error fetching variant: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch variant: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PUT /api/product-variants/{id}
     * Update variant
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVariant(@PathVariable Long id,
                                            @Valid @RequestBody ProductVariantRequest request) {
        try {
            System.out.println("Updating variant with id: " + id);

            ProductVariantResponse response = productVariantService.updateVariant(id, request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Variant updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating variant: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update variant: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * DELETE /api/product-variants/{id}
     * Delete variant
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVariant(@PathVariable Long id) {
        try {
            System.out.println("Deleting variant with id: " + id);

            productVariantService.deleteVariant(id);

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Variant deleted successfully");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error deleting variant: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete variant: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
