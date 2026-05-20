package com.visa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visa.dto.RechercheDemandeDTO;
import com.visa.exception.BusinessValidationException;
import com.visa.service.RechercheDemandeService;

@RestController
@RequestMapping("/api/demande")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RechercheDemandeController {

    @Autowired
    private RechercheDemandeService rechercheDemandeService;

    /**
     * API pour rechercher une demande par numéro de passeport ou par ID de demande
     * 
     * @param recherche : numéro de passeport ou ID de demande
     * @return RechercheDemandeDTO avec la demande et les autres demandes du demandeur
     */
    @GetMapping("/rechercher")
    public ResponseEntity<?> rechercherDemande(@RequestParam String recherche) {
        try {
            RechercheDemandeDTO resultat = rechercheDemandeService.rechercherDemande(recherche);
            return ResponseEntity.ok(resultat);
        } catch (BusinessValidationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("NOT_FOUND", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("ERROR", "Erreur lors de la recherche : " + e.getMessage()));
        }
    }

    /**
     * Classe interne pour les erreurs
     */
    public static class ErrorResponse {
        private String code;
        private String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
