package com.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.FichierUploade;

@Repository
public interface FichierUploadeRepository extends JpaRepository<FichierUploade, Integer> {
    List<FichierUploade> findByDossierProfessionnelId(Integer dossierProfessionnelId);
}
