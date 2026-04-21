package com.visa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "type_statut_visa")
public class TypeStatutVisa {

    @Id
    @Column(length = 30)
    private String id;

    @Column(nullable = false, length = 50)
    private String libelle;

    @Column(nullable = false)
    private Integer rang;

    public TypeStatutVisa() {
    }

    public TypeStatutVisa(String id, String libelle, Integer rang) {
        this.id = id;
        this.libelle = libelle;
        this.rang = rang;
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

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }
}
