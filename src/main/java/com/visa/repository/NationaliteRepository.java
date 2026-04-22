package com.visa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.Nationalite;

@Repository
public interface NationaliteRepository extends JpaRepository<Nationalite, Integer> {
}
