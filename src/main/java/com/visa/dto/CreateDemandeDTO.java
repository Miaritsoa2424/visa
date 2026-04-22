package com.visa.dto;

import java.time.LocalDate;
import java.util.List;

public class CreateDemandeDTO {
    // Etat civil
    public String nom;
    public String prenom;
    public String nomJeuneFille;
    public String email;
    public LocalDate dateNaissance;
    public String lieuNaissance;
    public String adresse;
    public String telephone;
    public Integer nationalite;
    public Integer situationFamiliale;

    // Passeport
    public String numeroPasseport;
    public LocalDate dateExpirationPasseport;

    // Visa transformable
    public String numeroVisaTransformable;
    public LocalDate dateArrivee;
    public LocalDate dateExpirationVisaTransformable;

    // Demande
    public LocalDate dateDemande;
    public Integer typeVisa;
    public Integer typeDemandeId;

    // Champs à fournir (checkboxes)
    public List<Integer> champFournirIds;

    public CreateDemandeDTO() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNomJeuneFille() {
        return nomJeuneFille;
    }

    public void setNomJeuneFille(String nomJeuneFille) {
        this.nomJeuneFille = nomJeuneFille;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getNationalite() {
        return nationalite;
    }

    public void setNationalite(Integer nationalite) {
        this.nationalite = nationalite;
    }

    public Integer getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(Integer situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }

    public String getNumeroPasseport() {
        return numeroPasseport;
    }

    public void setNumeroPasseport(String numeroPasseport) {
        this.numeroPasseport = numeroPasseport;
    }

    public LocalDate getDateExpirationPasseport() {
        return dateExpirationPasseport;
    }

    public void setDateExpirationPasseport(LocalDate dateExpirationPasseport) {
        this.dateExpirationPasseport = dateExpirationPasseport;
    }

    public String getNumeroVisaTransformable() {
        return numeroVisaTransformable;
    }

    public void setNumeroVisaTransformable(String numeroVisaTransformable) {
        this.numeroVisaTransformable = numeroVisaTransformable;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) {
        this.dateArrivee = dateArrivee;
    }

    public LocalDate getDateExpirationVisaTransformable() {
        return dateExpirationVisaTransformable;
    }

    public void setDateExpirationVisaTransformable(LocalDate dateExpirationVisaTransformable) {
        this.dateExpirationVisaTransformable = dateExpirationVisaTransformable;
    }

    public LocalDate getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    public Integer getTypeVisa() {
        return typeVisa;
    }

    public void setTypeVisa(Integer typeVisa) {
        this.typeVisa = typeVisa;
    }

    public Integer getTypeDemandeId() {
        return typeDemandeId;
    }

    public void setTypeDemandeId(Integer typeDemandeId) {
        this.typeDemandeId = typeDemandeId;
    }

    public List<Integer> getChampFournirIds() {
        return champFournirIds;
    }

    public void setChampFournirIds(List<Integer> champFournirIds) {
        this.champFournirIds = champFournirIds;
    }
}
