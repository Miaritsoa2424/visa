package com.visa.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.dto.RechercheDemandeDTO;
import com.visa.entity.Demande;
import com.visa.entity.Passeport;
import com.visa.exception.BusinessValidationException;
import com.visa.repository.DemandeRepository;
import com.visa.repository.PasseportRepository;
import com.visa.repository.StatutDemandeRepository;

@Service
public class RechercheDemandeService {

    @Autowired
    private PasseportRepository passeportRepository;

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private StatutDemandeRepository statutDemandeRepository;

    /**
     * Recherche une demande par numéro de passeport ou par numéro de demande
     * 
     * @param recherche : numéro de passeport (string) ou numéro de demande (string ou ID numérique)
     * @return RechercheDemandeDTO avec la demande trouvée et les autres demandes du demandeur
     * @throws BusinessValidationException si la recherche est vide ou si aucun résultat n'est trouvé
     */
    public RechercheDemandeDTO rechercherDemande(String recherche) throws BusinessValidationException {
        if (recherche == null || recherche.trim().isEmpty()) {
            throw new BusinessValidationException("Le critère de recherche est obligatoire.");
        }

        recherche = recherche.trim();
        RechercheDemandeDTO resultat = null;

        // Essayer d'abord de chercher par numéro de passeport
        resultat = rechercherParNumeroPasseport(recherche);
        if (resultat != null) {
            return resultat;
        }

        // Si pas trouvé par passeport, essayer par numéro de demande (string)
        // Formats supportés: "123", "DEM-123", "demande-123".
        Integer demandeId = extraireIdDemandeDepuisNumero(recherche);
        if (demandeId != null) {
            resultat = rechercherParIdDemande(demandeId);
            if (resultat != null) {
                return resultat;
            }
        }

        // Aucun résultat trouvé
        throw new BusinessValidationException("Aucune demande ou passeport trouvé pour : " + recherche);
    }

    private Integer extraireIdDemandeDepuisNumero(String recherche) {
        String valeur = recherche.trim();

        try {
            return Integer.parseInt(valeur);
        } catch (NumberFormatException e) {
            // Continuer avec les formats préfixés
        }

        String upper = valeur.toUpperCase();
        if (upper.startsWith("DEM-")) {
            String suffixe = upper.substring(4).trim();
            if (!suffixe.isEmpty()) {
                try {
                    return Integer.parseInt(suffixe);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        if (upper.startsWith("DEMANDE-")) {
            String suffixe = upper.substring(8).trim();
            if (!suffixe.isEmpty()) {
                try {
                    return Integer.parseInt(suffixe);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Recherche une demande par son ID
     */
    private RechercheDemandeDTO rechercherParIdDemande(Integer demandeId) {
        Optional<Demande> optDemande = demandeRepository.findById(demandeId);
        if (optDemande.isEmpty()) {
            return null;
        }

        Demande demande = optDemande.get();
        RechercheDemandeDTO dto = construireDTOFromDemande(demande, true);

        // Charger les autres demandes du même propriétaire
        Integer personneId = demande.getPasseport().getPersonne().getId();
        List<Demande> autresDemandes = demandeRepository.findByPersonneIdOrderByDateDemandeDesc(personneId);
        autresDemandes.removeIf(d -> d.getId().equals(demandeId)); // Retirer la demande cherchée

        dto.setAutresDemandes(autresDemandes.stream()
                .map(d -> construireDTOFromDemande(d, false))
                .collect(Collectors.toList()));

        return dto;
    }

    /**
     * Recherche par numéro de passeport
     */
    private RechercheDemandeDTO rechercherParNumeroPasseport(String numeroPasseport) {
        Optional<Passeport> optPasseport = passeportRepository.findFirstByNumeroOrderByIdAsc(numeroPasseport);
        if (optPasseport.isEmpty()) {
            return null;
        }

        Passeport passeport = optPasseport.get();
        Integer personneId = passeport.getPersonne().getId();

        // Récupérer toutes les demandes de ce propriétaire
        List<Demande> demandes = demandeRepository.findByPersonneIdOrderByDateDemandeDesc(personneId);

        if (demandes.isEmpty()) {
            throw new BusinessValidationException(
                    "Aucune demande trouvée pour le propriétaire du passeport : " + numeroPasseport);
        }

        // La première demande (la plus récente) est retournée comme entrée principale,
        // mais sans mise en avant car la recherche est faite par passeport.
        Demande demandePrincipale = demandes.get(0);
        RechercheDemandeDTO dto = construireDTOFromDemande(demandePrincipale, false);

        // Les autres demandes
        List<RechercheDemandeDTO> autresDemandes = demandes.stream()
                .skip(1) // Sauter la première
                .map(d -> construireDTOFromDemande(d, false))
                .collect(Collectors.toList());

        dto.setAutresDemandes(autresDemandes);

        return dto;
    }

    /**
     * Construit un DTO à partir d'une demande
     */
    private RechercheDemandeDTO construireDTOFromDemande(Demande demande, boolean estLaDemandeCherchee) {
        RechercheDemandeDTO dto = new RechercheDemandeDTO();

        dto.setDemandeId(demande.getId());
        dto.setNumeroPasseport(demande.getPasseport().getNumero());
        dto.setNomPersonne(demande.getPasseport().getPersonne().getNom());
        dto.setPrenomPersonne(demande.getPasseport().getPersonne().getPrenom());
        dto.setDateNaissancePersonne(demande.getPasseport().getPersonne().getDateNaissance());
        dto.setDateDemande(demande.getDateDemande());
        dto.setTypeDemandeLibelle(demande.getTypeDemande().getLibelle());

        if (demande.getTypeVisa() != null) {
            dto.setTypeVisaLibelle(demande.getTypeVisa().getLibelle());
        }

        // Récupérer le statut initial (Créé)
        var statutOpt = statutDemandeRepository.findById(1); // ID 1 = Créé
        if (statutOpt.isPresent()) {
            dto.setStatutDemandeLibelle(statutOpt.get().getTypeStatutDemande().getLibelle());
        }

        dto.setEstLaDemandeCherchee(estLaDemandeCherchee);

        return dto;
    }
}
