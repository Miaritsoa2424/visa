package com.visa.controller;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.visa.dto.CreateDemandeDTO;
import com.visa.entity.Base64MultipartFilePerso;
import com.visa.entity.ChampFournir;
import com.visa.entity.Demande;
import com.visa.entity.DossierProfessionnel;
import com.visa.entity.Personne;
import com.visa.entity.TypeDemande;
import com.visa.entity.Visa;
import com.visa.exception.BusinessValidationException;
import com.visa.repository.PaysRepository;
import com.visa.repository.VisaRepository;
import com.visa.service.ChampFournirService;
import com.visa.service.DemandeService;
import com.visa.service.DossierProfessionnelService;
import com.visa.service.NationaliteService;

import com.visa.service.ExportPdfService;
import org.springframework.web.multipart.MultipartFile;
import com.visa.service.SituationFamilialeService;
import com.visa.service.TypeDemandeService;
import com.visa.service.TypeVisaService;
import com.visa.service.UtilService;


@Controller
public class DemandeController {
    private static final Logger logger = LoggerFactory.getLogger(DemandeController.class);
    
    private final DossierProfessionnelService dossierProfessionnelService;
    @Autowired
    private VisaRepository visaRepository;
    @Autowired
    private PaysRepository paysRepository;

    private static final Integer TRANSFERT_TYPE_DEMANDE_ID = 4;

    @Autowired
    private DemandeService demandeService;
    @Autowired
    private com.visa.service.FichierUploadeService fichierUploadeService;
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

    private static final String TYPE_STATUT_DEMANDE_SCAN_TERMINE_ID = "2";

    @Autowired
    private ExportPdfService exportPdfService;

    DemandeController(DossierProfessionnelService dossierProfessionnelService) {
        this.dossierProfessionnelService = dossierProfessionnelService;
    }

    @GetMapping("/demandes")
    public String listDemandes(Model model) {
        List<Demande> demandes = demandeService.getDemandes();
        Map<Integer, Boolean> canEditByDemandeId = demandes.stream()
                .collect(Collectors.toMap(
                        Demande::getId,
                        demande -> demandeService.canEditDemandeByTypeStatutDemande(demande.getId())));
        Map<Integer, String> statutByDemandeId = demandes.stream()
            .collect(Collectors.toMap(
                Demande::getId,
                demande -> demandeService.getStatutDemandeLibelle(demande.getId())));

        model.addAttribute("demandes", demandes);
        model.addAttribute("canEditByDemandeId", canEditByDemandeId);
        model.addAttribute("statutByDemandeId", statutByDemandeId);
        return renderPage(model, "Liste des demandes", "demande/demandes.jsp", "demandes");
    }

    @GetMapping("/demande/export")
    public ResponseEntity<byte[]> exportDemandePdf(@RequestParam("id") Integer demandeId) {
        try {
            byte[] pdfBytes = exportPdfService.buildPdfForDemande(demandeId);
            String filename = "demande_" + demandeId + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                    .header("Content-Transfer-Encoding", "binary")
                    .contentLength(pdfBytes.length)
                    .body(pdfBytes);
        } catch (Exception e) {
            logger.error("Error exporting PDF for demande ID: " + demandeId, e);
            return ResponseEntity.internalServerError().build();
        }
    }    

    @GetMapping("/demande/fiche")
    public String ficheDemande(@RequestParam("id") Integer demandeId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Demande demande = demandeService.getDemandeById(demandeId);

            model.addAttribute("demande", demande);
            model.addAttribute("personne",
                    demande.getPasseport() == null ? null : demande.getPasseport().getPersonne());
            model.addAttribute("passeport", demande.getPasseport());
            model.addAttribute("visaTransformable",
                    demande.getPasseport() == null || demande.getPasseport().getPersonne() == null
                            ? null
                            : demandeService
                                    .getVisaTransformableByPersonneId(demande.getPasseport().getPersonne().getId()));
            model.addAttribute("selectedChampFournirIds", demandeService.getSelectedChampFournirIds(demandeId));
            model.addAttribute("champsFournirWithStatus", demandeService.getChampsFournirWithStatus(demandeId));
            model.addAttribute("canEdit", demandeService.canEditDemandeByTypeStatutDemande(demandeId));

            // Charger les fichiers uploade associes a cette demande (images, signatures, etc.)
            model.addAttribute("fichiersUplodes", fichierUploadeService.getFilesByDemandeId(demandeId));

            return renderPage(model, "Fiche demande", "demande/demande-fiche.jsp", "demande-confirmation");
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

        if (isTransfertType(typeDemande)) {
            return "redirect:/demande/check-numero-visa?typeDemandeId=" + typeDemandeId;
        }
        
        // Charger les listes via le service
        model.addAttribute("typeDemande", typeDemande);
        model.addAttribute("typeDemandeId", typeDemandeId);
        model.addAttribute("nationalites", nationaliteService.getNationalites());
        model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
        model.addAttribute("typesVisa", typeVisaService.getTypesVisa());
        
        return renderPage(model, "Nouvelle demande", "demande/nouvelle-demande.jsp", "demande-form");
    }

