package com.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.DossierProStatut;

@Repository
public interface DossierProStatutRepository extends JpaRepository<DossierProStatut, Integer> {
    List<DossierProStatut> findByDossierProfessionnelId(Integer dossierProfessionnelId);

    List<DossierProStatut> findByDossierProfessionnelDemandeIdOrderByIdAsc(Integer demandeId);
}
