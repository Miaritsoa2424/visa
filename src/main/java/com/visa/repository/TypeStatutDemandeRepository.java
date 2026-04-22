package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.TypeStatutDemande;

@Repository
public interface TypeStatutDemandeRepository extends JpaRepository<TypeStatutDemande, String> {
}