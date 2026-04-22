package com.visa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.visa.entity.Nationalite;
import com.visa.repository.NationaliteRepository;

@Service
public class NationaliteService {
    @Autowired
    private NationaliteRepository nationaliteRepository;

    public List<Nationalite> getNationalites() {
        return nationaliteRepository.findAll();
    }
}
