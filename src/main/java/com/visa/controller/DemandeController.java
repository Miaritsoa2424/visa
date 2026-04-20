package com.visa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.visa.repository.DemandeRepository;

@Controller
public class DemandeController {

    private final DemandeRepository demandeRepository;

    public DemandeController(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @GetMapping("/demandes")
    public String listDemandes(Model model) {
        model.addAttribute("demandes", demandeRepository.findAll());
        return "demandes";
    }
}