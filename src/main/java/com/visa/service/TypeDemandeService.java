package com.visa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.visa.entity.TypeDemande;
import com.visa.repository.TypeDemandeRepository;


@Service
public class TypeDemandeService {
    @Autowired
    private TypeDemandeRepository typeDemandeRepository;
    
    public List<TypeDemande> getTypesDemande() {
        return typeDemandeRepository.findAll();
    }

    public TypeDemande getById(Integer id) {
        return typeDemandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Type de demande introuvable"));
    }
}
