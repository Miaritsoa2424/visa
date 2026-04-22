package com.visa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.visa.entity.ChampFournir;
import com.visa.repository.ChampFournirRepository;

@Service
public class ChampFournirService {

    @Autowired
    private ChampFournirRepository champFournirRepository;

    public List<ChampFournir> getByTypeVisaId(Integer typeVisaId) {
        return champFournirRepository.findByTypeVisaId(typeVisaId);
    }
}
