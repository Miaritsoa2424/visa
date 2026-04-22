package com.visa.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.visa.service.DemandeService;
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
import com.visa.entity.TypeDemande;
import com.visa.exception.BusinessValidationException;
import com.visa.service.ChampFournirService;
import com.visa.service.NationaliteService;
import com.visa.service.SituationFamilialeService;
import com.visa.service.TypeDemandeService;
import com.visa.service.TypeVisaService;

@Controller
public class DemandeController {

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

    @GetMapping("/demandes")
    public String listDemandes(Model model) {
        model.addAttribute("demandes", demandeService.getDemandes());
        return "demandes";
    }

    @GetMapping("/demande/nouvelle")
    public String chooseDemandType(@RequestParam(value = "typeDemandeId", required = false) String typeDemandeIdParam,
            Model model) {
        Integer typeDemandeId = parseTypeDemandeId(typeDemandeIdParam);
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
        
        return "nouvelle-demande";
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
        dto.setDateNaissance(parseLocalDate(formValues.get("dateNaissance")));
        dto.setLieuNaissance(formValues.get("lieuNaissance"));
        dto.setAdresse(formValues.get("adresse"));
        dto.setTelephone(formValues.get("telephone"));
        dto.setNationalite(parseInteger(formValues.get("nationalite")));
        dto.setSituationFamiliale(parseInteger(formValues.get("situationFamiliale")));

        dto.setNumeroPasseport(formValues.get("numeroPasseport"));
        dto.setDateExpirationPasseport(parseLocalDate(formValues.get("dateExpirationPasseport")));

        dto.setNumeroVisaTransformable(formValues.get("numeroVisaTransformable"));
        dto.setDateArrivee(parseLocalDate(formValues.get("dateArrivee")));
        dto.setDateExpirationVisaTransformable(parseLocalDate(formValues.get("dateExpirationVisaTransformable")));

        dto.setDateDemande(parseLocalDate(formValues.get("dateDemande")));
        dto.setTypeVisa(parseInteger(formValues.get("typeVisa")));
        dto.setTypeDemandeId(parseInteger(formValues.get("typeDemandeId")));

        dto.setChampFournirIds(champFournirIds);

        try {
            var demandeCreee = demandeService.createDemande(dto);
            model.addAttribute("demande", demandeCreee);
            model.addAttribute("dto", dto);
            model.addAttribute("statutInitial", "Cree");
            model.addAttribute("champFournirCount", champFournirIds == null ? 0 : champFournirIds.size());
            return "demande-confirmation";
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demande/nouvelle?typeDemandeId=" + (dto.getTypeDemandeId() == null ? "" : dto.getTypeDemandeId());
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur technique lors de la creation de la demande.");
            return "redirect:/demande/nouvelle?typeDemandeId=" + (dto.getTypeDemandeId() == null ? "" : dto.getTypeDemandeId());
        }
    }
    // @PostMapping("/demande/creer")
    // @ResponseBody
    // public CreateDemandeDTO createDemande(@RequestParam Map<String, String> formValues,
    //         @RequestParam(name = "champFournirIds", required = false) List<Integer> champFournirIds) {
    //     CreateDemandeDTO dto = new CreateDemandeDTO();

    //     // Etat civil
    //     dto.nom = formValues.get("nom");
    //     dto.prenom = formValues.get("prenom");
    //     dto.nomJeuneFille = formValues.get("nomJeuneFille");
    //     dto.email = formValues.get("email");
    //     dto.dateNaissance = parseLocalDate(formValues.get("dateNaissance"));
    //     dto.lieuNaissance = formValues.get("lieuNaissance");
    //     dto.adresse = formValues.get("adresse");
    //     dto.telephone = formValues.get("telephone");
    //     dto.nationalite = parseInteger(formValues.get("nationalite"));
    //     dto.situationFamiliale = parseInteger(formValues.get("situationFamiliale"));

    //     // Passeport
    //     dto.numeroPasseport = formValues.get("numeroPasseport");
    //     dto.dateExpirationPasseport = parseLocalDate(formValues.get("dateExpirationPasseport"));

    //     // Visa transformable
    //     dto.numeroVisaTransformable = formValues.get("numeroVisaTransformable");
    //     dto.dateArrivee = parseLocalDate(formValues.get("dateArrivee"));
    //     dto.dateExpirationVisaTransformable = parseLocalDate(formValues.get("dateExpirationVisaTransformable"));

    //     // Demande
    //     dto.dateDemande = parseLocalDate(formValues.get("dateDemande"));
    //     dto.typeVisa = parseInteger(formValues.get("typeVisa"));
    //     dto.typeDemandeId = parseInteger(formValues.get("typeDemandeId"));

    //     dto.champFournirIds = champFournirIds;
    //     return dto;
    // }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    private LocalDate parseLocalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (Exception exception) {
            return null;
        }
    }

    private Integer parseTypeDemandeId(String typeDemandeIdParam) {
        if (typeDemandeIdParam == null || typeDemandeIdParam.isBlank() || "null".equalsIgnoreCase(typeDemandeIdParam)) {
            return null;
        }

        try {
            return Integer.valueOf(typeDemandeIdParam);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}