    @GetMapping("/demande/check-numero-visa")
    public String checkNumeroVisaPage(@RequestParam(value = "typeDemandeId", required = false) String typeDemandeIdParam,
            Model model) {
        Integer typeDemandeId = UtilService.parseTypeDemandeId(typeDemandeIdParam);
        if (typeDemandeId == null) {
            return "redirect:/home";
        }

        TypeDemande typeDemande = typeDemandeService.getById(typeDemandeId);
        if (!isTransfertType(typeDemande)) {
            return "redirect:/demande/nouvelle?typeDemandeId=" + typeDemandeId;
        }

        model.addAttribute("typeDemande", typeDemande);
        model.addAttribute("typeDemandeId", typeDemandeId);
        model.addAttribute("visaVerificationDone", false);
        return renderPage(model, "Verification du visa source", "demande/check-numero-visa.jsp", "demande-form");
    }

    @PostMapping("/demande/check-numero-visa")
    public String checkNumeroVisa(@RequestParam(value = "typeDemandeId", required = false) String typeDemandeIdParam,
            @RequestParam(value = "numeroVisa", required = false) String numeroVisa,
            Model model) {
        Integer typeDemandeId = UtilService.parseTypeDemandeId(typeDemandeIdParam);
        if (typeDemandeId == null) {
            return "redirect:/home";
        }

        TypeDemande typeDemande = typeDemandeService.getById(typeDemandeId);
        if (!isTransfertType(typeDemande)) {
            return "redirect:/demande/nouvelle?typeDemandeId=" + typeDemandeId;
        }

        boolean visaExiste = demandeService.visaExistsByNumero(numeroVisa);
        model.addAttribute("typeDemande", typeDemande);
        model.addAttribute("typeDemandeId", typeDemandeId);
        model.addAttribute("numeroVisa", numeroVisa);
        model.addAttribute("visaVerificationDone", true);
        model.addAttribute("visaExiste", visaExiste);
        model.addAttribute("verificationMessage",
                visaExiste
                        ? "Oui, le visa existe dans la base de donnees."
                        : "Non, le visa n'existe pas dans la base de donnees.");
        model.addAttribute("redirectUrl", visaExiste
                ? "/transfert/withData?numeroVisa=" + numeroVisa
                : "/demande/transfert-form-sans-donnee?typeDemandeId=" + typeDemandeId);

        return renderPage(model, "Verification du visa source", "demande/check-numero-visa.jsp", "demande-form");
    }

    @GetMapping("/demande/transfert-form-sans-donnee")
    public String transfertFormWithoutData(Model model) {

        Integer typeDemandeId = 4;

        TypeDemande typeDemande = typeDemandeService.getById(typeDemandeId);
        
        // Charger les listes via le service
        model.addAttribute("typeDemande", typeDemande);
        model.addAttribute("typeDemandeId", typeDemandeId);
        model.addAttribute("nationalites", nationaliteService.getNationalites());
        model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
        model.addAttribute("typesVisa", typeVisaService.getTypesVisa());
        model.addAttribute("pays", paysRepository.findAll());
        
        return renderPage(model, "Formulaire de transfert sans donnees", "demande/demande-transfert-without-data.jsp", "demande-form");
    }

