package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.TypeStatutVisa;

@Repository
public interface TypeStatutVisaRepository extends JpaRepository<TypeStatutVisa,Integer>{
    
}
