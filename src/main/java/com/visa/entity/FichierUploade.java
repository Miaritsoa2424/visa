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
@Table(name = "fichier_uploade")
public class FichierUploade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String valeur;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dossier_professionnel_id", nullable = false)
    private DossierProfessionnel dossierProfessionnel;

    public FichierUploade() {
    }

    public FichierUploade(Integer id, String valeur, DossierProfessionnel dossierProfessionnel) {
        this.id = id;
        this.valeur = valeur;
        this.dossierProfessionnel = dossierProfessionnel;
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

    public DossierProfessionnel getDossierProfessionnel() {
        return dossierProfessionnel;
    }

    public void setDossierProfessionnel(DossierProfessionnel dossierProfessionnel) {
        this.dossierProfessionnel = dossierProfessionnel;
    }
}
