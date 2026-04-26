package com.visa.service;

import java.time.LocalDate;
public class UtilService {

    public static Integer parseInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static LocalDate parseLocalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Integer parseTypeDemandeId(String typeDemandeIdParam) {
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
