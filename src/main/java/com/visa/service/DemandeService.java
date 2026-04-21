package com.visa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.entity.Demande;
import com.visa.entity.Nationalite;
import com.visa.entity.SituationFamiliale;
import com.visa.entity.TypeVisa;
import com.visa.repository.DemandeRepository;
import com.visa.repository.NationaliteRepository;
import com.visa.repository.SituationFamilialeRepository;
import com.visa.repository.TypeVisaRepository;

@Service
public class DemandeService {
    @Autowired
    private DemandeRepository demandeRepository;

    public List<Demande> getDemandes(){
        return demandeRepository.findAll();
    }

}
