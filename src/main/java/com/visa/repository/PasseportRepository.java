package com.visa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visa.entity.Passeport;

@Repository
public interface PasseportRepository extends JpaRepository<Passeport, Integer> {
    Optional<Passeport> findFirstByNumeroOrderByIdAsc(String numero);
}