    @PostMapping("/demande/transfert-sans-donnee")
    public String transfertWithoutData(@RequestParam Map<String, String> formValues,
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

        dto.setNumeroPasseportAncien(formValues.get("numeroPasseportAncien"));
        dto.setDateExpirationPasseportAncien(UtilService.parseLocalDate(formValues.get("dateExpirationPasseportAncien")));

        String numeroVisa = formValues.get("numeroVisaAncien");
        dto.setNumeroVisaAncien(numeroVisa);
        dto.setNumeroVisaTransformable(numeroVisa);
        dto.setDateEntre(UtilService.parseLocalDate(formValues.get("dateEntre")));
        dto.setDateExpiration(UtilService.parseLocalDate(formValues.get("dateExpiration")));
        dto.setDateDelivrance(UtilService.parseLocalDate(formValues.get("dateDelivrance")));
        dto.setIdPaysEntre(UtilService.parseInteger(formValues.get("idPaysEntre")));
        Integer idTypeVisa = UtilService.parseInteger(formValues.get("idTypeVisa"));
        dto.setIdTypeVisa(idTypeVisa);
        dto.setTypeVisa(idTypeVisa);

        dto.setChampFournirIds(champFournirIds);

        try {
            demandeService.tranfererVisaWithoutData(dto);
            model.addAttribute("success", true);
            model.addAttribute("succesMessage", "Transfert de visa réussi. La demande a été créée avec succès.");
            model.addAttribute("typeDemande", typeDemandeService.getById(TRANSFERT_TYPE_DEMANDE_ID));
            model.addAttribute("typeDemandeId", TRANSFERT_TYPE_DEMANDE_ID);
            model.addAttribute("nationalites", nationaliteService.getNationalites());
            model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
            model.addAttribute("typesVisa", typeVisaService.getTypesVisa());
            model.addAttribute("pays", paysRepository.findAll());
            return renderPage(model, "Transfert de Visa", "demande/demande-transfert-without-data.jsp", "demande-form");
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demande/transfert-form-sans-donnee";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors du transfert de visa: " + exception.getMessage());
            return "redirect:/demande/transfert-form-sans-donnee";
        }
    }
    

    @GetMapping("/transfert/withData")
    public String tranfertVisa(@RequestParam(value = "numeroVisa") String numeroVisa, Model model) {
        Visa visa = visaRepository.findFirstByNumero(numeroVisa).orElse(null);
        if (visa == null) {
            model.addAttribute("errorMessage", "Visa avec le numero " + numeroVisa + " n'existe pas.");
            return renderPage(model, "Transfert de Visa", "demande/check-numero-visa.jsp", "demande-form");
        }
        Personne personne = visa.getPersonne();
        if (personne == null) {
            model.addAttribute("errorMessage", "Aucune personne associee au visa numero " + numeroVisa + ".");
            return renderPage(model, "Transfert de Visa", "demande/check-numero-visa.jsp", "demande-form");
        }
        // Personne personne = personneRepository.findById(1).orElse(null);
        // Visa visa = personne == null
                // ? null
                // : visaRepository.findFirstByPersonneIdOrderByIdDesc(personne.getId()).orElse(null);

        model.addAttribute("personne", personne);
        model.addAttribute("visa", visa);
        model.addAttribute("typeDemandeId", 1); // Forcer le typeDemandeId à 1 pour le transfert de visa
        model.addAttribute("nationalites", nationaliteService.getNationalites());
        model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());

        return renderPage(model, "Transfert de Visa", "/WEB-INF/jsp/demande/demande-transfert-with-data.jsp",
                "demande-form");
    }

    @PostMapping("/transfert/execute")
    public String executeTransfert(@RequestParam Map<String, String> formValues, Model model) {

        try {

            String visaNumero = formValues.get("numeroVisa");
            String numeroPasseport = formValues.get("numeroPasseport");
            LocalDate dateExpiration = UtilService.parseLocalDate(formValues.get("dateExpirationPasseport"));

            //Visa
            Visa visa = visaRepository.findFirstByNumero(visaNumero)
                    .orElseThrow(() -> new BusinessValidationException("Visa avec le numero " + visaNumero + " n'existe pas."));

            /// Demande
            Demande demande = new Demande();
            demande.setDateDemande(LocalDate.now());
            demande.setTypeDemande(typeDemandeService.getById(4)); // Forcer le type de demande à 3 pour le transfert de
                                                                   // visa
            demande.setTypeVisa(visa.getTypeVisa());
            demandeService.tranfererVisa(visaNumero, numeroPasseport, dateExpiration, demande);
            model.addAttribute("success", true);
            model.addAttribute("succesMessage", "Transfert de visa réussi. La demande a été créée avec succès.");

            Personne personne = visa.getPersonne();

            model.addAttribute("personne", personne);
            model.addAttribute("visa", visa);
            model.addAttribute("typeDemandeId", 3); // Forcer le typeDemandeId à 3 pour le transfert de visa
            model.addAttribute("nationalites", nationaliteService.getNationalites());
            model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors du transfert de visa: " + e.getMessage());
        }
        return renderPage(model, "Transfert de Visa", "demande/demande-transfert-with-data.jsp",
        "demande-form");

    }

    
    @GetMapping("/demande/modifier")
    public String editDemande(@RequestParam("id") Integer demandeId, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // if (!demandeService.canOpenModifierPageByTypeStatutDemande(demandeId)) {
            //     redirectAttributes.addFlashAttribute("errorMessage",
            //             "Modification interdite: le type_statut_demande doit etre egal a 1 ou 2.");
            //     return "redirect:/demandes";
            // }

            Demande demande = demandeService.getDemandeById(demandeId);
            Integer typeDemandeId = demande.getTypeDemande() == null ? null : demande.getTypeDemande().getId();
            TypeDemande typeDemande = demande.getTypeDemande();

            model.addAttribute("demande", demande);
            model.addAttribute("typeDemande", typeDemande);
            model.addAttribute("typeDemandeId", typeDemandeId);
            model.addAttribute("selectedTypeVisaId",
                    demande.getTypeVisa() == null ? null : demande.getTypeVisa().getId());
            model.addAttribute("selectedChampFournirIds", demandeService.getSelectedChampFournirIds(demandeId));
            model.addAttribute("personne",
                    demande.getPasseport() == null ? null : demande.getPasseport().getPersonne());
            model.addAttribute("passeport", demande.getPasseport());
            model.addAttribute("visaTransformable",
                    demande.getPasseport() == null || demande.getPasseport().getPersonne() == null
                            ? null
                            : demandeService
                                    .getVisaTransformableByPersonneId(demande.getPasseport().getPersonne().getId()));
            model.addAttribute("nationalites", nationaliteService.getNationalites());
            model.addAttribute("situationsFamiliales", situationFamilialeService.getSituationsFamiliales());
            model.addAttribute("typesVisa", typeVisaService.getTypesVisa());
            // model.addAttribute("isScanTermine", demandeService.isScanTermineByTypeStatutDemandeId(demandeId));
            model.addAttribute("isScanTermine", false);


            return renderPage(model, "Modifier demande", "demande/modifier-demande.jsp", "demande-form");
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

    @GetMapping("/demande/photo-signature")
    public String photoSignaturePage(@RequestParam("id") Integer id, Model model) {
        model.addAttribute("photoCaptured", false);
        model.addAttribute("id", id);
        return renderPage(model, "Photo et signature", "demande/photo-signature.jsp", "photo-signature");
    }

    @GetMapping("/demande/attachments")
    @ResponseBody
    public List<Map<String, Object>> getAttachmentsForDemande(@RequestParam("demandeId") Integer demandeId) {
        List<Map<String, Object>> response = new ArrayList<>();
        List<com.visa.entity.FichierUploade> fichiers = fichierUploadeService.getFilesByDemandeId(demandeId);
        if (fichiers == null || fichiers.isEmpty()) {
            return response;
        }

        for (com.visa.entity.FichierUploade f : fichiers) {
            Map<String, Object> item = new HashMap<>();
            String valeur = f.getValeur();
            // valeur is stored like "assets/dossierPro/...." - build public URL
            String url = valeur == null ? "" : ("/" + valeur.replaceAll("\\\\", "/"));
            String filename = valeur == null ? "" : Paths.get(valeur).getFileName().toString();
            String lower = filename == null ? "" : filename.toLowerCase();
            String kind = "other";
            if (lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".gif")) {
                kind = "image";
            } else if (lower.endsWith(".pdf")) {
                kind = "pdf";
            }

            item.put("id", f.getId());
            String dossierValeur = "";
            if (f.getDossierProfessionnel() != null && f.getDossierProfessionnel().getValeur() != null) {
                dossierValeur = f.getDossierProfessionnel().getValeur();
            }
            item.put("dossierValeur", dossierValeur);
            item.put("filename", filename);
            item.put("url", url);
            item.put("kind", kind);
            response.add(item);
        }

        return response;
    }

    @PostMapping("/demande/photo-signature")
    public String submitPhotoSignature(@RequestParam(value = "photoData", required = false) String photoData,
            @RequestParam(value = "signatureData", required = false) String signatureData,
            @RequestParam(value = "id") Integer id,
            Model model) {

        try {
            Demande demande = demandeService.getDemandeById(id);
            if (demande == null) {
                model.addAttribute("errorMessage", "Demande introuvable.");
                return renderPage(model, "Photo et signature", "demande/photo-signature.jsp", "photo-signature");
            }

            DossierProfessionnel dossier = new DossierProfessionnel();
            dossier.setDemande(demande);
            dossier.setValeur("Image");
            DossierProfessionnel saved = dossierProfessionnelService.saveDossierProfessionnel(dossier);

            DossierProfessionnel signature = new DossierProfessionnel();
            signature.setDemande(demande);
            signature.setValeur("Signature");
            DossierProfessionnel savedSignature = dossierProfessionnelService.saveDossierProfessionnel(signature);

            // Construire des MultipartFile depuis les données base64
            MultipartFile photoFile = new Base64MultipartFilePerso("photo.png", photoData);
            MultipartFile signatureFile = new Base64MultipartFilePerso("signature.png", signatureData);

            java.util.List<java.nio.file.Path> copies = new ArrayList<>();
            // Appel de la methode qui copie les fichiers et enregistre les entites FichierUploade
            dossierProfessionnelService.enregistrerFichiersUploades(saved, new MultipartFile[] { photoFile}, copies);

            dossierProfessionnelService.enregistrerFichiersUploades(savedSignature, new MultipartFile[] { signatureFile}, copies);

            demandeService.modifierStatutDemande(demande, TYPE_STATUT_DEMANDE_SCAN_TERMINE_ID); // Passer le statut de la demande à "photo et signature"

            model.addAttribute("successMessage", "Photo et signature captures avec succes.");
            model.addAttribute("photoData", photoData);
            model.addAttribute("signatureData", signatureData);
            model.addAttribute("photoCaptured", photoData != null && !photoData.isBlank());
            model.addAttribute("signatureCaptured", signatureData != null && !signatureData.isBlank());
            return renderPage(model, "Photo et signature", "demande/photo-signature.jsp", "photo-signature");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement: " + e.getMessage());
            return renderPage(model, "Photo et signature", "demande/photo-signature.jsp", "photo-signature");
        }
    }

    @GetMapping("/qrcode/{demandeId}")
    public ResponseEntity<byte[]> getQrCode(@PathVariable Integer demandeId) {
        try {
            Demande demande = demandeService.getDemandeById(demandeId);
            if (demande == null || demande.getQrcode() == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(demande.getQrcode());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
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
        dto.setDateExpirationVisaTransformable(
                UtilService.parseLocalDate(formValues.get("dateExpirationVisaTransformable")));

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
            return renderPage(model, "Demande confirmee", "demande/demande-confirmation.jsp", "demande-confirmation");
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demande/nouvelle?typeDemandeId="
                    + (dto.getTypeDemandeId() == null ? "" : dto.getTypeDemandeId());
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur technique lors de la creation de la demande.");
            return "redirect:/demande/nouvelle?typeDemandeId="
                    + (dto.getTypeDemandeId() == null ? "" : dto.getTypeDemandeId());
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
        dto.setDateExpirationVisaTransformable(
                UtilService.parseLocalDate(formValues.get("dateExpirationVisaTransformable")));

        dto.setDateDemande(UtilService.parseLocalDate(formValues.get("dateDemande")));
        dto.setTypeVisa(UtilService.parseInteger(formValues.get("typeVisa")));
        dto.setTypeDemandeId(UtilService.parseInteger(formValues.get("typeDemandeId")));

        dto.setChampFournirIds(champFournirIds);

        if (demandeId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Identifiant de demande invalide.");
            return "redirect:/demandes";
        }

        if (!demandeService.canEditDemandeByTypeStatutDemande(demandeId)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Modification interdite: le type_statut_demande doit etre egal a 1.");
            return "redirect:/demandes";
        }

        try {
            Demande demandeModifiee = demandeService.updateDemande(demandeId, dto);
            model.addAttribute("demande", demandeModifiee);
            model.addAttribute("dto", dto);
            model.addAttribute("statutInitial", "Modifiee");
            model.addAttribute("champFournirCount", champFournirIds == null ? 0 : champFournirIds.size());
            return renderPage(model, "Demande confirmee", "demande/demande-confirmation.jsp", "demande-confirmation");
        } catch (BusinessValidationException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
            return "redirect:/demande/modifier?id=" + demandeId;
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur technique lors de la modification de la demande.");
            return "redirect:/demande/modifier?id=" + demandeId;
        }
    }

    private String renderPage(Model model, String pageTitle, String contentPage, String pageStyle) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("contentPage", contentPage);
        model.addAttribute("pageStyle", pageStyle);
        return "layout";
    }

    private boolean isTransfertType(TypeDemande typeDemande) {
        return typeDemande != null
                && TRANSFERT_TYPE_DEMANDE_ID.equals(typeDemande.getId());
    }

}