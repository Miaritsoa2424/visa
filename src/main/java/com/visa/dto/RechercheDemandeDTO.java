package com.visa.dto;

import java.time.LocalDate;
import java.util.List;

public class RechercheDemandeDTO {
    
    private Integer demandeId;
    private String numeroPasseport;
    private String nomPersonne;
    private String prenomPersonne;
    private LocalDate dateNaissancePersonne;
    private LocalDate dateDemande;
    private String typeDemandeLibelle;
    private String typeVisaLibelle;
    private String statutDemandeLibelle;
    private boolean estLaDemandeCherchee;
    
    private List<RechercheDemandeDTO> autresDemandes;
    
    public RechercheDemandeDTO() {
    }

    public RechercheDemandeDTO(Integer demandeId, String numeroPasseport, String nomPersonne,
            String prenomPersonne, LocalDate dateNaissancePersonne, LocalDate dateDemande,
            String typeDemandeLibelle, String typeVisaLibelle, String statutDemandeLibelle,
            boolean estLaDemandeCherchee) {
        this.demandeId = demandeId;
        this.numeroPasseport = numeroPasseport;
        this.nomPersonne = nomPersonne;
        this.prenomPersonne = prenomPersonne;
        this.dateNaissancePersonne = dateNaissancePersonne;
        this.dateDemande = dateDemande;
        this.typeDemandeLibelle = typeDemandeLibelle;
        this.typeVisaLibelle = typeVisaLibelle;
        this.statutDemandeLibelle = statutDemandeLibelle;
        this.estLaDemandeCherchee = estLaDemandeCherchee;
    }

    public Integer getDemandeId() {
        return demandeId;
    }

    public void setDemandeId(Integer demandeId) {
        this.demandeId = demandeId;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }

    public String getNomPersonne() {
        return nomPersonne;
    }

    public void setNomPersonne(String nomPersonne) {
        this.nomPersonne = nomPersonne;
    }

    public String getPrenomPersonne() {
        return prenomPersonne;
    }

    public void setPrenomPersonne(String prenomPersonne) {
        this.prenomPersonne = prenomPersonne;
    }

    public LocalDate getDateNaissancePersonne() {
        return dateNaissancePersonne;
    }

    public void setDateNaissancePersonne(LocalDate dateNaissancePersonne) {
        this.dateNaissancePersonne = dateNaissancePersonne;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public String getTypeDemandeLibelle() {
        return typeDemandeLibelle;
    }

    public void setTypeDemandeLibelle(String typeDemandeLibelle) {
        this.typeDemandeLibelle = typeDemandeLibelle;
    }

    public String getTypeVisaLibelle() {
        return typeVisaLibelle;
    }

    public void setTypeVisaLibelle(String typeVisaLibelle) {
        this.typeVisaLibelle = typeVisaLibelle;
    }

    public String getStatutDemandeLibelle() {
        return statutDemandeLibelle;
    }

    public void setStatutDemandeLibelle(String statutDemandeLibelle) {
        this.statutDemandeLibelle = statutDemandeLibelle;
    }

    public boolean isEstLaDemandeCherchee() {
        return estLaDemandeCherchee;
    }

    public void setEstLaDemandeCherchee(boolean estLaDemandeCherchee) {
        this.estLaDemandeCherchee = estLaDemandeCherchee;
    }

    public List<RechercheDemandeDTO> getAutresDemandes() {
        return autresDemandes;
    }

    public void setAutresDemandes(List<RechercheDemandeDTO> autresDemandes) {
        this.autresDemandes = autresDemandes;
    }
}
