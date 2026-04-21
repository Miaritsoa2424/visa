package com.visa.controller;

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
import com.visa.entity.Demande;
import com.visa.entity.TypeDemande;
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
    public String createDemande(CreateDemandeDTO dto,
            @RequestParam(name = "champFournirIds", required = false) List<Integer> champFournirIds,
            RedirectAttributes redirectAttributes) {
        try {
            // Assigner les champFournirIds au DTO
            dto.champFournirIds = champFournirIds;

            // Appeler le service pour créer la demande
            Demande demande = demandeService.createDemande(dto);

            // Message de succès
            redirectAttributes.addFlashAttribute("succesMessage", 
                "Demande créée avec succès (ID: " + demande.getId() + ")");

            return "redirect:/demandes";
        } catch (Exception e) {
            // Message d'erreur
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors de la création de la demande: " + e.getMessage());

            if (dto.typeDemandeId == null) {
                return "redirect:/home";
            }

            return "redirect:/demande/nouvelle?typeDemandeId=" + dto.typeDemandeId;
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