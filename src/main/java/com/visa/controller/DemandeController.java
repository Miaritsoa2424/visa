package com.visa.controller;

import com.visa.entity.Personne;
import com.visa.entity.Visa;
import com.visa.repository.PersonneRepository;
import com.visa.repository.VisaRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.visa.dto.CreateDemandeDTO;
import com.visa.entity.ChampFournir;
import com.visa.entity.Demande;
import com.visa.entity.TypeDemande;
import com.visa.exception.BusinessValidationException;
import com.visa.service.ChampFournirService;
import com.visa.service.DemandeService;
import com.visa.service.NationaliteService;
import com.visa.service.SituationFamilialeService;
import com.visa.service.TypeDemandeService;
import com.visa.service.TypeVisaService;
import com.visa.service.UtilService;

@Controller
public class DemandeController {
    @Autowired
    private PersonneRepository personneRepository;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private DemandeService demandeService;
    @Autowired
    private NationaliteService nationaliteService;
    @Autowired
    private SituationFamilialeService situationFamilialeService;
    @Autowired
    private TypeVisaService typeVisaService;
    @Autowired
    private TypeDemandeService typeDemandeService;
    @Autowired
    private ChampFournirService champFournirService;

    // DemandeController(PersonneRepository personneRepository) {
    //     this.personneRepository = personneRepository;
    // }

    @GetMapping("/demandes")
    public String listDemandes(Model model) {
        List<Demande> demandes = demandeService.getDemandes();
        Map<Integer, Boolean> canEditByDemandeId = demandes.stream()
                .collect(Collectors.toMap(
                        Demande::getId,
                demande -> demandeService.canEditDemandeByTypeStatutDemande(demande.getId())));

        model.addAttribute("demandes", demandes);
        model.addAttribute("canEditByDemandeId", canEditByDemandeId);
        return renderPage(model, "Liste des demandes", "/WEB-INF/jsp/demande/demandes.jsp", "demandes");
    }

