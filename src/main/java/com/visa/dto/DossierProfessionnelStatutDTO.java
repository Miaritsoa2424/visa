package com.visa.dto;

import java.util.ArrayList;
import java.util.List;

public class DossierProfessionnelStatutDTO {

    private Integer dossierProfessionnelId;
    private String valeur;
    private Integer champFournirId;
    private String champFournirLibelle;
    private Integer dernierStatutId;
    private String dernierStatutLibelle;
    private List<StatutDossierProDTO> statuts = new ArrayList<>();

    public DossierProfessionnelStatutDTO() {
    }

    public Integer getDossierProfessionnelId() {
        return dossierProfessionnelId;
    }

    public void setDossierProfessionnelId(Integer dossierProfessionnelId) {
        this.dossierProfessionnelId = dossierProfessionnelId;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Integer getChampFournirId() {
        return champFournirId;
    }

    public void setChampFournirId(Integer champFournirId) {
        this.champFournirId = champFournirId;
    }

    public String getChampFournirLibelle() {
        return champFournirLibelle;
    }

    public void setChampFournirLibelle(String champFournirLibelle) {
        this.champFournirLibelle = champFournirLibelle;
    }

    public Integer getDernierStatutId() {
        return dernierStatutId;
    }

    public void setDernierStatutId(Integer dernierStatutId) {
        this.dernierStatutId = dernierStatutId;
    }

    public String getDernierStatutLibelle() {
        return dernierStatutLibelle;
    }

    public void setDernierStatutLibelle(String dernierStatutLibelle) {
        this.dernierStatutLibelle = dernierStatutLibelle;
    }

    public List<StatutDossierProDTO> getStatuts() {
        return statuts;
    }

    public void setStatuts(List<StatutDossierProDTO> statuts) {
        this.statuts = statuts;
    }
}