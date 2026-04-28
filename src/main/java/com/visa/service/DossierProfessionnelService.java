package com.visa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.visa.dto.DossierProfessionnelStatutDTO;
import com.visa.dto.StatutDossierProDTO;
import com.visa.entity.DossierProStatut;
import com.visa.entity.DossierProfessionnel;
import com.visa.exception.BusinessValidationException;
import com.visa.repository.DossierProfessionnelRepository;
import com.visa.repository.DossierProStatutRepository;

@Service
public class DossierProfessionnelService {

    @Autowired
    private DossierProfessionnelRepository dossierProfessionnelRepository;

    @Autowired
    private DossierProStatutRepository dossierProStatutRepository;

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