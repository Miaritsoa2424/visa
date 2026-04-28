package com.visa.dto;

import java.time.LocalDate;
import java.util.List;

import com.visa.entity.Nationalite;
import com.visa.entity.Personne;
import com.visa.entity.SituationFamiliale;
import com.visa.exception.BusinessValidationException;

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

    // Ancien passeport
    public String numeroPasseportAncien;
    public LocalDate dateExpirationPasseportAncien;

    // Visa transformable
    public String numeroVisaTransformable;
    public LocalDate dateArrivee;
    public LocalDate dateExpirationVisaTransformable;

    // Visa ancien
    public String numeroVisaAncien;
    public LocalDate dateEntre;
    public LocalDate dateExpiration;
    public LocalDate dateDelivrance;
    public Integer idPaysEntre;
    public Integer idTypeVisa;

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

    public String getNumeroVisaAncien() {
        return numeroVisaAncien;
    }

    public void setNumeroVisaAncien(String numeroVisaAncien) {
        this.numeroVisaAncien = numeroVisaAncien;
    }

    public LocalDate getDateEntre() {
        return dateEntre;
    }

    public void setDateEntre(LocalDate dateEntre) {
        this.dateEntre = dateEntre;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public LocalDate getDateDelivrance() {
        return dateDelivrance;
    }

    public void setDateDelivrance(LocalDate dateDelivrance) {
        this.dateDelivrance = dateDelivrance;
    }

    public Integer getIdPaysEntre() {
        return idPaysEntre;
    }

    public void setIdPaysEntre(Integer idPaysEntre) {
        this.idPaysEntre = idPaysEntre;
    }

    public Integer getIdTypeVisa() {
        return idTypeVisa;
    }

    public void setIdTypeVisa(Integer idTypeVisa) {
        this.idTypeVisa = idTypeVisa;
    }

    public void validateRequiredFieldsTransfertWithoutData(){

        ///Etat civil
        if (this.getNom() == null || this.getNom().isBlank()) {
            throw new BusinessValidationException("Le champ nom est obligatoire.");
        }
        if (this.getDateNaissance() == null) {
            throw new BusinessValidationException("Le champ date de naissance est obligatoire.");
        }
        if (this.getAdresse() == null || this.getAdresse().isBlank()) {
            throw new BusinessValidationException("Le champ adresse est obligatoire.");
        }
        if (this.getTelephone() == null || this.getTelephone().isBlank()) {
            throw new BusinessValidationException("Le champ telephone est obligatoire.");
        }
        if (this.getNationalite() == null) {
            throw new BusinessValidationException("Le champ nationalite est obligatoire.");
        }
         if (this.getSituationFamiliale() == null) {
            throw new BusinessValidationException("La situation familiale est obligatoire.");
        }

        //Nouveau passeport
        if (this.getNumeroPasseport() == null || this.getNumeroPasseport().isBlank()) {
            throw new BusinessValidationException("Le numero du passeport est obligatoire.");
        }
        if (this.getDateExpirationPasseport() == null) {
            throw new BusinessValidationException("La date d'expiration du passeport est obligatoire.");
        }

        //Ancien passeport
        if (this.getNumeroPasseportAncien() == null || this.getNumeroPasseportAncien().isBlank()) {
            throw new BusinessValidationException("Le numero du passeport est obligatoire.");
        }
        if (this.getDateExpirationPasseportAncien() == null) {
            throw new BusinessValidationException("La date d'expiration du passeport est obligatoire.");
        }

        //Visa ancien
        if (this.getNumeroVisaAncien() == null || this.getNumeroVisaAncien().isBlank()) {
            throw new BusinessValidationException("Le numero du visa est obligatoire.");
        }
        if (this.getDateEntre() == null) {
            throw new BusinessValidationException("La date d'entrée est obligatoire.");
        }
        if (this.getDateExpiration() == null) {
            throw new BusinessValidationException("La date d'expiration du visa est obligatoire.");     
        }
        if (this.getDateDelivrance() == null) {
            throw new BusinessValidationException("La date de délivrance du visa est obligatoire.");
        }
        if (this.getIdPaysEntre() == null) {
            throw new BusinessValidationException("Le pays d'entrée est obligatoire.");
        }
        if (this.getIdTypeVisa() == null) {
            throw new BusinessValidationException("Le type de visa est obligatoire.");
        }


    }

    public String getNumeroPasseportAncien() {
        return numeroPasseportAncien;
    }

    public void setNumeroPasseportAncien(String numeroPasseportAncien) {
        this.numeroPasseportAncien = numeroPasseportAncien;
    }

    public LocalDate getDateExpirationPasseportAncien() {
        return dateExpirationPasseportAncien;
    }

    public void setDateExpirationPasseportAncien(LocalDate dateExpirationPasseportAncien) {
        this.dateExpirationPasseportAncien = dateExpirationPasseportAncien;
    }

    
}
