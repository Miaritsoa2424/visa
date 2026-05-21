package com.visa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.entity.DossierProfessionnel;
import com.visa.entity.FichierUploade;
import com.visa.repository.DossierProfessionnelRepository;
import com.visa.repository.FichierUploadeRepository;

@Service
public class FichierUploadeService {

    @Autowired
    private DossierProfessionnelRepository dossierProfessionnelRepository;

    @Autowired
    private FichierUploadeRepository fichierUploadeRepository;

    /**
     * Retourne la liste des FichierUploade lies a une demande via les dossiers professionnels.
     */
    public List<FichierUploade> getFilesByDemandeId(Integer demandeId) {
        if (demandeId == null) {
            return new ArrayList<>();
        }

        List<DossierProfessionnel> dossiers = dossierProfessionnelRepository.findByDemandeId(demandeId);
        if (dossiers == null || dossiers.isEmpty()) {
            return new ArrayList<>();
        }

        List<FichierUploade> result = new ArrayList<>();
        for (DossierProfessionnel dossier : dossiers) {
            List<FichierUploade> fichiers = fichierUploadeRepository.findByDossierProfessionnelId(
                    dossier.getId());
            if (fichiers != null && !fichiers.isEmpty()) {
                result.addAll(fichiers);
            }
        }

        return result;
    }
}
