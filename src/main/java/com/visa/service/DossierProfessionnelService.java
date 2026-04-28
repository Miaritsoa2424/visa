package com.visa.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.visa.dto.DossierProfessionnelStatutDTO;
import com.visa.dto.StatutDossierProDTO;
import com.visa.entity.Demande;
import com.visa.entity.DossierProStatut;
import com.visa.entity.DossierProfessionnel;
import com.visa.entity.FichierUploade;
import com.visa.entity.StatutDemande;
import com.visa.entity.StatutDossierPro;
import com.visa.entity.TypeStatutDemande;
import com.visa.exception.BusinessValidationException;
import com.visa.repository.DossierProfessionnelRepository;
import com.visa.repository.DossierProStatutRepository;
import com.visa.repository.FichierUploadeRepository;
import com.visa.repository.StatutDemandeRepository;
import com.visa.repository.StatutDossierProRepository;
import com.visa.repository.TypeStatutDemandeRepository;

@Service
public class DossierProfessionnelService {

	private static final String TYPE_STATUT_DEMANDE_SCAN_TERMINE_ID = "2";
	private static final String STATUT_DOSSIER_PRO_SCANNE_LIBELLE = "Scanne";
	private static final Path DOSSIER_UPLOAD_PATH = Paths.get("assets", "dossierPro");

    @Autowired
    private DossierProfessionnelRepository dossierProfessionnelRepository;

    @Autowired
    private DossierProStatutRepository dossierProStatutRepository;

	@Autowired
	private FichierUploadeRepository fichierUploadeRepository;

	@Autowired
	private StatutDemandeRepository statutDemandeRepository;

	@Autowired
	private TypeStatutDemandeRepository typeStatutDemandeRepository;

	@Autowired
	private StatutDossierProRepository statutDossierProRepository;

