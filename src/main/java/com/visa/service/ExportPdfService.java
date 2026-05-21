package com.visa.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
                // Header
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 22);
                cs.newLineAtOffset(50, 770);
                cs.showText("VisaTrack Madagascar");
                cs.endText();

                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                cs.newLineAtOffset(50, 745);
                cs.showText("Demande #" + demande.getId());
                cs.endText();

                // Separator line
                cs.setStrokingColor(Color.DARK_GRAY);
                cs.setLineWidth(1);
                cs.moveTo(50, 735);
                cs.lineTo(545, 735);
                cs.stroke();

                DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMMM yyyy", java.util.Locale.FRENCH);

                // Info block with nicer layout
                float textX = 50;
                float textY = 715;
                cs.beginText();
                cs.setFont(PDType1Font.HELVETICA, 11);
                cs.newLineAtOffset(textX, textY);

                if (demande.getDateDemande() != null) {
                    cs.showText("Date: " + df.format(demande.getDateDemande()));
                    cs.newLineAtOffset(0, -16);
                }

                if (demande.getTypeVisa() != null) {
                    cs.showText("Type de visa demandé: " + demande.getTypeVisa().getLibelle());
                    cs.newLineAtOffset(0, -18);
                }

                if (demande.getPasseport() != null && demande.getPasseport().getPersonne() != null) {
                    com.visa.entity.Personne p = demande.getPasseport().getPersonne();
                    cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    cs.showText("Informations personnelles:");
                    cs.newLineAtOffset(0, -14);
                    cs.setFont(PDType1Font.HELVETICA, 11);

                    String nom = p.getNom() == null ? "" : p.getNom();
                    String prenom = p.getPrenom() == null ? "" : p.getPrenom();
                    cs.showText("Nom: " + nom + "    Prénom: " + prenom);
                    cs.newLineAtOffset(0, -14);

                    if (p.getDateNaissance() != null) {
                        cs.showText("Date de naissance: " + df.format(p.getDateNaissance()));
                        cs.newLineAtOffset(0, -14);
                    }

                    if (p.getLieuNaissance() != null && !p.getLieuNaissance().isBlank()) {
                        cs.showText("Lieu de naissance: " + p.getLieuNaissance());
                        cs.newLineAtOffset(0, -14);
                    }

                    if (demande.getPasseport() != null && demande.getPasseport().getNumero() != null) {
                        cs.showText("N° passeport: " + demande.getPasseport().getNumero());
                        cs.newLineAtOffset(0, -14);
                    }

                    if (p.getAdresse() != null && !p.getAdresse().isBlank()) {
                        cs.showText("Adresse: " + p.getAdresse());
                        cs.newLineAtOffset(0, -14);
                    }
                }

                // Passeport summary
                if (demande.getPasseport() != null) {
                    com.visa.entity.Passeport passeport = demande.getPasseport();
                    cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    cs.showText("Passeport:");
                    cs.newLineAtOffset(0, -14);
                    cs.setFont(PDType1Font.HELVETICA, 11);

                    if (passeport.getNumero() != null) {
                        cs.showText("Numéro: " + passeport.getNumero());
                        cs.newLineAtOffset(0, -14);
                    }
                    if (passeport.getDateExpiration() != null) {
                        cs.showText("Date d'expiration: " + df.format(passeport.getDateExpiration()));
                        cs.newLineAtOffset(0, -14);
                    }
                }

                // Visa transformable
                if (demande.getPasseport() != null && demande.getPasseport().getPersonne() != null) {
                    Integer personneId = demande.getPasseport().getPersonne().getId();
                    com.visa.entity.VisaTransformable vt = demandeService.getVisaTransformableByPersonneId(personneId);
                    if (vt != null) {
                        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        cs.showText("Visa transformable (source):");
                        cs.newLineAtOffset(0, -14);
                        cs.setFont(PDType1Font.HELVETICA, 11);

                        if (vt.getNumero() != null) {
                            cs.showText("Numéro: " + vt.getNumero());
                            cs.newLineAtOffset(0, -14);
                        }
                        if (vt.getDateArrivee() != null) {
                            cs.showText("Date d'entrée: " + df.format(vt.getDateArrivee()));
                            cs.newLineAtOffset(0, -14);
                        }
                        if (vt.getDateExpiration() != null) {
                            cs.showText("Date d'expiration: " + df.format(vt.getDateExpiration()));
                            cs.newLineAtOffset(0, -14);
                        }
                    }
                }

                cs.endText();
            }

            // Build a professional table listing documents with captions and thumbnails
            if (fichiers != null && !fichiers.isEmpty()) {
                float marginLeft = 50;
                float marginRight = 545;
                float tableWidth = marginRight - marginLeft;
                float col1Width = 260; // description column
                float col2Width = tableWidth - col1Width; // thumbnail column
                float rowHeight = 120;

                PDPage tablePage = new PDPage(PDRectangle.A4);
                doc.addPage(tablePage);
                PDPageContentStream csTable = new PDPageContentStream(doc, tablePage);

                // Table header
                float y = 720;
                csTable.setStrokingColor(Color.DARK_GRAY);
                csTable.setLineWidth(0.8f);
                csTable.beginText();
                csTable.setFont(PDType1Font.HELVETICA_BOLD, 12);
                csTable.newLineAtOffset(marginLeft + 2, y);
                csTable.showText("Documents fournis");
                csTable.endText();

                y -= 18;

                // Draw column titles
                csTable.beginText();
                csTable.setFont(PDType1Font.HELVETICA_BOLD, 10);
                csTable.newLineAtOffset(marginLeft + 2, y);
                csTable.showText("Description");
                csTable.newLineAtOffset(col1Width, 0);
                csTable.showText("Aperçu");
                csTable.endText();

                y -= 12;

                for (FichierUploade f : fichiers) {
                    if (y - rowHeight < 60) {
                        csTable.close();
                        tablePage = new PDPage(PDRectangle.A4);
                        doc.addPage(tablePage);
                        csTable = new PDPageContentStream(doc, tablePage);
                        y = 720;
                    }

                    String caption = "Document";
                    if (f.getDossierProfessionnel() != null && f.getDossierProfessionnel().getChampFournir() != null
                            && f.getDossierProfessionnel().getChampFournir().getLibelle() != null) {
                        caption = f.getDossierProfessionnel().getChampFournir().getLibelle();
                    }

                    // Row border
                    csTable.addRect(marginLeft, y - rowHeight, tableWidth, rowHeight);
                    csTable.stroke();

                    // Description column
                    csTable.beginText();
                    csTable.setFont(PDType1Font.HELVETICA_BOLD, 11);
                    csTable.newLineAtOffset(marginLeft + 6, y - 18);
                    csTable.showText(caption);
                    csTable.endText();

                    // If available, show additional info (file path or valeur)
                    if (f.getValeur() != null && !f.getValeur().isBlank()) {
                        String info = f.getValeur();
                        csTable.beginText();
                        csTable.setFont(PDType1Font.HELVETICA, 9);
                        csTable.newLineAtOffset(marginLeft + 6, y - 36);
                        // Truncate long paths for display
                        if (info.length() > 80) {
                            info = info.substring(0, 77) + "...";
                        }
                        csTable.showText(info);
                        csTable.endText();
                    }

                    // Thumbnail column
                    try {
                        String valeur = f.getValeur();
                        if (valeur != null) {
                            Path path = APP_ROOT.resolve(valeur).normalize();
                            File file = path.toFile();
                            if (file.exists()) {
                                BufferedImage thumbImg = ImageIO.read(file);
                                if (thumbImg != null) {
                                    PDImageXObject pdThumb = LosslessFactory.createFromImage(doc, thumbImg);
                                    float imgW = pdThumb.getWidth();
                                    float imgH = pdThumb.getHeight();

                                    float maxW = col2Width - 20;
                                    float maxH = rowHeight - 20;
                                    float scale = Math.min(maxW / imgW, maxH / imgH);
                                    float drawW = imgW * scale;
                                    float drawH = imgH * scale;

                                    float imgX = marginLeft + col1Width + (col2Width - drawW) / 2;
                                    float imgY = y - 10 - drawH;
                                    csTable.drawImage(pdThumb, imgX, imgY, drawW, drawH);
                                } else {
                                    // Non-image file: write type label
                                    csTable.beginText();
                                    csTable.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
                                    csTable.newLineAtOffset(marginLeft + col1Width + 10, y - 30);
                                    csTable.showText("Fichier (aperçu non disponible)");
                                    csTable.endText();
                                }
                            }
                        }
                    } catch (IOException ioe) {
                        // ignore thumbnail errors
                    }

                    y -= rowHeight + 8;
                }

                csTable.close();
            }

            // Append QR code page at the end if present
            if (demande.getQrcode() != null && demande.getQrcode().length > 0) {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(demande.getQrcode());
                    BufferedImage qimg = ImageIO.read(bais);
                    if (qimg != null) {
                        PDPage qrPage = new PDPage(PDRectangle.A4);
                        doc.addPage(qrPage);
                        PDImageXObject qrImage = LosslessFactory.createFromImage(doc, qimg);
                        try (PDPageContentStream cs = new PDPageContentStream(doc, qrPage)) {
                            cs.beginText();
                            cs.setFont(PDType1Font.HELVETICA_BOLD, 14);
                            cs.newLineAtOffset(50, 760);
                            cs.showText("QR Code:");
                            cs.endText();

                            PDRectangle mediaBox = qrPage.getMediaBox();
                            float pageHeight = mediaBox.getHeight();
                            float pageWidth = mediaBox.getWidth();

                            float imgWidth = qrImage.getWidth();
                            float imgHeight = qrImage.getHeight();
                            float scale = Math.min((pageWidth - 200) / imgWidth, (pageHeight - 300) / imgHeight);
                            float drawWidth = imgWidth * scale;
                            float drawHeight = imgHeight * scale;

                            float x = (pageWidth - drawWidth) / 2;
                            float y = (pageHeight - drawHeight) / 2 - 40;

                            cs.drawImage(qrImage, x, y, drawWidth, drawHeight);
                        }
                    }
                } catch (IOException e) {
                    // ignore QR rendering errors
                }
            }

            doc.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la generation du PDF: " + e.getMessage(), e);
        }
    }
}
