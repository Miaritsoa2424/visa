package com.visa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.ChampFournir;

@Repository
public interface ChampFournirRepository extends JpaRepository<ChampFournir, Integer> {

    List<ChampFournir> findByTypeVisaId(Integer typeVisaId);
}
