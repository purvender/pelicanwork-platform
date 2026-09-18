package com.pelicanwork.pelicanwork_backend.service;

import com.pelicanwork.pelicanwork_backend.dto.CategoryRequest;
import com.pelicanwork.pelicanwork_backend.dto.CategoryResponse;
import com.pelicanwork.pelicanwork_backend.entity.Category;
import com.pelicanwork.pelicanwork_backend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Create a new category
     */
    public CategoryResponse createCategory(CategoryRequest request) {
        // Validate name
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Category name is required");
        }

        Category category = new Category();
        category.setName(request.getName().trim());
        category.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);

        // Set parent category if provided
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);

        return mapToResponse(saved);
    }

    /**
     * Get all categories
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get top-level categories (no parent)
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getTopLevelCategories() {
        List<Category> categories = categoryRepository.findByParentIsNull();
        return categories.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get subcategories by parent ID
     */
    @Transactional(readOnly = true)
    public List<CategoryResponse> getSubcategories(Long parentId) {
        List<Category> categories = categoryRepository.findByParentId(parentId);
        return categories.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get category by ID
     */
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return mapToResponse(category);
    }

    /**
     * Update category
     */
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Update fields
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            category.setName(request.getName().trim());
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription().trim());
        }

        // Update parent category if provided
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }

        Category updated = categoryRepository.save(category);
        return mapToResponse(updated);
    }

    /**
     * Delete category
     */
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    /**
     * Map Category entity to CategoryResponse DTO
     */
    private CategoryResponse mapToResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        return response;
    }
}
