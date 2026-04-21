package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.entity.Demande;

public interface DemandeRepository extends JpaRepository<Demande, Integer> {
}