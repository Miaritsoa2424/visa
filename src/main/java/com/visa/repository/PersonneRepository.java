package com.visa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visa.entity.Personne;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Integer> {
    Optional<Personne> findFirstByEmailOrderByIdAsc(String email);

    @Query(value = "SELECT pn FROM passeport p LEFT JOIN historique_passeport_visa hpv ON p.id = hpv.passeport_id JOIN personne pn ON p.personne_id = pn.id WHERE hpv.visa_id = :id", nativeQuery = true)
    Optional<Personne> findPersonneWithVisaById(@Param("id") Integer idVisa);
}
