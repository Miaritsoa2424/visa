package com.visa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.Personne;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Integer> {
    Optional<Personne> findFirstByEmailOrderByIdAsc(String email);
}
