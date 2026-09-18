package com.pelicanwork.pelicanwork_backend.service;

import com.pelicanwork.pelicanwork_backend.dto.OrganizationRequest;
import com.pelicanwork.pelicanwork_backend.dto.OrganizationResponse;
import com.pelicanwork.pelicanwork_backend.entity.Organization;
import com.pelicanwork.pelicanwork_backend.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    /**
     * Create a new organization
     */
    public OrganizationResponse createOrganization(OrganizationRequest request) {
        // Validate name is not empty
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new RuntimeException("Organization name is required");
        }

        Organization org = new Organization();
        org.setName(request.getName().trim());
        org.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        
        Organization saved = organizationRepository.save(org);
        
        return new OrganizationResponse(
            saved.getId(),
            saved.getName(),
            saved.getDescription(),
            saved.getCreatedAt(),
            saved.getUpdatedAt(),
            0  // Initial member count
        );
    }

    /**
     * Get all organizations
     */
    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        
        return organizations.stream()
            .map(org -> new OrganizationResponse(
                org.getId(),
                org.getName(),
                org.getDescription(),
                org.getCreatedAt(),
                org.getUpdatedAt(),
                0  // Will be populated later
            ))
            .collect(Collectors.toList());
    }

    /**
     * Get organization by ID
     */
    @Transactional(readOnly = true)
    public OrganizationResponse getOrganizationById(Long id) {
        Organization org = organizationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        
        return new OrganizationResponse(
            org.getId(),
            org.getName(),
            org.getDescription(),
            org.getCreatedAt(),
            org.getUpdatedAt(),
            0
        );
    }

    /**
     * Update organization
     */
    public OrganizationResponse updateOrganization(Long id, OrganizationRequest request) {
        Organization org = organizationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        
        // Update fields
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            org.setName(request.getName().trim());
        }
        
        if (request.getDescription() != null) {
            org.setDescription(request.getDescription().trim());
        }
        
        Organization updated = organizationRepository.save(org);
        
        return new OrganizationResponse(
            updated.getId(),
            updated.getName(),
            updated.getDescription(),
            updated.getCreatedAt(),
            updated.getUpdatedAt(),
            0
        );
    }

    /**
     * Delete organization
     */
    public void deleteOrganization(Long id) {
        if (!organizationRepository.existsById(id)) {
            throw new RuntimeException("Organization not found with id: " + id);
        }
        organizationRepository.deleteById(id);
    }

    /**
     * Search organizations by name
     */
    @Transactional(readOnly = true)
    public List<OrganizationResponse> searchOrganizations(String keyword) {
        List<Organization> organizations = organizationRepository.findByNameContainingIgnoreCase(keyword);
        
        return organizations.stream()
            .map(org -> new OrganizationResponse(
                org.getId(),
                org.getName(),
                org.getDescription(),
                org.getCreatedAt(),
                org.getUpdatedAt(),
                0
            ))
            .collect(Collectors.toList());
    }
}
