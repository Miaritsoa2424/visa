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
@Table(name = "visa_transformable")
public class VisaTransformable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String numero;

    @Column(name = "date_arrivee", nullable = false)
    private LocalDate dateArrivee;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @ManyToOne(optional = false)
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;

    public VisaTransformable() {
    }

    public VisaTransformable(Integer id, String numero, LocalDate dateArrivee, LocalDate dateExpiration, Personne personne) {
        this.id = id;
        this.numero = numero;
        this.dateArrivee = dateArrivee;
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
            throw new BusinessValidationException("Le numero du visa transformable est obligatoire quand cette section est renseignee.");
        }
        this.numero = numero.trim();
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        if (dateArrivee == null) {
            throw new BusinessValidationException("La date d'entree du visa transformable est obligatoire quand cette section est renseignee.");
        }
        if (dateArrivee.isAfter(LocalDate.now())) {
            throw new BusinessValidationException("La date d'entree ne doit pas etre dans le futur.");
        }
        this.dateArrivee = dateArrivee;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        if (dateExpiration == null) {
            throw new BusinessValidationException("La date d'expiration du visa transformable est obligatoire quand cette section est renseignee.");
        }
        if (!dateExpiration.isAfter(LocalDate.now())) {
            throw new BusinessValidationException("La date d'expiration doit etre dans le futur.");
        }
        if (this.dateArrivee != null && dateExpiration.isBefore(this.dateArrivee)) {
            throw new BusinessValidationException("La date d'expiration du visa transformable doit etre apres la date d'entree.");
        }
        this.dateExpiration = dateExpiration;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        if (personne == null) {
            throw new BusinessValidationException("Le visa transformable doit etre rattache a une personne.");
        }
        this.personne = personne;
    }
}
