package com.xsh.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsh.user.model.User;
import com.xsh.user.service.UserService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setHandle("testuser");
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setBio("Test bio");
        testUser.setCreatedAt(OffsetDateTime.now());
    }

    @Test
    void getUsers_ShouldReturnPageOfUsers() throws Exception {
        Page<User> userPage = new PageImpl<>(Arrays.asList(testUser));
        when(userService.getUsers(0, 10)).thenReturn(userPage);

        mockMvc.perform(get("/api/users")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].handle").value(testUser.getHandle()));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userService.getUserById(userId)).thenReturn(testUser);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.handle").value(testUser.getHandle()));
    }

    @Test
    void getUserByHandle_WhenUserExists_ShouldReturnUser() throws Exception {
        when(userService.getUserByHandle(testUser.getHandle())).thenReturn(testUser);

        mockMvc.perform(get("/api/users/handle/{handle}", testUser.getHandle()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()));
    }

    @Test
    void createUser_WithValidData_ShouldReturnCreatedUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.handle").value(testUser.getHandle()));
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() throws Exception {
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.handle").value(testUser.getHandle()));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getUsersByIds_ShouldReturnListOfUsers() throws Exception {
        // Arrange
        List<UUID> userIds = Arrays.asList(userId);
        List<User> users = Arrays.asList(testUser);
        when(userService.getUsersByIds(userIds)).thenReturn(users);

        // Act & Assert
        mockMvc.perform(post("/api/users/ids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].handle").value(testUser.getHandle()));
    }
}