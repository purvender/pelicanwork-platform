package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.OrganizationRequest;
import com.pelicanwork.pelicanwork_backend.dto.OrganizationResponse;
import com.pelicanwork.pelicanwork_backend.service.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/organizations")
@CrossOrigin(origins = "*")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    /**
     * POST /api/organizations
     * Create a new organization
     */
    @PostMapping
    public ResponseEntity<?> createOrganization(@Valid @RequestBody OrganizationRequest request) {
        try {
            System.out.println("Creating organization: " + request.getName());
            
            OrganizationResponse response = organizationService.createOrganization(request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Organization created successfully");
            result.put("data", response);
            
            return ResponseEntity.ok(result);
            
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.err.println("Error creating organization: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/organizations
     * Get all organizations
     */
    @GetMapping
    public ResponseEntity<?> getAllOrganizations() {
        try {
            System.out.println("Fetching all organizations");
            
            List<OrganizationResponse> organizations = organizationService.getAllOrganizations();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", organizations.size());
            response.put("data", organizations);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error fetching organizations: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch organizations: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/organizations/{id}
     * Get organization by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizationById(@PathVariable Long id) {
        try {
            System.out.println("Fetching organization with id: " + id);
            
            OrganizationResponse response = organizationService.getOrganizationById(id);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("data", response);
            
            return ResponseEntity.ok(result);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error fetching organization: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * PUT /api/organizations/{id}
     * Update organization
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrganization(@PathVariable Long id, 
                                                 @Valid @RequestBody OrganizationRequest request) {
        try {
            System.out.println("Updating organization with id: " + id);
            
            OrganizationResponse response = organizationService.updateOrganization(id, request);
            
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "Organization updated successfully");
            result.put("data", response);
            
            return ResponseEntity.ok(result);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error updating organization: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * DELETE /api/organizations/{id}
     * Delete organization
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        try {
            System.out.println("Deleting organization with id: " + id);
            
            organizationService.deleteOrganization(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Organization deleted successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("Error deleting organization: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete organization: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/organizations/search?keyword=xyz
     * Search organizations by name
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchOrganizations(@RequestParam String keyword) {
        try {
            System.out.println("Searching organizations with keyword: " + keyword);
            
            List<OrganizationResponse> organizations = organizationService.searchOrganizations(keyword);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("count", organizations.size());
            response.put("data", organizations);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error searching organizations: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to search organizations: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
