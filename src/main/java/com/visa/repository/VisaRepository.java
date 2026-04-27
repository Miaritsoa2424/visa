package com.visa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.visa.entity.Personne;
import com.visa.entity.Visa;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Integer> {
    Optional<Visa> findFirstByPersonneIdOrderByIdDesc(Integer personneId);
    Optional<Visa> findFirstByNumero(String numero);
    Optional<Visa> findFirstByNumeroOrderByIdAsc(String numero);

}
