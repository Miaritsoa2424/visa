package com.visa.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.entity.Demande;
import com.visa.entity.FichierUploade;

@Service
public class ExportPdfService {

    @Autowired
    private DemandeService demandeService;

    @Autowired
    private FichierUploadeService fichierUploadeService;

    private static final Path APP_ROOT = Paths.get("");

    public byte[] buildPdfForDemande(Integer demandeId) {
        Demande demande = demandeService.getDemandeById(demandeId);
        List<FichierUploade> fichiers = fichierUploadeService.getFilesByDemandeId(demandeId);

        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Page 1: information texte  
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 24);
                cs.newLineAtOffset(50, 750);
                cs.showText("VisaTrack Madagascar");
                cs.newLineAtOffset(0, -30);
                cs.showText("Demande #" + demande.getId());
                cs.endText();
                
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(50, 650);
                
                // Display date on first line
                if (demande.getDateDemande() != null) {
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRENCH);
                    cs.showText("Date: " + df.format(demande.getDateDemande()));
                    cs.newLineAtOffset(0, -15);
                }
                
                // Display visa type on second line
                if (demande.getTypeVisa() != null) {
                    cs.showText("Type de visa: " + demande.getTypeVisa().getLibelle());
                    cs.newLineAtOffset(0, -15);
                }
                
                cs.endText();
            }

            // Add images
            int imageCount = 0;
            for (FichierUploade f : fichiers) {
                String valeur = f.getValeur();
                if (valeur == null) continue;
                Path path = APP_ROOT.resolve(valeur).normalize();
                File file = path.toFile();
                if (!file.exists()) continue;

                try {
                    BufferedImage bimg = ImageIO.read(file);
                    if (bimg == null) continue;

                    PDPage imgPage = new PDPage(PDRectangle.A4);
                    doc.addPage(imgPage);
                    imageCount++;

                    PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bimg);
                    try (PDPageContentStream cs = new PDPageContentStream(doc, imgPage)) {
                        PDRectangle mediaBox = imgPage.getMediaBox();
                        float pageHeight = mediaBox.getHeight();
                        float pageWidth = mediaBox.getWidth();

                        float imgWidth = pdImage.getWidth();
                        float imgHeight = pdImage.getHeight();

                        float scale = Math.min((pageWidth - 100) / imgWidth, (pageHeight - 100) / imgHeight);
                        float drawWidth = imgWidth * scale;
                        float drawHeight = imgHeight * scale;

                        float x = (pageWidth - drawWidth) / 2;
                        float y = (pageHeight - drawHeight) / 2;

                        cs.drawImage(pdImage, x, y, drawWidth, drawHeight);
                    }
                } catch (IOException e) {
                    // skip
                }
            }

            doc.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la generation du PDF: " + e.getMessage(), e);
        }
    }
}
