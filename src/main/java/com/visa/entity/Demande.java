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
@Table(name = "demande")
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_demande", nullable = false)
    private LocalDate dateDemande;

    @ManyToOne(optional = false)
    @JoinColumn(name = "passeport_id", nullable = false)
    private Passeport passeport;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_visa_id", nullable = false)
    private TypeVisa typeVisa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_demande_id", nullable = false)
    private TypeDemande typeDemande;

    public Demande() {
    }

    public Demande(Integer id, LocalDate dateDemande, Passeport passeport, TypeVisa typeVisa, TypeDemande typeDemande) {
        this.id = id;
        this.dateDemande = dateDemande;
        this.passeport = passeport;
        this.typeVisa = typeVisa;
        this.typeDemande = typeDemande;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Passeport getPasseport() {
        return passeport;
    }

    public void setPasseport(Passeport passeport) {
        this.passeport = passeport;
    }

    public TypeVisa getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(TypeVisa typeVisa) {
        this.typeVisa = typeVisa;
    }

    public TypeDemande getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(TypeDemande typeDemande) {
        this.typeDemande = typeDemande;
    }

    public void validateRequiredFields() {
        if (dateDemande == null) {
            throw new BusinessValidationException("La date de la demande est obligatoire.");
        }
        if (passeport == null) {
            throw new BusinessValidationException("Le passeport est obligatoire.");
        }
        // if (typeVisa == null) {
        //     throw new BusinessValidationException("Le type de visa est obligatoire.");
        // }
        if (typeDemande == null) {
            throw new BusinessValidationException("Le type de demande est obligatoire.");
        }
    }
}
