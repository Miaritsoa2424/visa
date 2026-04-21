package com.visa.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.visa.entity.TypeDemande;
import com.visa.repository.DemandeRepository;
import com.visa.repository.TypeDemandeRepository;

@Controller
public class DemandeController {

    private final DemandeRepository demandeRepository;
    private final TypeDemandeRepository typeDemandeRepository;

    public DemandeController(DemandeRepository demandeRepository, TypeDemandeRepository typeDemandeRepository) {
        this.demandeRepository = demandeRepository;
        this.typeDemandeRepository = typeDemandeRepository;
    }

    @GetMapping("/demandes")
    public String listDemandes(Model model) {
        model.addAttribute("demandes", demandeRepository.findAll());
        return "demandes";
    }

    @GetMapping("/demande/nouvelle")
    public String chooseDemandType(@RequestParam("typeDemandeId") Integer typeDemandeId, Model model) {
        TypeDemande typeDemande = typeDemandeRepository.findById(typeDemandeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Type de demande introuvable"));
        model.addAttribute("typeDemande", typeDemande);
        return "nouvelle-demande";
    }
}