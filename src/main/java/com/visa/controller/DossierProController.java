package com.visa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.visa.dto.DossierProfessionnelStatutDTO;
import com.visa.service.DossierProfessionnelService;

@Controller
public class DossierProController {

    private static final Integer DEMANDE_ID_FIXE = 2;

    @Autowired
    private DossierProfessionnelService dossierProfessionnelService;

    @GetMapping("/dossier-pro")
    public String listDossiersProfessionnels(Model model) {
        List<DossierProfessionnelStatutDTO> dossiersProfessionnels = dossierProfessionnelService
                .getDossiersProfessionnelsAvecStatutsByDemandeId(DEMANDE_ID_FIXE);

        model.addAttribute("demandeId", DEMANDE_ID_FIXE);
        model.addAttribute("dossiersProfessionnels", dossiersProfessionnels);
        return renderPage(model, "Dossiers professionnels", "dossier-pro/dossiers-professionnels.jsp", "dossier-pro");
    }

    @PostMapping("/dossier-pro/scanner")
    public String scannerDossier(@RequestParam("dossierProfessionnelId") Integer dossierProfessionnelId,
            @RequestParam(value = "fichiers", required = false) MultipartFile[] fichiers,
            RedirectAttributes redirectAttributes) {
        int nbFichiersRecus = 0;
        if (fichiers != null) {
            for (MultipartFile fichier : fichiers) {
                if (fichier != null && !fichier.isEmpty()) {
                    nbFichiersRecus++;
                }
            }
        }

        redirectAttributes.addFlashAttribute("infoMessage",
                "Upload effectue pour le dossier professionnel #" + dossierProfessionnelId
                        + " (" + nbFichiersRecus + " fichier(s) recu(s)).");
        return "redirect:/dossier-pro";
    }

    private String renderPage(Model model, String pageTitle, String contentPage, String pageStyle) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("contentPage", contentPage);
        model.addAttribute("pageStyle", pageStyle);
        return "layout";
    }
}
