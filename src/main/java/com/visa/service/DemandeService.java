package com.visa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visa.dto.CreateDemandeDTO;
import com.visa.entity.ChampFournir;
import com.visa.entity.Demande;
import com.visa.entity.DossierProfessionnel;
import com.visa.entity.Nationalite;
import com.visa.entity.Passeport;
import com.visa.entity.Personne;
import com.visa.entity.SituationFamiliale;
import com.visa.entity.TypeDemande;
import com.visa.entity.TypeVisa;
import com.visa.entity.VisaTransformable;
import com.visa.repository.ChampFournirRepository;
import com.visa.repository.DemandeRepository;
import com.visa.repository.DossierProfessionnelRepository;
import com.visa.repository.NationaliteRepository;
import com.visa.repository.PasseportRepository;
import com.visa.repository.PersonneRepository;
import com.visa.repository.SituationFamilialeRepository;
import com.visa.repository.TypeDemandeRepository;
import com.visa.repository.TypeVisaRepository;
import com.visa.repository.VisaTransformableRepository;

@Service
public class DemandeService {
    @Autowired
    private DemandeRepository demandeRepository;
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private PasseportRepository passeportRepository;
    @Autowired
    private VisaTransformableRepository visaTransformableRepository;
    @Autowired
    private DossierProfessionnelRepository dossierProfessionnelRepository;
    @Autowired
    private NationaliteRepository nationaliteRepository;
    @Autowired
    private SituationFamilialeRepository situationFamilialeRepository;
    @Autowired
    private TypeVisaRepository typeVisaRepository;
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    @Autowired
    private ChampFournirRepository champFournirRepository;

    public List<Demande> getDemandes(){
        return demandeRepository.findAll();
    }

    @Transactional
    public Demande createDemande(CreateDemandeDTO dto) {
        // 1️⃣ Créer Personne (étape 1: pas de FK)
        Nationalite nationalite = nationaliteRepository.findById(dto.nationalite)
                .orElseThrow(() -> new RuntimeException("Nationalité non trouvée: " + dto.nationalite));
        SituationFamiliale situationFamiliale = situationFamilialeRepository.findById(dto.situationFamiliale)
                .orElseThrow(() -> new RuntimeException("Situation familiale non trouvée: " + dto.situationFamiliale));

        Personne personne = new Personne();
        personne.setNom(dto.nom);
        personne.setPrenom(dto.prenom);
        personne.setNomJeuneFille(dto.nomJeuneFille);
        personne.setEmail(dto.email);
        personne.setDateNaissance(dto.dateNaissance);
        personne.setLieuNaissance(dto.lieuNaissance);
        personne.setAdresse(dto.adresse);
        personne.setTelephone(dto.telephone);
        personne.setNationalite(nationalite);
        personne.setSituationFamiliale(situationFamiliale);
        personne = personneRepository.save(personne);

        // 2️⃣ Créer Passeport (FK: personne_id)
        Passeport passeport = new Passeport();
        passeport.setNumero(dto.numeroPasseport);
        passeport.setDateExpiration(dto.dateExpirationPasseport);
        passeport.setPersonne(personne);
        passeport = passeportRepository.save(passeport);

        // 3️⃣ Créer VisaTransformable (FK: personne_id)
        VisaTransformable visaTransformable = new VisaTransformable();
        visaTransformable.setNumero(dto.numeroVisaTransformable);
        visaTransformable.setDateArrivee(dto.dateArrivee);
        visaTransformable.setDateExpiration(dto.dateExpirationVisaTransformable);
        visaTransformable.setPersonne(personne);
        visaTransformable = visaTransformableRepository.save(visaTransformable);

        // 4️⃣ Créer Demande (FK: passeport_id, type_visa_id, type_demande_id)
        TypeVisa typeVisa = typeVisaRepository.findById(dto.typeVisa)
                .orElseThrow(() -> new RuntimeException("Type visa non trouvé: " + dto.typeVisa));
        TypeDemande typeDemande = typeDemandeRepository.findById(dto.typeDemandeId)
                .orElseThrow(() -> new RuntimeException("Type demande non trouvé: " + dto.typeDemandeId));

        Demande demande = new Demande();
        demande.setDateDemande(dto.dateDemande);
        demande.setPasseport(passeport);
        demande.setTypeVisa(typeVisa);
        demande.setTypeDemande(typeDemande);
        demande = demandeRepository.save(demande);

        // 5️⃣ Créer DossierProfessionnel pour chaque champFournirId coché
        if (dto.champFournirIds != null && !dto.champFournirIds.isEmpty()) {
            for (Integer champFournirId : dto.champFournirIds) {
                ChampFournir champFournir = champFournirRepository.findById(champFournirId)
                        .orElseThrow(() -> new RuntimeException("Champ fournir non trouvé: " + champFournirId));

                DossierProfessionnel dossier = new DossierProfessionnel();
                dossier.setValeur("");
                dossier.setDemande(demande);
                dossier.setChampFournir(champFournir);
                dossierProfessionnelRepository.save(dossier);
            }
        }

        return demande;
    }

}
