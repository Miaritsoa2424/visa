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
}
