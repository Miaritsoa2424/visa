package com.visa.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visa.dto.CreateDemandeDTO;
import com.visa.entity.ChampFournir;
import com.visa.entity.Demande;
import com.visa.entity.DossierProfessionnel;
import com.visa.entity.HistoriquePasseportVisa;
import com.visa.entity.Nationalite;
import com.visa.entity.Passeport;
import com.visa.entity.Personne;
import com.visa.entity.SituationFamiliale;
import com.visa.entity.StatutDemande;
import com.visa.entity.StatutVisa;
import com.visa.entity.TypeDemande;
import com.visa.entity.TypeStatutDemande;
import com.visa.entity.TypeVisa;
import com.visa.entity.Visa;
import com.visa.entity.VisaTransformable;
import com.visa.exception.BusinessValidationException;
import com.visa.repository.ChampFournirRepository;
import com.visa.repository.DemandeRepository;
import com.visa.repository.DossierProfessionnelRepository;
import com.visa.repository.HistoriquePasseportVisaRepository;
import com.visa.repository.NationaliteRepository;
import com.visa.repository.PasseportRepository;
import com.visa.repository.PaysRepository;
import com.visa.repository.PersonneRepository;
import com.visa.repository.SituationFamilialeRepository;
import com.visa.repository.StatutDemandeRepository;
import com.visa.repository.StatutVisaRepository;
import com.visa.repository.TypeDemandeRepository;
import com.visa.repository.TypeStatutDemandeRepository;
import com.visa.repository.TypeStatutVisaRepository;
import com.visa.repository.TypeVisaRepository;
import com.visa.repository.VisaRepository;
import com.visa.repository.VisaTransformableRepository;

@Service
public class DemandeService {
    private final PaysRepository paysRepository;
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
    private StatutDemandeRepository statutDemandeRepository;
    @Autowired
    private TypeVisaRepository typeVisaRepository;
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    @Autowired
    private TypeStatutDemandeRepository typeStatutDemandeRepository;
    @Autowired
    private ChampFournirRepository champFournirRepository;
    @Autowired
    private PasseportService passeportService;
    @Autowired
    private HistoriquePasseportVisaRepository historiquePasseportVisaRepository;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private TypeStatutVisaRepository typeStatutVisaRepository;
    @Autowired
    private StatutVisaRepository statutVisaRepository;

    DemandeService(PaysRepository paysRepository) {
        this.paysRepository = paysRepository;
    }

    public List<Demande> getDemandes() {
        return demandeRepository.findAll();
    }

    public Demande getDemandeById(Integer demandeId) {
        return demandeRepository.findById(demandeId)
                .orElseThrow(() -> new BusinessValidationException("Demande non trouvee: " + demandeId));
    }