    @GetMapping("/demande/fiche")
    public String ficheDemande(@RequestParam("id") Integer demandeId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Demande demande = demandeService.getDemandeById(demandeId);

            model.addAttribute("demande", demande);
            model.addAttribute("personne", demande.getPasseport() == null ? null : demande.getPasseport().getPersonne());
            model.addAttribute("passeport", demande.getPasseport());
            model.addAttribute("visaTransformable",
                    demande.getPasseport() == null || demande.getPasseport().getPersonne() == null
                            ? null
                            : demandeService.getVisaTransformableByPersonneId(demande.getPasseport().getPersonne().getId()));
            model.addAttribute("selectedChampFournirIds", demandeService.getSelectedChampFournirIds(demandeId));
            model.addAttribute("canEdit", demandeService.canEditDemandeByTypeStatutDemande(demandeId));

            return renderPage(model, "Fiche demande", "/WEB-INF/jsp/demande/demande-fiche.jsp", "demande-confirmation");
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demandes";
        }
    }

    @GetMapping("/demande/nouvelle")
    public String chooseDemandType(@RequestParam(value = "typeDemandeId", required = false) String typeDemandeIdParam,
            Model model) {
        Integer typeDemandeId = UtilService.parseTypeDemandeId(typeDemandeIdParam);
        if (typeDemandeId == null) {
            return "redirect:/home";
        }

        TypeDemande typeDemande = typeDemandeService.getById(typeDemandeId);
        
        // Charger les listes via le service
        model.addAttribute("typeDemande", typeDemande);
        model.addAttribute("typeDemandeId", typeDemandeId);
        model.addAttribute("nationalites", nationaliteService.getNationalites());
        model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
        model.addAttribute("typesVisa", typeVisaService.getTypesVisa());
        
        return renderPage(model, "Nouvelle demande", "/WEB-INF/jsp/demande/nouvelle-demande.jsp", "demande-form");
    }

    @GetMapping("/transfert/withData")
    public String tranfertVisa(Model model) {
        Personne personne = personneRepository.findById(1).orElse(null);
        Visa visa = personne == null
                ? null
                : visaRepository.findFirstByPersonneIdOrderByIdDesc(personne.getId()).orElse(null);

        model.addAttribute("personne", personne);
        model.addAttribute("visa", visa);
        model.addAttribute("typeDemandeId", 1); // Forcer le typeDemandeId à 1 pour le transfert de visa
        model.addAttribute("nationalites", nationaliteService.getNationalites());
        model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
        
        return renderPage(model, "Transfert de Visa", "/WEB-INF/jsp/demande/demande-transfert-with-data.jsp", "demande-form");
    }
    

    @GetMapping("/demande/modifier")
    public String editDemande(@RequestParam("id") Integer demandeId, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!demandeService.canEditDemandeByTypeStatutDemande(demandeId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Modification interdite: le type_statut_demande doit etre egal a 1.");
                return "redirect:/demandes";
            }

            Demande demande = demandeService.getDemandeById(demandeId);
            Integer typeDemandeId = demande.getTypeDemande() == null ? null : demande.getTypeDemande().getId();
            TypeDemande typeDemande = demande.getTypeDemande();

            model.addAttribute("demande", demande);
            model.addAttribute("typeDemande", typeDemande);
            model.addAttribute("typeDemandeId", typeDemandeId);
            model.addAttribute("selectedTypeVisaId", demande.getTypeVisa() == null ? null : demande.getTypeVisa().getId());
            model.addAttribute("selectedChampFournirIds", demandeService.getSelectedChampFournirIds(demandeId));
            model.addAttribute("personne", demande.getPasseport() == null ? null : demande.getPasseport().getPersonne());
            model.addAttribute("passeport", demande.getPasseport());
            model.addAttribute("visaTransformable",
                    demande.getPasseport() == null || demande.getPasseport().getPersonne() == null
                            ? null
                            : demandeService.getVisaTransformableByPersonneId(demande.getPasseport().getPersonne().getId()));
            model.addAttribute("nationalites", nationaliteService.getNationalites());
            model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
            model.addAttribute("typesVisa", typeVisaService.getTypesVisa());

            return renderPage(model, "Modifier demande", "/WEB-INF/jsp/demande/modifier-demande.jsp", "demande-form");
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demandes";
        }
    }

    @GetMapping("/demande/champs-fournir")
    @ResponseBody
    public List<Map<String, Object>> getChampsFournir(@RequestParam("typeVisaId") Integer typeVisaId) {
        List<ChampFournir> champs = champFournirService.getByTypeVisaId(typeVisaId);
        List<Map<String, Object>> response = new ArrayList<>();

        for (ChampFournir champ : champs) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", champ.getId());
            item.put("libelle", champ.getLibelle());
            item.put("typeDonnee", champ.getTypeDonnee());
            response.add(item);
        }

        return response;
    }


    @PostMapping("/demande/creer")
        public String createDemande(@RequestParam Map<String, String> formValues,
            @RequestParam(name = "champFournirIds", required = false) List<Integer> champFournirIds,
            RedirectAttributes redirectAttributes,
            Model model) {
        CreateDemandeDTO dto = new CreateDemandeDTO();

        dto.setNom(formValues.get("nom"));
        dto.setPrenom(formValues.get("prenom"));
        dto.setNomJeuneFille(formValues.get("nomJeuneFille"));
        dto.setEmail(formValues.get("email"));
        dto.setDateNaissance(UtilService.parseLocalDate(formValues.get("dateNaissance")));
        dto.setLieuNaissance(formValues.get("lieuNaissance"));
        dto.setAdresse(formValues.get("adresse"));
        dto.setTelephone(formValues.get("telephone"));
        dto.setNationalite(UtilService.parseInteger(formValues.get("nationalite")));
        dto.setSituationFamiliale(UtilService.parseInteger(formValues.get("situationFamiliale")));

        dto.setNumeroPasseport(formValues.get("numeroPasseport"));
        dto.setDateExpirationPasseport(UtilService.parseLocalDate(formValues.get("dateExpirationPasseport")));

        dto.setNumeroVisaTransformable(formValues.get("numeroVisaTransformable"));
        dto.setDateArrivee(UtilService.parseLocalDate(formValues.get("dateArrivee")));
        dto.setDateExpirationVisaTransformable(UtilService.parseLocalDate(formValues.get("dateExpirationVisaTransformable")));

        dto.setDateDemande(UtilService.parseLocalDate(formValues.get("dateDemande")));
        dto.setTypeVisa(UtilService.parseInteger(formValues.get("typeVisa")));
        dto.setTypeDemandeId(UtilService.parseInteger(formValues.get("typeDemandeId")));

        dto.setChampFournirIds(champFournirIds);

        try {
            var demandeCreee = demandeService.createDemande(dto);
            model.addAttribute("demande", demandeCreee);
            model.addAttribute("dto", dto);
            model.addAttribute("statutInitial", "Cree");
            model.addAttribute("champFournirCount", champFournirIds == null ? 0 : champFournirIds.size());
            return renderPage(model, "Demande confirmee", "/WEB-INF/jsp/demande/demande-confirmation.jsp", "demande-confirmation");
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demande/nouvelle?typeDemandeId=" + (dto.getTypeDemandeId() == null ? "" : dto.getTypeDemandeId());
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur technique lors de la creation de la demande.");
            return "redirect:/demande/nouvelle?typeDemandeId=" + (dto.getTypeDemandeId() == null ? "" : dto.getTypeDemandeId());
        }
    }

    @PostMapping("/demande/modifier")
    public String updateDemande(@RequestParam Map<String, String> formValues,
            @RequestParam(name = "champFournirIds", required = false) List<Integer> champFournirIds,
            RedirectAttributes redirectAttributes,
            Model model) {
        CreateDemandeDTO dto = new CreateDemandeDTO();

        Integer demandeId = UtilService.parseInteger(formValues.get("demandeId"));

        dto.setNom(formValues.get("nom"));
        dto.setPrenom(formValues.get("prenom"));
        dto.setNomJeuneFille(formValues.get("nomJeuneFille"));
        dto.setEmail(formValues.get("email"));
        dto.setDateNaissance(UtilService.parseLocalDate(formValues.get("dateNaissance")));
        dto.setLieuNaissance(formValues.get("lieuNaissance"));
        dto.setAdresse(formValues.get("adresse"));
        dto.setTelephone(formValues.get("telephone"));
        dto.setNationalite(UtilService.parseInteger(formValues.get("nationalite")));
        dto.setSituationFamiliale(UtilService.parseInteger(formValues.get("situationFamiliale")));

        dto.setNumeroPasseport(formValues.get("numeroPasseport"));
        dto.setDateExpirationPasseport(UtilService.parseLocalDate(formValues.get("dateExpirationPasseport")));

        dto.setNumeroVisaTransformable(formValues.get("numeroVisaTransformable"));
        dto.setDateArrivee(UtilService.parseLocalDate(formValues.get("dateArrivee")));
        dto.setDateExpirationVisaTransformable(UtilService.parseLocalDate(formValues.get("dateExpirationVisaTransformable")));

        dto.setDateDemande(UtilService.parseLocalDate(formValues.get("dateDemande")));
        dto.setTypeVisa(UtilService.parseInteger(formValues.get("typeVisa")));
        dto.setTypeDemandeId(UtilService.parseInteger(formValues.get("typeDemandeId")));

        dto.setChampFournirIds(champFournirIds);

        if (demandeId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Identifiant de demande invalide.");
            return "redirect:/demandes";
        }

        if (!demandeService.canEditDemandeByTypeStatutDemande(demandeId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Modification interdite: le type_statut_demande doit etre egal a 1.");
            return "redirect:/demandes";
        }

        try {
            Demande demandeModifiee = demandeService.updateDemande(demandeId, dto);
            model.addAttribute("demande", demandeModifiee);
            model.addAttribute("dto", dto);
            model.addAttribute("statutInitial", "Modifiee");
            model.addAttribute("champFournirCount", champFournirIds == null ? 0 : champFournirIds.size());
            return renderPage(model, "Demande confirmee", "/WEB-INF/jsp/demande/demande-confirmation.jsp", "demande-confirmation");
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demande/modifier?id=" + demandeId;
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur technique lors de la modification de la demande.");
            return "redirect:/demande/modifier?id=" + demandeId;
        }
    }

    private String renderPage(Model model, String pageTitle, String contentPage, String pageStyle) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("contentPage", contentPage);
        model.addAttribute("pageStyle", pageStyle);
        return "layout";
    }

}