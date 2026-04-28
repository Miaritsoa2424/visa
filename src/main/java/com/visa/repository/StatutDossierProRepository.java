package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.StatutDossierPro;

@Repository
public interface StatutDossierProRepository extends JpaRepository<StatutDossierPro, Integer> {
}
