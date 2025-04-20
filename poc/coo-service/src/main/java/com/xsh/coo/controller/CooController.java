package com.xsh.coo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xsh.coo.model.Coo;
import com.xsh.coo.service.CooService;

import java.util.UUID;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/coos")
public class CooController {

    private final CooService cooService;

    public CooController(CooService cooService) {
        this.cooService = cooService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coo> getCooById(@PathVariable UUID id) {
        return ResponseEntity.ok(cooService.getCooById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Coo>> getCoosByUserId(@PathVariable UUID userId, 
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cooService.getCoosByUserId(userId, page, size));
    }

    @PostMapping
    public ResponseEntity<Coo> createCoo(@RequestBody Coo coo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cooService.createCoo(coo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coo> updateCoo(@PathVariable UUID id, @RequestBody Coo coo) {
        return ResponseEntity.ok(cooService.updateCoo(id, coo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoo(@PathVariable UUID id) {
        cooService.deleteCoo(id);
        return ResponseEntity.noContent().build();
    }
}