package com.xsh.search.controller;

import com.xsh.search.model.User;
import com.xsh.search.model.Coo;
import com.xsh.search.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private User testUser;
    private Coo testCoo;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setHandle("testuser");
        testUser.setName("Test User");

        testCoo = new Coo();
        testCoo.setId(UUID.randomUUID());
        testCoo.setContent("Test coo content");
        testCoo.setUserId(testUser.getId());
    }

    @Test
    void searchUsers_WithValidText_ReturnsMatchingUsers() throws Exception {
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users);

        when(searchService.searchUsers(anyString(), anyInt(), anyInt()))
                .thenReturn(userPage);

        mockMvc.perform(get("/search/users")
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].handle").value("testuser"))
                .andExpect(jsonPath("$.content[0].name").value("Test User"));
    }

    @Test
    void searchUsers_WithEmptyText_ReturnsAllUsers() throws Exception {
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users);

        when(searchService.searchUsers(anyString(), anyInt(), anyInt()))
                .thenReturn(userPage);

        mockMvc.perform(get("/search/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].handle").value("testuser"));
    }

    @Test
    void searchCoos_WithValidText_ReturnsMatchingCoos() throws Exception {
        List<Coo> coos = Arrays.asList(testCoo);
        Page<Coo> cooPage = new PageImpl<>(coos);

        when(searchService.searchCoos(anyString(), anyInt(), anyInt()))
                .thenReturn(cooPage);

        mockMvc.perform(get("/search/coos")
                        .param("text", "test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("Test coo content"))
                .andExpect(jsonPath("$.content[0].userId").value(testUser.getId().toString()));
    }

    @Test
    void searchCoos_WithoutText_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/search/coos")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }
}