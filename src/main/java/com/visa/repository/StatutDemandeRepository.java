package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.StatutDemande;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Integer> {
}