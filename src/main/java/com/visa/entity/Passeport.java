package com.visa.entity;

import java.time.LocalDate;

import com.visa.exception.BusinessValidationException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "passeport")
public class Passeport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String numero;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @ManyToOne(optional = false)
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;

    public Passeport() {
    }

    public Passeport(Integer id, String numero, LocalDate dateExpiration, Personne personne) {
        this.id = id;
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.personne = personne;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        if (numero == null || numero.isBlank()) {
            throw new BusinessValidationException("Le numero du passeport est obligatoire.");
        }
        this.numero = numero.trim();
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        if (dateExpiration == null) {
            throw new BusinessValidationException("La date d'expiration du passeport est obligatoire.");
        }
        if (!dateExpiration.isAfter(LocalDate.now())) {
            throw new BusinessValidationException("La date d'expiration du passeport doit etre dans le futur.");
        }
        this.dateExpiration = dateExpiration;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        if (personne == null) {
            throw new BusinessValidationException("Le passeport doit etre rattache a une personne.");
        }
        this.personne = personne;
    }
}
