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
@Table(name = "champ_fournir")
public class ChampFournir {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String libelle;

    @Column(name = "type_donnee", length = 30)
    private String typeDonnee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_visa_id", nullable = false)
    private TypeVisa typeVisa;

    public ChampFournir() {
    }

    public ChampFournir(Integer id, String libelle, String typeDonnee, TypeVisa typeVisa) {
        this.id = id;
        this.libelle = libelle;
        this.typeDonnee = typeDonnee;
        this.typeVisa = typeVisa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTypeDonnee() {
        return typeDonnee;
    }

    public void setTypeDonnee(String typeDonnee) {
        this.typeDonnee = typeDonnee;
    }

    public TypeVisa getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(TypeVisa typeVisa) {
        this.typeVisa = typeVisa;
    }
}
