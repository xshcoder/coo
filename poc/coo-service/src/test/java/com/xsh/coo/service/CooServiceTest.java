package com.xsh.coo.service;

import com.xsh.coo.model.Coo;
import com.xsh.coo.repository.CooRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CooServiceTest {

    @Mock
    private CooRepository cooRepository;

    private CooService cooService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cooService = new CooService(cooRepository);
    }

    @Test
    void getCooById_WhenCooExists_ShouldReturnCoo() {
        // Arrange
        UUID id = UUID.randomUUID();
        Coo expectedCoo = new Coo();
        when(cooRepository.findById(id)).thenReturn(Optional.of(expectedCoo));

        // Act
        Coo actualCoo = cooService.getCooById(id);

        // Assert
        assertEquals(expectedCoo, actualCoo);
        verify(cooRepository).findById(id);
    }

    @Test
    void getCooById_WhenCooDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(cooRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cooService.getCooById(id));
        verify(cooRepository).findById(id);
    }

    @Test
    void createCoo_WhenCooHasNoId_ShouldSaveCoo() {
        // Arrange
        Coo coo = new Coo();
        Coo savedCoo = new Coo();
        when(cooRepository.save(any(Coo.class))).thenReturn(savedCoo);

        // Act
        Coo actualCoo = cooService.createCoo(coo);

        // Assert
        assertEquals(savedCoo, actualCoo);
        verify(cooRepository).save(coo);
    }

    @Test
    void createCoo_WhenCooHasId_ShouldThrowException() {
        // Arrange
        Coo coo = new Coo();
        coo.setId(UUID.randomUUID());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cooService.createCoo(coo));
        verify(cooRepository, never()).save(any());
    }

    @Test
    void updateCoo_WhenCooExists_ShouldUpdateCoo() {
        // Arrange
        UUID id = UUID.randomUUID();
        Coo coo = new Coo();
        Coo updatedCoo = new Coo();
        when(cooRepository.findById(id)).thenReturn(Optional.of(new Coo()));
        when(cooRepository.save(any(Coo.class))).thenReturn(updatedCoo);

        // Act
        Coo actualCoo = cooService.updateCoo(id, coo);

        // Assert
        assertEquals(updatedCoo, actualCoo);
        verify(cooRepository).findById(id);
        verify(cooRepository).save(coo);
    }

    @Test
    void updateCoo_WhenCooDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Coo coo = new Coo();
        when(cooRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cooService.updateCoo(id, coo));
        verify(cooRepository).findById(id);
        verify(cooRepository, never()).save(any());
    }

    @Test
    void deleteCoo_WhenCooExists_ShouldDeleteCoo() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(cooRepository.findById(id)).thenReturn(Optional.of(new Coo()));

        // Act
        cooService.deleteCoo(id);

        // Assert
        verify(cooRepository).findById(id);
        verify(cooRepository).deleteById(id);
    }

    @Test
    void deleteCoo_WhenCooDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(cooRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cooService.deleteCoo(id));
        verify(cooRepository).findById(id);
        verify(cooRepository, never()).deleteById(any());
    }
    
    @Test
    void getCoosByUserId_ShouldReturnPageOfCoos() {
        // Arrange
        UUID userId = UUID.randomUUID();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        
        List<Coo> coosList = Arrays.asList(new Coo(), new Coo());
        Page<Coo> expectedPage = new PageImpl<>(coosList, pageable, coosList.size());
        
        when(cooRepository.findByUserId(userId, pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Coo> actualPage = cooService.getCoosByUserId(userId, page, size);
        
        // Assert
        assertEquals(expectedPage, actualPage);
        verify(cooRepository).findByUserId(userId, pageable);
    }
}