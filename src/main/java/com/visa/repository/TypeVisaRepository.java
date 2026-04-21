package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.TypeVisa;

@Repository
public interface TypeVisaRepository extends JpaRepository<TypeVisa, Integer> {
}
