package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.DossierProfessionnel;

@Repository
public interface DossierProfessionnelRepository extends JpaRepository<DossierProfessionnel, Integer> {
}
