package com.visa.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visa.entity.StatutDemande;

@Repository
public interface StatutDemandeRepository extends JpaRepository<StatutDemande, Integer> {

    @Query("""
        SELECT s
        FROM StatutDemande s
        WHERE s.demande.id = :demandeId
        ORDER BY s.dateStatut DESC, s.typeStatutDemande.id DESC
    """)
    public List<StatutDemande> findByDemandeIdOrderByDateAndId(
            @Param("demandeId") Integer demandeId,
            Pageable pageable
    );

}