package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.ProductRequest;
import com.pelicanwork.pelicanwork_backend.dto.ProductResponse;
import com.pelicanwork.pelicanwork_backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * POST /api/products
     * Create a new product
     */
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest request) {
        try {
            System.out.println("Creating product: " + request.getName());

            ProductResponse response = productService.createProduct(request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Product created successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create product: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/products
     * Get all products
     */
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            System.out.println("Fetching all products");

            List<ProductResponse> products = productService.getAllProducts();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", products.size());
            response.put("data", products);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch products: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/products/active
     * Get active products only
     */
    @GetMapping("/active")
    public ResponseEntity<?> getActiveProducts() {
        try {
            System.out.println("Fetching active products");

            List<ProductResponse> products = productService.getActiveProducts();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", products.size());
            response.put("data", products);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch products: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/products/category/{categoryId}
     * Get products by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getProductsByCategory(@PathVariable Long categoryId) {
        try {
            System.out.println("Fetching products for category: " + categoryId);

            List<ProductResponse> products = productService.getProductsByCategory(categoryId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", products.size());
            response.put("data", products);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch products: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/products/{id}
     * Get product by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            System.out.println("Fetching product with id: " + id);

            ProductResponse response = productService.getProductById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error fetching product: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch product: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/products/search?keyword=chair
     * Search products by name
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String keyword) {
        try {
            System.out.println("Searching products with keyword: " + keyword);

            List<ProductResponse> products = productService.searchProducts(keyword);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", products.size());
            response.put("data", products);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error searching products: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to search products: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PUT /api/products/{id}
     * Update product
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                            @Valid @RequestBody ProductRequest request) {
        try {
            System.out.println("Updating product with id: " + id);

            ProductResponse response = productService.updateProduct(id, request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Product updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update product: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * DELETE /api/products/{id}
     * Delete product
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            System.out.println("Deleting product with id: " + id);

            productService.deleteProduct(id);

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Product deleted successfully");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error deleting product: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete product: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PATCH /api/products/{id}/status?isActive=true
     * Activate/deactivate product
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateProductStatus(@PathVariable Long id,
                                                  @RequestParam Boolean isActive) {
        try {
            System.out.println("Updating product status for id: " + id + " to " + isActive);

            ProductResponse response = productService.updateProductStatus(id, isActive);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Product status updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating product status: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update product status: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
