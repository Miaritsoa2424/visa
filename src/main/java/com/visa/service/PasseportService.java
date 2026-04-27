package com.visa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.entity.Passeport;
import com.visa.repository.PasseportRepository;

@Service
public class PasseportService {
    @Autowired
    private PasseportRepository passeportRepository;

    public boolean checkPasseport(Passeport passeport) {
        if (passeport == null) {
            throw new IllegalArgumentException("Le passeport ne peut pas être null.");
        }

        Passeport existingPasseport = passeportRepository.findById(passeport.getId()).get();

        if (null == existingPasseport) {
            return false;
            
        }
        return true;
    }

    public Passeport createPasseport(Passeport passeport) {
        passeport.validateRequiredFields();
        if (checkPasseport(passeport)) {
            throw new IllegalArgumentException("Le passeport avec l'id " + passeport.getId() + " existe déjà.");
        }
        passeportRepository.save(passeport);
        return passeport; 
    }
}
