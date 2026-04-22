package com.visa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.visa.service.TypeDemandeService;

@Controller
public class HomeController {

    @Autowired
    private TypeDemandeService typeDemandeService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("typesDemande", typeDemandeService.getTypesDemande());
        return "index";
    }
}
