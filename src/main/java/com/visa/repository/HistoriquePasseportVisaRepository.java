package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.HistoriquePasseportVisa;

@Repository
public interface HistoriquePasseportVisaRepository extends JpaRepository<HistoriquePasseportVisa, Integer> {
    
}
