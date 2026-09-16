package com.pelicanwork.pelicanwork_backend.repository;


import com.pelicanwork.pelicanwork_backend.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}
