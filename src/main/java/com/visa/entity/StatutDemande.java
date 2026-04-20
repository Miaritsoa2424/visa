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
@Table(name = "statut_demande")
public class StatutDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_statut", nullable = false)
    private LocalDate dateStatut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_statut_demande_id", nullable = false)
    private TypeStatutDemande typeStatutDemande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    public StatutDemande() {
    }

    public StatutDemande(Integer id, LocalDate dateStatut, TypeStatutDemande typeStatutDemande, Demande demande) {
        this.id = id;
        this.dateStatut = dateStatut;
        this.typeStatutDemande = typeStatutDemande;
        this.demande = demande;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateStatut() {
        return dateStatut;
    }

    public void setDateStatut(LocalDate dateStatut) {
        this.dateStatut = dateStatut;
    }

    public TypeStatutDemande getTypeStatutDemande() {
        return typeStatutDemande;
    }

    public void setTypeStatutDemande(TypeStatutDemande typeStatutDemande) {
        this.typeStatutDemande = typeStatutDemande;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}
