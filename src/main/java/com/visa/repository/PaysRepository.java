package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.Pays;

@Repository
public interface PaysRepository extends JpaRepository<Pays,Integer>{
    
}
