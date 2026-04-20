package com.visa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "type_statut_demande")
public class TypeStatutDemande {

    @Id
    @Column(length = 30)
    private String id;

    @Column(nullable = false, length = 50)
    private String libelle;

    public TypeStatutDemande() {
    }

    public TypeStatutDemande(String id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