    public Set<Integer> getSelectedChampFournirIds(Integer demandeId) {
        return dossierProfessionnelRepository.findByDemandeId(demandeId).stream()
                .map(dossier -> dossier.getChampFournir() == null ? null : dossier.getChampFournir().getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public List<DossierProfessionnel> getDossierProfessionnelByDemandeId(Integer demandeId) {
        return dossierProfessionnelRepository.findByDemandeId(demandeId);
    }

    public List<Map<String, Object>> getChampsFournirWithStatus(Integer demandeId) {
        Demande demande = getDemandeById(demandeId);
        if (demande.getTypeVisa() == null) {
            return new ArrayList<>();
        }

        List<ChampFournir> champsRequis = champFournirRepository.findByTypeVisaId(demande.getTypeVisa().getId());
        List<DossierProfessionnel> dossiersFournis = dossierProfessionnelRepository.findByDemandeId(demandeId);
        
        Set<Integer> champFournirIdsFournis = dossiersFournis.stream()
                .map(d -> d.getChampFournir().getId())
                .collect(Collectors.toSet());

        List<Map<String, Object>> result = new ArrayList<>();
        for (ChampFournir champ : champsRequis) {
            Map<String, Object> champMap = new HashMap<>();
            champMap.put("id", champ.getId());
            champMap.put("libelle", champ.getLibelle());
            champMap.put("typeDonnee", champ.getTypeDonnee());
            champMap.put("isFourni", champFournirIdsFournis.contains(champ.getId()));
            
            DossierProfessionnel dossier = dossiersFournis.stream()
                    .filter(d -> d.getChampFournir().getId().equals(champ.getId()))
                    .findFirst()
                    .orElse(null);
            champMap.put("valeur", dossier != null ? dossier.getValeur() : null);
            
            result.add(champMap);
        }
        return result;
    }

    public VisaTransformable getVisaTransformableByPersonneId(Integer personneId) {
        return visaTransformableRepository.findFirstByPersonneIdOrderByIdAsc(personneId).orElse(null);
    }

    public boolean canEditDemandeByTypeStatutDemande(Integer demandeId) {
        StatutDemande statutDemande = statutDemandeRepository
                .findFirstByDemandeIdOrderByDateStatutDescIdDesc(demandeId)
                .orElse(null);
        if (statutDemande == null || statutDemande.getTypeStatutDemande() == null) {
            return false;
        }

        return "1".equals(statutDemande.getTypeStatutDemande().getId());
    }

    public boolean visaExistsByNumero(String numeroVisa) {
        String numeroNormalise = normalize(numeroVisa);
        if (numeroNormalise == null) {
            return false;
        }

        return visaRepository.findFirstByNumeroOrderByIdAsc(numeroNormalise).isPresent();
    }

    @Transactional(rollbackFor = Exception.class)
    public Demande createDemande(CreateDemandeDTO dto) {
        try {
            validateRequiredFields(dto);

            Nationalite nationalite = nationaliteRepository.findById(dto.getNationalite())
                    .orElseThrow(
                            () -> new BusinessValidationException("Nationalite non trouvee: " + dto.getNationalite()));
            SituationFamiliale situationFamiliale = situationFamilialeRepository.findById(dto.getSituationFamiliale())
                    .orElseThrow(() -> new BusinessValidationException(
                            "Situation familiale non trouvee: " + dto.getSituationFamiliale()));

            Personne personne = findOrCreatePersonne(dto, nationalite, situationFamiliale);
            Passeport passeport = findOrCreatePasseport(dto, personne);
            findOrCreateVisaTransformableIfProvided(dto, personne);

            TypeVisa typeVisa = typeVisaRepository.findById(dto.getTypeVisa())
                    .orElseThrow(() -> new BusinessValidationException("Type visa non trouve: " + dto.getTypeVisa()));
            TypeDemande typeDemande = typeDemandeRepository.findById(dto.getTypeDemandeId())
                    .orElseThrow(() -> new BusinessValidationException(
                            "Type demande non trouve: " + dto.getTypeDemandeId()));

            Demande demande = new Demande();
            demande.setDateDemande(dto.getDateDemande());
            demande.setPasseport(passeport);
            demande.setTypeVisa(typeVisa);
            demande.setTypeDemande(typeDemande);
            demande = demandeRepository.save(demande);

            createInitialStatutDemande(demande);

            saveDossierProfessionnels(dto, demande);

            return demande;
        } catch (BusinessValidationException exception) {
            throw exception;
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new BusinessValidationException(
                    "Plusieurs enregistrements identiques existent deja en base pour cette valeur. Merci de contacter l'administration pour nettoyer les doublons.");
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessValidationException(
                    "Violation de contrainte en base de donnees: " + exception.getMostSpecificCause().getMessage());
        } catch (RuntimeException exception) {
            throw new BusinessValidationException(
                    "Erreur metier lors de la creation de la demande: " + exception.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Demande updateDemande(Integer demandeId, CreateDemandeDTO dto) {
        try {
            validateRequiredFields(dto);

            if (!canEditDemandeByTypeStatutDemande(demandeId)) {
                throw new BusinessValidationException(
                        "Modification interdite: le type_statut_demande doit etre egal a 1.");
            }

            Demande demande = demandeRepository.findById(demandeId)
                    .orElseThrow(() -> new BusinessValidationException("Demande non trouvee: " + demandeId));

            Passeport passeport = demande.getPasseport();
            if (passeport == null || passeport.getPersonne() == null) {
                throw new BusinessValidationException("La demande ne contient pas de passeport/personne a modifier.");
            }

            Personne personne = passeport.getPersonne();

            Nationalite nationalite = nationaliteRepository.findById(dto.getNationalite())
                    .orElseThrow(
                            () -> new BusinessValidationException("Nationalite non trouvee: " + dto.getNationalite()));
            SituationFamiliale situationFamiliale = situationFamilialeRepository.findById(dto.getSituationFamiliale())
                    .orElseThrow(() -> new BusinessValidationException(
                            "Situation familiale non trouvee: " + dto.getSituationFamiliale()));

            personne.setNom(dto.getNom());
            personne.setPrenom(dto.getPrenom());
            personne.setNomJeuneFille(dto.getNomJeuneFille());
            personne.setEmail(normalize(dto.getEmail()));
            personne.setDateNaissance(dto.getDateNaissance());
            personne.setLieuNaissance(dto.getLieuNaissance());
            personne.setAdresse(dto.getAdresse());
            personne.setTelephone(dto.getTelephone());
            personne.setNationalite(nationalite);
            personne.setSituationFamiliale(situationFamiliale);
            personne = personneRepository.save(personne);

            String numeroPasseport = normalize(dto.getNumeroPasseport());
            Passeport passeportByNumero = passeportRepository.findFirstByNumeroOrderByIdAsc(numeroPasseport)
                    .orElse(null);
            if (passeportByNumero != null && !Objects.equals(passeportByNumero.getId(), passeport.getId())) {
                throw new BusinessValidationException("Le numero du passeport existe deja pour une autre personne.");
            }

            passeport.setNumero(numeroPasseport);
            passeport.setDateExpiration(dto.getDateExpirationPasseport());
            passeport.setPersonne(personne);
            passeport = passeportRepository.save(passeport);

            VisaTransformable visaTransformable = visaTransformableRepository
                    .findFirstByPersonneIdOrderByIdAsc(personne.getId())
                    .orElse(new VisaTransformable());

            String numeroVisaTransformable = normalize(dto.getNumeroVisaTransformable());
            VisaTransformable visaByNumero = visaTransformableRepository
                    .findFirstByNumeroOrderByIdAsc(numeroVisaTransformable)
                    .orElse(null);
            if (visaByNumero != null
                    && visaTransformable.getId() != null
                    && !Objects.equals(visaByNumero.getId(), visaTransformable.getId())) {
                throw new BusinessValidationException(
                        "Le numero du visa transformable existe deja pour une autre personne.");
            }
            if (visaByNumero != null && visaTransformable.getId() == null) {
                throw new BusinessValidationException(
                        "Le numero du visa transformable existe deja pour une autre personne.");
            }

            visaTransformable.setNumero(numeroVisaTransformable);
            visaTransformable.setDateArrivee(dto.getDateArrivee());
            visaTransformable.setDateExpiration(dto.getDateExpirationVisaTransformable());
            visaTransformable.setPersonne(personne);
            visaTransformableRepository.save(visaTransformable);

            TypeVisa typeVisa = typeVisaRepository.findById(dto.getTypeVisa())
                    .orElseThrow(() -> new BusinessValidationException("Type visa non trouve: " + dto.getTypeVisa()));
            TypeDemande typeDemande = typeDemandeRepository.findById(dto.getTypeDemandeId())
                    .orElseThrow(() -> new BusinessValidationException(
                            "Type demande non trouve: " + dto.getTypeDemandeId()));

            demande.setDateDemande(dto.getDateDemande());
            demande.setPasseport(passeport);
            demande.setTypeVisa(typeVisa);
            demande.setTypeDemande(typeDemande);
            demande = demandeRepository.save(demande);

            dossierProfessionnelRepository.deleteByDemandeId(demandeId);
            saveDossierProfessionnels(dto, demande);

            return demande;
        } catch (BusinessValidationException exception) {
            throw exception;
        } catch (IncorrectResultSizeDataAccessException exception) {
            throw new BusinessValidationException(
                    "Plusieurs enregistrements identiques existent deja en base pour cette valeur. Merci de contacter l'administration pour nettoyer les doublons.");
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessValidationException(
                    "Violation de contrainte en base de donnees: " + exception.getMostSpecificCause().getMessage());
        } catch (RuntimeException exception) {
            throw new BusinessValidationException(
                    "Erreur metier lors de la modification de la demande: " + exception.getMessage());
        }
    }

    private void validateRequiredFields(CreateDemandeDTO dto) {
        if (dto == null) {
            throw new BusinessValidationException("La requete de creation est invalide.");
        }
        if (dto.getNom() == null || dto.getNom().isBlank()) {
            throw new BusinessValidationException("Le champ nom est obligatoire.");
        }
        if (dto.getDateNaissance() == null) {
            throw new BusinessValidationException("Le champ date de naissance est obligatoire.");
        }
        if (dto.getAdresse() == null || dto.getAdresse().isBlank()) {
            throw new BusinessValidationException("Le champ adresse est obligatoire.");
        }
        if (dto.getTelephone() == null || dto.getTelephone().isBlank()) {
            throw new BusinessValidationException("Le champ telephone est obligatoire.");
        }
        if (dto.getNationalite() == null) {
            throw new BusinessValidationException("Le champ nationalite est obligatoire.");
        }
        if (dto.getNumeroPasseport() == null || dto.getNumeroPasseport().isBlank()) {
            throw new BusinessValidationException("Le numero du passeport est obligatoire.");
        }
        if (dto.getDateExpirationPasseport() == null) {
            throw new BusinessValidationException("La date d'expiration du passeport est obligatoire.");
        }
        if (dto.getSituationFamiliale() == null) {
            throw new BusinessValidationException("La situation familiale est obligatoire.");
        }
        if (dto.getTypeVisa() == null) {
            throw new BusinessValidationException("Le type de visa est obligatoire.");
        }
        if (dto.getTypeDemandeId() == null) {
            throw new BusinessValidationException("Le type de demande est obligatoire.");
        }
        if (dto.getDateDemande() == null) {
            throw new BusinessValidationException("La date de demande est obligatoire.");
        }
        if (dto.getNumeroVisaTransformable() == null || dto.getNumeroVisaTransformable().isBlank()) {
            throw new BusinessValidationException("Le numero du visa transformable est obligatoire.");
        }
        if (dto.getDateArrivee() == null) {
            throw new BusinessValidationException("La date d'entree du visa transformable est obligatoire.");
        }
        if (dto.getDateExpirationVisaTransformable() == null) {
            throw new BusinessValidationException("La date d'expiration du visa transformable est obligatoire.");
        }
    }

    private Personne findOrCreatePersonne(CreateDemandeDTO dto, Nationalite nationalite,
            SituationFamiliale situationFamiliale) {
        String email = normalize(dto.getEmail());
        Personne existingPersonne = null;

        if (email != null) {
            existingPersonne = personneRepository.findFirstByEmailOrderByIdAsc(email).orElse(null);
        }

        if (existingPersonne == null) {
            existingPersonne = findExistingPersonneByIdentity(dto, nationalite, situationFamiliale);
        }

        Personne personne = existingPersonne == null ? new Personne() : existingPersonne;
        personne.setNom(dto.getNom());
        personne.setPrenom(dto.getPrenom());
        personne.setNomJeuneFille(dto.getNomJeuneFille());
        personne.setEmail(email);
        personne.setDateNaissance(dto.getDateNaissance());
        personne.setLieuNaissance(dto.getLieuNaissance());
        personne.setAdresse(dto.getAdresse());
        personne.setTelephone(dto.getTelephone());
        personne.setNationalite(nationalite);
        personne.setSituationFamiliale(situationFamiliale);

        return personneRepository.save(personne);
    }

    private Passeport findOrCreatePasseport(CreateDemandeDTO dto, Personne personne) {
        String numeroPasseport = normalize(dto.getNumeroPasseport());
        Passeport passeport = passeportRepository.findFirstByNumeroOrderByIdAsc(numeroPasseport)
                .orElse(new Passeport());

        if (passeport.getId() != null && passeport.getPersonne() != null && passeport.getPersonne().getId() != null
                && !Objects.equals(passeport.getPersonne().getId(), personne.getId())) {
            throw new BusinessValidationException("Le numero du passeport existe deja pour une autre personne.");
        }

        passeport.setNumero(numeroPasseport);
        passeport.setDateExpiration(dto.getDateExpirationPasseport());
        passeport.setPersonne(personne);
        return passeportRepository.save(passeport);
    }

    private VisaTransformable findOrCreateVisaTransformableIfProvided(CreateDemandeDTO dto, Personne personne) {
        String numeroVisaTransformable = normalize(dto.getNumeroVisaTransformable());
        VisaTransformable visaTransformable = visaTransformableRepository
                .findFirstByNumeroOrderByIdAsc(numeroVisaTransformable)
                .orElse(new VisaTransformable());

        if (visaTransformable.getId() != null && visaTransformable.getPersonne() != null
                && visaTransformable.getPersonne().getId() != null
                && !Objects.equals(visaTransformable.getPersonne().getId(), personne.getId())) {
            throw new BusinessValidationException(
                    "Le numero du visa transformable existe deja pour une autre personne.");
        }

        visaTransformable.setNumero(numeroVisaTransformable);
        visaTransformable.setDateArrivee(dto.getDateArrivee());
        visaTransformable.setDateExpiration(dto.getDateExpirationVisaTransformable());
        visaTransformable.setPersonne(personne);
        return visaTransformableRepository.save(visaTransformable);
    }

    private Personne findExistingPersonneByIdentity(CreateDemandeDTO dto, Nationalite nationalite,
            SituationFamiliale situationFamiliale) {
        String nom = normalize(dto.getNom());
        String telephone = normalize(dto.getTelephone());
        String adresse = normalize(dto.getAdresse());

        return personneRepository.findAll().stream()
                .filter(personne -> Objects.equals(normalize(personne.getNom()), nom))
                .filter(personne -> Objects.equals(normalize(personne.getTelephone()), telephone))
                .filter(personne -> Objects.equals(normalize(personne.getAdresse()), adresse))
                .filter(personne -> personne.getNationalite() != null
                        && nationalite != null
                        && Objects.equals(personne.getNationalite().getId(), nationalite.getId()))
                .filter(personne -> personne.getSituationFamiliale() != null
                        && situationFamiliale != null
                        && Objects.equals(personne.getSituationFamiliale().getId(), situationFamiliale.getId()))
                .findFirst()
                .orElse(null);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void saveDossierProfessionnels(CreateDemandeDTO dto, Demande demande) {
        List<ChampFournir> champsFournir = champFournirRepository.findByTypeVisaId(dto.getTypeVisa());
        Set<Integer> champFournirSelectedIds = dto.getChampFournirIds() == null
                ? Set.of()
                : new HashSet<>(dto.getChampFournirIds());

        for (ChampFournir champFournir : champsFournir) {
            DossierProfessionnel dossier = new DossierProfessionnel();
            dossier.setDemande(demande);
            dossier.setChampFournir(champFournir);
            dossier.setValeur(String.valueOf(champFournirSelectedIds.contains(champFournir.getId())));
            dossierProfessionnelRepository.save(dossier);
        }
    }

    private StatutDemande createInitialStatutDemande(Demande demande) {
        TypeStatutDemande typeStatutDemande = typeStatutDemandeRepository.findById("1")
                .orElseThrow(() -> new BusinessValidationException("Type de statut de demande 'Cree' non trouve."));

        StatutDemande statutDemande = new StatutDemande();
        statutDemande.setDateStatut(demande.getDateDemande());
        statutDemande.setTypeStatutDemande(typeStatutDemande);
        statutDemande.setDemande(demande);
        StatutDemande statutDemandeSaved = statutDemandeRepository.save(statutDemande);
        return statutDemandeSaved;
    }

    public void tranfererVisa(String numeroVisa, String numeroPasseport, LocalDate dateExpiration, Demande demande) {
        Visa visa = visaRepository.findFirstByNumero(numeroVisa)
                .orElseThrow(() -> new BusinessValidationException("Visa non trouve avec le numero: " + numeroVisa));

        Passeport nouveauxPasseport = new Passeport();
        nouveauxPasseport.setNumero(numeroPasseport);

        nouveauxPasseport.setPersonne(visa.getPersonne());
        // nouveauxPasseport.setDateExpiration(LocalDate.of(dateExpiration.getYear(),
        // dateExpiration.getMonth(), dateExpiration.getDayOfMonth()));
        nouveauxPasseport.setDateExpiration(dateExpiration);

        transfererVisa(visa, nouveauxPasseport, demande);
    }

    @Transactional(rollbackFor = Exception.class)
    private void transfererVisa(Visa visa, Passeport nouveauxPasseport, Demande demande) {

        // Validation des champs
        nouveauxPasseport.validateRequiredFields();

        // Creation de passeport
        Passeport passeportSaved = passeportService.createPasseport(nouveauxPasseport);

        // Creation de la demande de transfert
        demande.setPasseport(passeportSaved);

        demande.validateRequiredFields();

        Demande demandeSaved = demandeRepository.save(demande);

        // Creation du statut de la demande
        createInitialStatutDemande(demandeSaved);

        HistoriquePasseportVisa historique = new HistoriquePasseportVisa();
        historique.setVisa(visa);
        historique.setPasseport(passeportSaved);
        historique.setDateHistorique(demandeSaved.getDateDemande());
        historiquePasseportVisaRepository.save(historique);

    }

    private Personne buildPersonneFromDto(CreateDemandeDTO dto) {
        Personne personneTarget = new Personne();
        personneTarget.setNom(dto.getNom());
        personneTarget.setPrenom(dto.getPrenom());
        personneTarget.setNomJeuneFille(dto.getNomJeuneFille());
        personneTarget.setEmail(normalize(dto.getEmail()));
        personneTarget.setDateNaissance(dto.getDateNaissance());
        personneTarget.setLieuNaissance(dto.getLieuNaissance());
        personneTarget.setAdresse(dto.getAdresse());
        personneTarget.setTelephone(dto.getTelephone());
        Nationalite nationaliteTarget = nationaliteRepository.findById(dto.getNationalite())
                .orElseThrow(() -> new BusinessValidationException("Nationalite non trouvee: " + dto.getNationalite()));
        personneTarget.setNationalite(nationaliteTarget);
        SituationFamiliale situationFamilialeTarget = situationFamilialeRepository.findById(dto.getSituationFamiliale())
                .orElseThrow(() -> new BusinessValidationException(
                        "Situation familiale non trouvee: " + dto.getSituationFamiliale()));
        personneTarget.setSituationFamiliale(situationFamilialeTarget);
        return personneTarget;
    }

    private Passeport buildPasseportFromDto(CreateDemandeDTO dto, Personne personne) {
        Passeport passeportTarget = new Passeport();
        passeportTarget.setNumero(normalize(dto.getNumeroPasseport()));
        passeportTarget.setDateExpiration(dto.getDateExpirationPasseport());
        passeportTarget.setPersonne(personne);
        return passeportTarget;
    }

    private Passeport buildAncienPasseportFromDto(CreateDemandeDTO dto, Personne personne) {
        Passeport passeport = new Passeport();
        passeport.setNumero(normalize(dto.getNumeroPasseportAncien()));
        passeport.setDateExpiration(dto.getDateExpirationPasseportAncien());
        passeport.setPersonne(personne);
        return passeport;
    }

    private Visa buildVisaFromDtoVisa(CreateDemandeDTO dto, Personne personne) {
        Visa visa = new Visa();
        visa.setNumero(normalize(dto.getNumeroVisaTransformable()));
        visa.setDateEntree(dto.getDateEntre());
        visa.setDateExpiration(dto.getDateExpiration());
        visa.setPersonne(personne);
        visa.setDateDelivrance(dto.getDateDelivrance());
        TypeVisa typeVisa = typeVisaRepository.findById(dto.getTypeVisa())
                .orElseThrow(() -> new BusinessValidationException("Type visa non trouve: " + dto.getTypeVisa()));
        visa.setTypeVisa(typeVisa);
        visa.setPaysEntree(paysRepository.findById(dto.getIdPaysEntre())
                .orElseThrow(() -> new BusinessValidationException("Pays non trouve")));
        return visa;
    }

    @Transactional(rollbackFor = BusinessValidationException.class)
    public void tranfererVisaWithoutData(CreateDemandeDTO createDemandeDTO) {
        try {
            createDemandeDTO.validateRequiredFieldsTransfertWithoutData();

            Personne personneTarget = buildPersonneFromDto(createDemandeDTO);

            personneTarget = personneRepository.save(personneTarget);

            Passeport ancienPasseport = buildAncienPasseportFromDto(createDemandeDTO, personneTarget);

            ancienPasseport = passeportRepository.save(ancienPasseport);

            Demande demande = new Demande();
            demande.setDateDemande(LocalDate.now());
            demande.setPasseport(ancienPasseport);
            TypeVisa typeVisa = typeVisaRepository.findById(createDemandeDTO.getIdTypeVisa())
                    .orElseThrow(() -> new BusinessValidationException(
                            "Type visa non trouve: " + createDemandeDTO.getIdTypeVisa()));
            demande.setTypeVisa(typeVisa);
            TypeDemande typeDemande = typeDemandeRepository.findById(1)
                    .orElseThrow(() -> new BusinessValidationException("Type demande non trouve: 1"));
            demande.setTypeDemande(typeDemande);
            demande = demandeRepository.save(demande);

            saveDossierProfessionnels(createDemandeDTO, demande);

            createInitialStatutDemande(demande);

            // Creation du statut de la demande
            StatutDemande statutDemande = new StatutDemande();
            TypeStatutDemande typeStatutDemande = typeStatutDemandeRepository.findById("3")
                    .orElseThrow(() -> new BusinessValidationException("Type de statut de demande"));
            statutDemande.setTypeStatutDemande(typeStatutDemande);
            statutDemande.setDemande(demande);
            statutDemande.setDateStatut(LocalDate.now());
            statutDemande = statutDemandeRepository.save(statutDemande);

            Visa visa = buildVisaFromDtoVisa(createDemandeDTO, personneTarget);
            visa.setDemande(demande);
            visa = visaRepository.save(visa);

            StatutVisa statutVisa = new StatutVisa();
            statutVisa.setDateStatut(LocalDate.now());
            statutVisa.setVisa(visa);
            statutVisa.setTypeStatutVisa(typeStatutVisaRepository.findById(1)
                    .orElseThrow(() -> new BusinessValidationException("Type de statut visa non valide")));
            statutVisa = statutVisaRepository.save(statutVisa);

            HistoriquePasseportVisa historiquePasseportVisa = new HistoriquePasseportVisa();
            historiquePasseportVisa.setDateHistorique(LocalDate.now());
            historiquePasseportVisa.setPasseport(ancienPasseport);
            historiquePasseportVisa.setVisa(visa);

            // A implementer
            // CarteResident carteResident = new CarteResident();
            // carteResident.set

            Passeport passeportTarget = buildPasseportFromDto(createDemandeDTO, personneTarget);
            passeportTarget = passeportRepository.save(passeportTarget);

            Demande demandeDuplicata = new Demande();
            demandeDuplicata.setDateDemande(LocalDate.now());
            demandeDuplicata.setPasseport(passeportTarget);
            demandeDuplicata.setTypeVisa(typeVisa);
            TypeDemande typeDemande2 = typeDemandeRepository.findById(3)
                    .orElseThrow(() -> new BusinessValidationException("Type demande non trouve: 3"));
            demandeDuplicata.setTypeDemande(typeDemande2);
            demandeRepository.save(demandeDuplicata);

            createInitialStatutDemande(demandeDuplicata);

            HistoriquePasseportVisa historiquePasseportVisa2 = new HistoriquePasseportVisa();
            historiquePasseportVisa2.setDateHistorique(LocalDate.now());
            historiquePasseportVisa2.setPasseport(passeportTarget);
            historiquePasseportVisa2.setVisa(visa);
            historiquePasseportVisaRepository.save(historiquePasseportVisa2);
        } catch (BusinessValidationException e) {
            throw e;
        }
    }
}
