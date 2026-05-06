package com.visa.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class QrCodeService {

    @Value("${app.vue.url:http://localhost:5173}")
    private String vueAppUrl;

    /**
     * Génère un QR Code en tant que bytes (PNG)
     * Le QR Code encode une URL vers l'application Vue avec l'ID de la demande
     * 
     * @param demandeId ID de la demande à encoder
     * @return Tableau de bytes du PNG du QR Code
     * @throws WriterException Si la génération du QR Code échoue
     * @throws IOException Si la conversion en PNG échoue
     */
    public byte[] generateQrCodeBytes(Integer demandeId) throws WriterException, IOException {
        // URL vers l'application Vue à la page d'accueil avec l'ID de la demande
        String donnees = vueAppUrl + "/?demandeId=" + demandeId;

        // Génération du QR Code avec zxing
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(donnees, BarcodeFormat.QR_CODE, 300, 300);
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Conversion en bytes PNG
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", baos);
        return baos.toByteArray();
    }
}