	@Transactional(rollbackFor = Exception.class)
	public int uploaderFichiers(Integer dossierProfessionnelId, MultipartFile[] fichiers) {
		List<Path> fichiersCopies = new ArrayList<>();
		DossierProfessionnel dossierProfessionnel = dossierProfessionnelRepository.findById(dossierProfessionnelId)
				.orElseThrow(() -> new BusinessValidationException(
						"Dossier professionnel non trouve: " + dossierProfessionnelId));

		try {
			int fichiersEnregistres = enregistrerFichiersUploades(dossierProfessionnel, fichiers, fichiersCopies);
			return fichiersEnregistres;
		} catch (RuntimeException exception) {
			supprimerFichiersCopies(fichiersCopies);
			throw exception;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void terminerScanPourDemande(Integer demandeId) {
		if (demandeId == null) {
			throw new BusinessValidationException("L'identifiant de la demande est obligatoire.");
		}

		List<DossierProfessionnel> dossiersProfessionnels = dossierProfessionnelRepository.findByDemandeId(demandeId);
		if (dossiersProfessionnels.isEmpty()) {
			throw new BusinessValidationException("Aucun dossier professionnel trouve pour cette demande.");
		}

		Demande demande = dossiersProfessionnels.get(0).getDemande();
		if (demande == null || demande.getId() == null) {
			throw new BusinessValidationException("La demande liee aux dossiers professionnels est introuvable.");
		}

		// Ne modifier que le statut de la demande; les statuts des dossiers pro sont ajoutes lors de l'upload
		modifierStatutDemandeEnScanTermine(demande);
	}

	private int enregistrerFichiersUploades(DossierProfessionnel dossierProfessionnel, MultipartFile[] fichiers,
			List<Path> fichiersCopies) {
		if (fichiers == null || fichiers.length == 0) {
			throw new BusinessValidationException("Aucun fichier n'a ete fourni.");
		}

		try {
			Files.createDirectories(DOSSIER_UPLOAD_PATH);
		} catch (IOException exception) {
			throw new BusinessValidationException(
					"Impossible de creer le dossier de destination des fichiers: " + exception.getMessage());
		}

		int fichiersEnregistres = 0;
		for (MultipartFile fichier : fichiers) {
			if (fichier == null || fichier.isEmpty()) {
				continue;
			}

			String originalFilename = fichier.getOriginalFilename();
			String safeFilename = buildSafeFilename(dossierProfessionnel.getId(), originalFilename);
			Path targetPath = DOSSIER_UPLOAD_PATH.resolve(safeFilename);

			try (InputStream inputStream = fichier.getInputStream()) {
				Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException exception) {
				throw new BusinessValidationException(
						"Erreur lors de la copie du fichier '" + originalFilename + "': " + exception.getMessage());
			}
			fichiersCopies.add(targetPath);

			FichierUploade fichierUploade = new FichierUploade();
			fichierUploade.setDossierProfessionnel(dossierProfessionnel);
			fichierUploade.setValeur(targetPath.toString().replace("\\", "/"));

			try {
				fichierUploadeRepository.save(fichierUploade);
				fichiersEnregistres++;
			} catch (DataIntegrityViolationException exception) {
				throw new BusinessValidationException(
						"Erreur lors de l'enregistrement du fichier uploadé en base: " + exception.getMostSpecificCause().getMessage());
			}
		}

		if (fichiersEnregistres == 0) {
			throw new BusinessValidationException("Aucun fichier valide n'a ete fourni.");
		}

		// Marquer le dossier professionnel comme scanne maintenant que des fichiers ont ete enregistres
		insererStatutDossierProScannePour(dossierProfessionnel);

		return fichiersEnregistres;
	}

	private void insererStatutDossierProScannePour(DossierProfessionnel dossierProfessionnel) {
		StatutDossierPro statutDossierPro = statutDossierProRepository
				.findFirstByLibelleIgnoreCase(STATUT_DOSSIER_PRO_SCANNE_LIBELLE)
				.orElseThrow(() -> new BusinessValidationException(
						"Statut dossier professionnel 'Scanne' non trouve."));

		DossierProStatut dossierProStatut = new DossierProStatut();
		dossierProStatut.setDossierProfessionnel(dossierProfessionnel);
		dossierProStatut.setStatutDossierPro(statutDossierPro);
		dossierProStatutRepository.save(dossierProStatut);
	}

	private void supprimerFichiersCopies(List<Path> fichiersCopies) {
		for (int index = fichiersCopies.size() - 1; index >= 0; index--) {
			Path fichierCopie = fichiersCopies.get(index);
			try {
				Files.deleteIfExists(fichierCopie);
			} catch (IOException exception) {
				// Nettoyage best-effort: on ne masque pas l'erreur d'origine.
			}
		}
	}

	private String buildSafeFilename(Integer dossierProfessionnelId, String originalFilename) {
		String fallbackName = "fichier.bin";
		String baseName = originalFilename == null || originalFilename.isBlank()
				? fallbackName
				: Paths.get(originalFilename).getFileName().toString();
		String prefix = dossierProfessionnelId + "_" + UUID.randomUUID().toString().replace("-", "");
		return prefix + "_" + baseName;
	}

	private void modifierStatutDemandeEnScanTermine(Demande demande) {
		if (demande == null || demande.getId() == null) {
			throw new BusinessValidationException("La demande liee au dossier professionnel est introuvable.");
		}

		TypeStatutDemande typeStatutDemande = typeStatutDemandeRepository
				.findById(TYPE_STATUT_DEMANDE_SCAN_TERMINE_ID)
				.orElseThrow(() -> new BusinessValidationException(
						"Type de statut demande 'Scan termine' non trouve."));

		StatutDemande statutDemande = new StatutDemande();
		statutDemande.setDemande(demande);
		statutDemande.setTypeStatutDemande(typeStatutDemande);
		statutDemande.setDateStatut(java.time.LocalDate.now());
		statutDemandeRepository.save(statutDemande);
	}

	private void insererStatutDossierProScanne(List<DossierProfessionnel> dossiersProfessionnels) {
		StatutDossierPro statutDossierPro = statutDossierProRepository
				.findFirstByLibelleIgnoreCase(STATUT_DOSSIER_PRO_SCANNE_LIBELLE)
				.orElseThrow(() -> new BusinessValidationException(
						"Statut dossier professionnel 'Scanne' non trouve."));

		for (DossierProfessionnel dossierProfessionnel : dossiersProfessionnels) {
			DossierProStatut dossierProStatut = new DossierProStatut();
			dossierProStatut.setDossierProfessionnel(dossierProfessionnel);
			dossierProStatut.setStatutDossierPro(statutDossierPro);
			dossierProStatutRepository.save(dossierProStatut);
		}
	}

    @Transactional(readOnly = true)
    public List<DossierProfessionnelStatutDTO> getDossiersProfessionnelsAvecStatutsByDemandeId(Integer demandeId) {
	if (demandeId == null) {
	    throw new BusinessValidationException("L'identifiant de la demande est obligatoire.");
	}

	List<DossierProfessionnel> dossiersProfessionnels = dossierProfessionnelRepository.findByDemandeId(demandeId);
	if (dossiersProfessionnels.isEmpty()) {
	    return new ArrayList<>();
	}

	List<DossierProStatut> dossiersProStatuts = dossierProStatutRepository
		.findByDossierProfessionnelDemandeIdOrderByIdAsc(demandeId);

	Map<Integer, List<StatutDossierProDTO>> statutsParDossierId = dossiersProStatuts.stream()
		.filter(dossierProStatut -> dossierProStatut.getDossierProfessionnel() != null
			&& dossierProStatut.getDossierProfessionnel().getId() != null)
		.collect(Collectors.groupingBy(
			dossierProStatut -> dossierProStatut.getDossierProfessionnel().getId(),
			Collectors.mapping(this::toStatutDTO, Collectors.toList())));

	return dossiersProfessionnels.stream()
		.map(dossierProfessionnel -> toDossierProfessionnelStatutDTO(dossierProfessionnel,
			statutsParDossierId.getOrDefault(dossierProfessionnel.getId(), new ArrayList<>())))
		.collect(Collectors.toList());
    }

    private DossierProfessionnelStatutDTO toDossierProfessionnelStatutDTO(DossierProfessionnel dossierProfessionnel,
	    List<StatutDossierProDTO> statuts) {
	DossierProfessionnelStatutDTO dto = new DossierProfessionnelStatutDTO();
	dto.setDossierProfessionnelId(dossierProfessionnel.getId());
	dto.setValeur(dossierProfessionnel.getValeur());
	dto.setChampFournirId(dossierProfessionnel.getChampFournir() == null ? null
		: dossierProfessionnel.getChampFournir().getId());
	dto.setChampFournirLibelle(dossierProfessionnel.getChampFournir() == null ? null
		: dossierProfessionnel.getChampFournir().getLibelle());
	if (!statuts.isEmpty()) {
	    StatutDossierProDTO dernierStatut = statuts.get(statuts.size() - 1);
	    dto.setDernierStatutId(dernierStatut.getId());
	    dto.setDernierStatutLibelle(dernierStatut.getLibelle());
	}
	dto.setStatuts(statuts);
	return dto;
    }

    private StatutDossierProDTO toStatutDTO(DossierProStatut dossierProStatut) {
	StatutDossierProDTO dto = new StatutDossierProDTO();
	dto.setId(dossierProStatut.getStatutDossierPro() == null ? null : dossierProStatut.getStatutDossierPro().getId());
	dto.setLibelle(dossierProStatut.getStatutDossierPro() == null ? null
		: dossierProStatut.getStatutDossierPro().getLibelle());
	return dto;
    }
}