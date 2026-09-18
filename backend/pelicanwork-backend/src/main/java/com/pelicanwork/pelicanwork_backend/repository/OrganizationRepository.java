package com.pelicanwork.pelicanwork_backend.repository;

import com.pelicanwork.pelicanwork_backend.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    
    // Find organizations by name (case insensitive, contains)
    List<Organization> findByNameContainingIgnoreCase(String keyword);
    
    // Check if organization exists by name
    boolean existsByName(String name);
}
