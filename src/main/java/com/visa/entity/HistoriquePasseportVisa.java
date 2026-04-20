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
@Table(name = "historique_passeport_visa")
public class HistoriquePasseportVisa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_historique", nullable = false)
    private LocalDate dateHistorique;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visa_id", nullable = false)
    private Visa visa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "passeport_id", nullable = false)
    private Passeport passeport;

    public HistoriquePasseportVisa() {
    }

    public HistoriquePasseportVisa(Integer id, LocalDate dateHistorique, Visa visa, Passeport passeport) {
        this.id = id;
        this.dateHistorique = dateHistorique;
        this.visa = visa;
        this.passeport = passeport;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateHistorique() {
        return dateHistorique;
    }

    public void setDateHistorique(LocalDate dateHistorique) {
        this.dateHistorique = dateHistorique;
    }

    public Visa getVisa() {
        return visa;
    }

    public void setVisa(Visa visa) {
        this.visa = visa;
    }

    public Passeport getPasseport() {
        return passeport;
    }

    public void setPasseport(Passeport passeport) {
        this.passeport = passeport;
    }
}
