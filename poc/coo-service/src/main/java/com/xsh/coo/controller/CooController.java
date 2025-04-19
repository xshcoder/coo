package com.xsh.coo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.xsh.coo.model.Coo;
import com.xsh.coo.service.CooService;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/coos")
public class CooController {

    private final CooService cooService;

    public CooController(CooService cooService) {
        this.cooService = cooService;
    }

    @GetMapping
    public List<Coo> getAllCoos() {
        return cooService.getAllCoos();
    }

    @GetMapping("/{id}")
    public Coo getCooById(@PathVariable UUID id) {
        return cooService.getCooById(id);
    }

    @GetMapping("/user/{userId}")
    public Page<Coo> getCoosByUserId(@PathVariable UUID userId, 
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return cooService.getCoosByUserId(userId, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Coo createCoo(@RequestBody Coo coo) {
        return cooService.createCoo(coo);
    }

    @PutMapping("/{id}")
    public Coo updateCoo(@PathVariable UUID id, @RequestBody Coo coo) {
        return cooService.updateCoo(id, coo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoo(@PathVariable UUID id) {
        cooService.deleteCoo(id);
    }
}