package com.visa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "dossier_pro_statut")
public class DossierProStatut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_statut_dossier_pro", nullable = false)
    private StatutDossierPro statutDossierPro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_dossier_pro", nullable = false)
    private DossierProfessionnel dossierProfessionnel;

    public DossierProStatut() {
    }

    public DossierProStatut(Integer id, StatutDossierPro statutDossierPro, DossierProfessionnel dossierProfessionnel) {
        this.id = id;
        this.statutDossierPro = statutDossierPro;
        this.dossierProfessionnel = dossierProfessionnel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatutDossierPro getStatutDossierPro() {
        return statutDossierPro;
    }

    public void setStatutDossierPro(StatutDossierPro statutDossierPro) {
        this.statutDossierPro = statutDossierPro;
    }

    public DossierProfessionnel getDossierProfessionnel() {
        return dossierProfessionnel;
    }

    public void setDossierProfessionnel(DossierProfessionnel dossierProfessionnel) {
        this.dossierProfessionnel = dossierProfessionnel;
    }
}
