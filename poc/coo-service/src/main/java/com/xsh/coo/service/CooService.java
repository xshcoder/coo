package com.xsh.coo.service;

import com.xsh.coo.model.Coo;
import com.xsh.coo.repository.CooRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class CooService {

    private final CooRepository cooRepository;

    public CooService(CooRepository cooRepository) {
        this.cooRepository = cooRepository;
    }

    public List<Coo> getAllCoos() {
        return cooRepository.findAll();
    }

    public Coo getCooById(UUID id) {
        return cooRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coo not found"));
    }

    public Page<Coo> getCoosByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cooRepository.findByUserId(userId, pageable);
    }

    public Coo createCoo(Coo coo) {
        if (coo.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New coo should not have an ID");
        }
        return cooRepository.save(coo);
    }

    public Coo updateCoo(UUID id, Coo coo) {
        if (!cooRepository.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coo not found");
        }
        coo.setId(id);
        return cooRepository.save(coo);
    }

    public void deleteCoo(UUID id) {
        if (!cooRepository.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coo not found");
        }
        cooRepository.deleteById(id);
    }
}