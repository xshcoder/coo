package com.xsh.coo.controller;

import com.xsh.coo.model.Coo;
import com.xsh.coo.service.CooService;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CooController.class)
public class CooControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CooService cooService;

    private Coo testCoo;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testCoo = new Coo();
        testCoo.setId(UUID.randomUUID());
        testCoo.setUserId(userId);
        testCoo.setContent("Test coo content");
        testCoo.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    void createCoo_WithValidData_ReturnsCreatedCoo() throws Exception {
        when(cooService.createCoo(any(Coo.class)))
                .thenReturn(testCoo);

        mockMvc.perform(post("/api/coos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"" + userId + "\", \"content\": \"Test coo content\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test coo content"))
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    void getCoo_WithValidId_ReturnsCoo() throws Exception {
        when(cooService.getCooById(any(UUID.class)))
                .thenReturn(testCoo);

        mockMvc.perform(get("/api/coos/" + testCoo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test coo content"));
    }

    @Test
    void getUserCoos_WithValidUserId_ReturnsCoosList() throws Exception {
        List<Coo> coos = Arrays.asList(testCoo);
        Page<Coo> cooPage = new PageImpl<>(coos);

        when(cooService.getCoosByUserId(any(UUID.class), anyInt(), anyInt()))
                .thenReturn(cooPage);

        mockMvc.perform(get("/api/coos/user/" + userId)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("Test coo content"));
    }

    @Test
    void deleteCoo_WithValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/coos/" + testCoo.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateCoo_WithValidData_ReturnsUpdatedCoo() throws Exception {
        when(cooService.updateCoo(any(UUID.class), any(Coo.class)))
                .thenReturn(testCoo);

        mockMvc.perform(put("/api/coos/" + testCoo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"" + userId + "\", \"content\": \"Updated coo content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test coo content"))
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }
}