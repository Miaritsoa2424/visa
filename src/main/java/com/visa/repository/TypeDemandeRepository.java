package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.entity.TypeDemande;

public interface TypeDemandeRepository extends JpaRepository<TypeDemande, Integer> {
}