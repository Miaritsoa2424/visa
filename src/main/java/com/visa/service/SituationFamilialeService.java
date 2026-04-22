package com.visa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.visa.entity.SituationFamiliale;
import com.visa.repository.SituationFamilialeRepository;

@Service
public class SituationFamilialeService {
    @Autowired
    private SituationFamilialeRepository situationFamilialeRepository;

    public List<SituationFamiliale> getSituationsFamiliales() {
        return situationFamilialeRepository.findAll();
    }
}
