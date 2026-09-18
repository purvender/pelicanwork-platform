package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.CategoryRequest;
import com.pelicanwork.pelicanwork_backend.dto.CategoryResponse;
import com.pelicanwork.pelicanwork_backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * POST /api/categories
     * Create a new category
     */
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryRequest request) {
        try {
            System.out.println("Creating category: " + request.getName());

            CategoryResponse response = categoryService.createCategory(request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Category created successfully");
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
            System.err.println("Error creating category: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create category: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/categories
     * Get all categories
     */
    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        try {
            System.out.println("Fetching all categories");

            List<CategoryResponse> categories = categoryService.getAllCategories();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", categories.size());
            response.put("data", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching categories: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch categories: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/categories/top-level
     * Get top-level categories (no parent)
     */
    @GetMapping("/top-level")
    public ResponseEntity<?> getTopLevelCategories() {
        try {
            System.out.println("Fetching top-level categories");

            List<CategoryResponse> categories = categoryService.getTopLevelCategories();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", categories.size());
            response.put("data", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching categories: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch categories: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/categories/{id}
     * Get category by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        try {
            System.out.println("Fetching category with id: " + id);

            CategoryResponse response = categoryService.getCategoryById(id);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error fetching category: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch category: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/categories/{id}/subcategories
     * Get subcategories by parent ID
     */
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<?> getSubcategories(@PathVariable Long id) {
        try {
            System.out.println("Fetching subcategories for parent: " + id);

            List<CategoryResponse> categories = categoryService.getSubcategories(id);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", categories.size());
            response.put("data", categories);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("Error fetching subcategories: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch subcategories: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PUT /api/categories/{id}
     * Update category
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                             @Valid @RequestBody CategoryRequest request) {
        try {
            System.out.println("Updating category with id: " + id);

            CategoryResponse response = categoryService.updateCategory(id, request);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Category updated successfully");
            result.put("data", response);

            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating category: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update category: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * DELETE /api/categories/{id}
     * Delete category
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        try {
            System.out.println("Deleting category with id: " + id);

            categoryService.deleteCategory(id);

            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Category deleted successfully");

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error deleting category: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete category: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
