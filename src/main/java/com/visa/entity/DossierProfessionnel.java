package com.visa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dossier_professionnel")
public class DossierProfessionnel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String valeur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "demande_id", nullable = false)
    private Demande demande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "champ_fournir_id", nullable = false)
    private ChampFournir champFournir;

    public DossierProfessionnel() {
    }

    public DossierProfessionnel(Integer id, String valeur, Demande demande, ChampFournir champFournir) {
        this.id = id;
        this.valeur = valeur;
        this.demande = demande;
        this.champFournir = champFournir;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public ChampFournir getChampFournir() {
        return champFournir;
    }

    public void setChampFournir(ChampFournir champFournir) {
        this.champFournir = champFournir;
    }
}
