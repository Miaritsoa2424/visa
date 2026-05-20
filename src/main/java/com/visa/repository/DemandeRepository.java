package com.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.visa.entity.Demande;

public interface DemandeRepository extends JpaRepository<Demande, Integer> {
    @Query("SELECT d FROM Demande d WHERE d.passeport.personne.id = :personneId ORDER BY d.dateDemande DESC")
    List<Demande> findByPersonneIdOrderByDateDemandeDesc(@Param("personneId") Integer personneId);
}