package com.visa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.visa.entity.TypeVisa;
import com.visa.repository.TypeVisaRepository;

@Service
public class TypeVisaService {
    @Autowired
    private TypeVisaRepository typeVisaRepository;

    public List<TypeVisa> getTypesVisa() {
        return typeVisaRepository.findAll();
    }
}
