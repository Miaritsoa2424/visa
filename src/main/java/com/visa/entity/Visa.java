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
@Table(name = "visa")
public class Visa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String numero;

    @Column(name = "date_entree")
    private LocalDate dateEntree;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "date_delivrance")
    private LocalDate dateDelivrance;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pays_entree_id", nullable = false)
    private Pays paysEntree;

    @ManyToOne(optional = false)
    @JoinColumn(name = "personne_id", nullable = false)
    private Personne personne;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_visa_id", nullable = false)
    private TypeVisa typeVisa;

    public Visa() {
    }

    public Visa(Integer id, String numero, LocalDate dateEntree, LocalDate dateExpiration, LocalDate dateDelivrance,
            Demande demande, Pays paysEntree, Personne personne, TypeVisa typeVisa) {
        this.id = id;
        this.numero = numero;
        this.dateEntree = dateEntree;
        this.dateExpiration = dateExpiration;
        this.dateDelivrance = dateDelivrance;
        this.demande = demande;
        this.paysEntree = paysEntree;
        this.personne = personne;
        this.typeVisa = typeVisa;
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

    public LocalDate getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDate dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public LocalDate getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(LocalDate dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Pays getPaysEntree() {
        return paysEntree;
    }

    public void setPaysEntree(Pays paysEntree) {
        this.paysEntree = paysEntree;
    }

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }

    public TypeVisa getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(TypeVisa typeVisa) {
        this.typeVisa = typeVisa;
    }
}
