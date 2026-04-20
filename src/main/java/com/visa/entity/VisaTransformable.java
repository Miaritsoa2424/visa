package com.visa.entity;

import java.time.LocalDate;

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
        this.numero = numero;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }
}
