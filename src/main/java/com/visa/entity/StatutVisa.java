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
@Table(name = "statut_visa")
public class StatutVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_statut", nullable = false)
    private LocalDate dateStatut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visa_id", nullable = false)
    private Visa visa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "type_statut_visa_id", nullable = false)
    private TypeStatutVisa typeStatutVisa;

    public StatutVisa() {
    }

    public StatutVisa(Integer id, LocalDate dateStatut, Visa visa, TypeStatutVisa typeStatutVisa) {
        this.id = id;
        this.dateStatut = dateStatut;
        this.visa = visa;
        this.typeStatutVisa = typeStatutVisa;
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

    public Visa getVisa() {
        return visa;
    }

    public void setVisa(Visa visa) {
        this.visa = visa;
    }

    public TypeStatutVisa getTypeStatutVisa() {
        return typeStatutVisa;
    }

    public void setTypeStatutVisa(TypeStatutVisa typeStatutVisa) {
        this.typeStatutVisa = typeStatutVisa;
    }
}
