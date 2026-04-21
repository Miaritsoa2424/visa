package com.visa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.visa.repository.TypeDemandeRepository;


@Controller
public class HomeController {

    private final TypeDemandeRepository typeDemandeRepository;

    public HomeController(TypeDemandeRepository typeDemandeRepository) {
        this.typeDemandeRepository = typeDemandeRepository;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("typesDemande", typeDemandeRepository.findAll());
        return "index";
    }
}
