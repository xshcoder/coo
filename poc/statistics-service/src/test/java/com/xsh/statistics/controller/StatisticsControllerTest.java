package com.xsh.statistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xsh.statistics.model.Statistics;
import com.xsh.statistics.model.StatisticsSubjectType;
import com.xsh.statistics.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatisticsService statisticsService;

    private UUID cooId;
    private UUID replyId;
    private Statistics cooStatistics;
    private Statistics replyStatistics;

    @BeforeEach
    void setUp() {
        cooId = UUID.randomUUID();
        replyId = UUID.randomUUID();

        cooStatistics = new Statistics();
        cooStatistics.setId(UUID.randomUUID());
        cooStatistics.setSubjectId(cooId);
        cooStatistics.setSubjectType(StatisticsSubjectType.COO);
        cooStatistics.setRepliesCount(10);
        cooStatistics.setLikesCount(20);
        cooStatistics.setViewsCount(30);
        cooStatistics.setUpdatedAt(OffsetDateTime.now());

        replyStatistics = new Statistics();
        replyStatistics.setId(UUID.randomUUID());
        replyStatistics.setSubjectId(replyId);
        replyStatistics.setSubjectType(StatisticsSubjectType.REPLY);
        replyStatistics.setRepliesCount(5);
        replyStatistics.setLikesCount(15);
        replyStatistics.setViewsCount(25);
        replyStatistics.setUpdatedAt(OffsetDateTime.now());
    }

    @Test
    void getCooStatistics_ShouldReturnStatistics() throws Exception {
        // Arrange
        when(statisticsService.getCooStatistics(cooId)).thenReturn(cooStatistics);

        // Act & Assert
        mockMvc.perform(get("/api/statistics/coo/{cooId}", cooId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectId").value(cooId.toString()))
                .andExpect(jsonPath("$.subjectType").value("COO"))
                .andExpect(jsonPath("$.repliesCount").value(10))
                .andExpect(jsonPath("$.likesCount").value(20))
                .andExpect(jsonPath("$.viewsCount").value(30));
    }

    @Test
    void getReplyStatistics_ShouldReturnStatistics() throws Exception {
        // Arrange
        when(statisticsService.getReplyStatistics(replyId)).thenReturn(replyStatistics);

        // Act & Assert
        mockMvc.perform(get("/api/statistics/reply/{replyId}", replyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subjectId").value(replyId.toString()))
                .andExpect(jsonPath("$.subjectType").value("REPLY"))
                .andExpect(jsonPath("$.repliesCount").value(5))
                .andExpect(jsonPath("$.likesCount").value(15))
                .andExpect(jsonPath("$.viewsCount").value(25));
    }

    @Test
    void createOrUpdateCooStatistics_ShouldReturnUpdatedStatistics() throws Exception {
        // Arrange
        Statistics inputStats = new Statistics();
        inputStats.setRepliesCount(15);
        inputStats.setLikesCount(25);
        inputStats.setViewsCount(35);

        Statistics updatedStats = new Statistics();
        updatedStats.setId(UUID.randomUUID());
        updatedStats.setSubjectId(cooId);
        updatedStats.setSubjectType(StatisticsSubjectType.COO);
        updatedStats.setRepliesCount(15);
        updatedStats.setLikesCount(25);
        updatedStats.setViewsCount(35);
        updatedStats.setUpdatedAt(OffsetDateTime.now());

        when(statisticsService.createOrUpdateCooStatistics(eq(cooId), any(Statistics.class)))
                .thenReturn(updatedStats);

        // Act & Assert
        mockMvc.perform(post("/api/statistics/coo/{cooId}", cooId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputStats)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subjectId").value(cooId.toString()))
                .andExpect(jsonPath("$.subjectType").value("COO"))
                .andExpect(jsonPath("$.repliesCount").value(15))
                .andExpect(jsonPath("$.likesCount").value(25))
                .andExpect(jsonPath("$.viewsCount").value(35));
    }

    @Test
    void createOrUpdateReplyStatistics_ShouldReturnUpdatedStatistics() throws Exception {
        // Arrange
        Statistics inputStats = new Statistics();
        inputStats.setRepliesCount(10);
        inputStats.setLikesCount(20);
        inputStats.setViewsCount(30);

        Statistics updatedStats = new Statistics();
        updatedStats.setId(UUID.randomUUID());
        updatedStats.setSubjectId(replyId);
        updatedStats.setSubjectType(StatisticsSubjectType.REPLY);
        updatedStats.setRepliesCount(10);
        updatedStats.setLikesCount(20);
        updatedStats.setViewsCount(30);
        updatedStats.setUpdatedAt(OffsetDateTime.now());

        when(statisticsService.createOrUpdateReplyStatistics(eq(replyId), any(Statistics.class)))
                .thenReturn(updatedStats);

        // Act & Assert
        mockMvc.perform(post("/api/statistics/reply/{replyId}", replyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputStats)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.subjectId").value(replyId.toString()))
                .andExpect(jsonPath("$.subjectType").value("REPLY"))
                .andExpect(jsonPath("$.repliesCount").value(10))
                .andExpect(jsonPath("$.likesCount").value(20))
                .andExpect(jsonPath("$.viewsCount").value(30));
    }

    @Test
    void getCoosStatistics_ShouldReturnStatisticsList() throws Exception {
        // Arrange
        UUID cooId2 = UUID.randomUUID();
        List<UUID> cooIds = Arrays.asList(cooId, cooId2);
        
        Statistics cooStatistics2 = new Statistics();
        cooStatistics2.setId(UUID.randomUUID());
        cooStatistics2.setSubjectId(cooId2);
        cooStatistics2.setSubjectType(StatisticsSubjectType.COO);
        cooStatistics2.setRepliesCount(5);
        cooStatistics2.setLikesCount(10);
        cooStatistics2.setViewsCount(15);
        cooStatistics2.setUpdatedAt(OffsetDateTime.now());
        
        List<Statistics> expectedStatistics = Arrays.asList(cooStatistics, cooStatistics2);
        
        when(statisticsService.getCoosStatistics(any())).thenReturn(expectedStatistics);
        
        // Act & Assert
        mockMvc.perform(post("/api/statistics/coos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cooIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].subjectId").value(cooId.toString()))
                .andExpect(jsonPath("$[0].subjectType").value("COO"))
                .andExpect(jsonPath("$[0].repliesCount").value(10))
                .andExpect(jsonPath("$[1].subjectId").value(cooId2.toString()))
                .andExpect(jsonPath("$[1].repliesCount").value(5));
    }

    @Test
    void getRepliesStatistics_ShouldReturnStatisticsList() throws Exception {
        // Arrange
        UUID replyId2 = UUID.randomUUID();
        List<UUID> replyIds = Arrays.asList(replyId, replyId2);
        
        Statistics replyStatistics2 = new Statistics();
        replyStatistics2.setId(UUID.randomUUID());
        replyStatistics2.setSubjectId(replyId2);
        replyStatistics2.setSubjectType(StatisticsSubjectType.REPLY);
        replyStatistics2.setRepliesCount(3);
        replyStatistics2.setLikesCount(8);
        replyStatistics2.setViewsCount(12);
        replyStatistics2.setUpdatedAt(OffsetDateTime.now());
        
        List<Statistics> expectedStatistics = Arrays.asList(replyStatistics, replyStatistics2);
        
        when(statisticsService.getRepliesStatistics(any())).thenReturn(expectedStatistics);
        
        // Act & Assert
        mockMvc.perform(post("/api/statistics/replies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].subjectId").value(replyId.toString()))
                .andExpect(jsonPath("$[0].subjectType").value("REPLY"))
                .andExpect(jsonPath("$[0].repliesCount").value(5))
                .andExpect(jsonPath("$[1].subjectId").value(replyId2.toString()))
                .andExpect(jsonPath("$[1].repliesCount").value(3));
    }
}