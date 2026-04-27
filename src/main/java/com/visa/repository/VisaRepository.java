package com.visa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.visa.entity.Visa;

public interface VisaRepository extends JpaRepository<Visa, Integer> {
    Optional<Visa> findFirstByNumeroOrderByIdAsc(String numero);
}