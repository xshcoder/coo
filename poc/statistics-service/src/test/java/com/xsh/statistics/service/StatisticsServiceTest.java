package com.xsh.statistics.service;

import com.xsh.statistics.model.Statistics;
import com.xsh.statistics.model.StatisticsSubjectType;
import com.xsh.statistics.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
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
    void getCooStatistics_WhenStatisticsExists_ShouldReturnStatistics() {
        // Arrange
        when(statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO))
                .thenReturn(Optional.of(cooStatistics));

        // Act
        Statistics result = statisticsService.getCooStatistics(cooId);

        // Assert
        assertEquals(cooId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.COO, result.getSubjectType());
        assertEquals(10, result.getRepliesCount());
        assertEquals(20, result.getLikesCount());
        assertEquals(30, result.getViewsCount());
    }

    @Test
    void getCooStatistics_WhenStatisticsDoesNotExist_ShouldReturnNewStatistics() {
        // Arrange
        when(statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO))
                .thenReturn(Optional.empty());

        // Act
        Statistics result = statisticsService.getCooStatistics(cooId);

        // Assert
        assertEquals(cooId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.COO, result.getSubjectType());
        assertEquals(0, result.getRepliesCount());
        assertEquals(0, result.getLikesCount());
        assertEquals(0, result.getViewsCount());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void getReplyStatistics_WhenStatisticsExists_ShouldReturnStatistics() {
        // Arrange
        when(statisticsRepository.findBySubjectIdAndSubjectType(replyId, StatisticsSubjectType.REPLY))
                .thenReturn(Optional.of(replyStatistics));

        // Act
        Statistics result = statisticsService.getReplyStatistics(replyId);

        // Assert
        assertEquals(replyId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.REPLY, result.getSubjectType());
        assertEquals(5, result.getRepliesCount());
        assertEquals(15, result.getLikesCount());
        assertEquals(25, result.getViewsCount());
    }

    @Test
    void getReplyStatistics_WhenStatisticsDoesNotExist_ShouldReturnNewStatistics() {
        // Arrange
        when(statisticsRepository.findBySubjectIdAndSubjectType(replyId, StatisticsSubjectType.REPLY))
                .thenReturn(Optional.empty());

        // Act
        Statistics result = statisticsService.getReplyStatistics(replyId);

        // Assert
        assertEquals(replyId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.REPLY, result.getSubjectType());
        assertEquals(0, result.getRepliesCount());
        assertEquals(0, result.getLikesCount());
        assertEquals(0, result.getViewsCount());
        assertNotNull(result.getUpdatedAt());
    }

    @Test
    void createOrUpdateCooStatistics_WhenStatisticsExists_ShouldUpdateStatistics() {
        // Arrange
        Statistics inputStats = new Statistics();
        inputStats.setRepliesCount(15);
        inputStats.setLikesCount(25);
        inputStats.setViewsCount(35);

        Statistics updatedStats = new Statistics();
        updatedStats.setId(cooStatistics.getId());
        updatedStats.setSubjectId(cooId);
        updatedStats.setSubjectType(StatisticsSubjectType.COO);
        updatedStats.setRepliesCount(15);
        updatedStats.setLikesCount(25);
        updatedStats.setViewsCount(35);
        updatedStats.setUpdatedAt(OffsetDateTime.now());

        when(statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO))
                .thenReturn(Optional.of(cooStatistics));
        when(statisticsRepository.save(any(Statistics.class))).thenReturn(updatedStats);

        // Act
        Statistics result = statisticsService.createOrUpdateCooStatistics(cooId, inputStats);

        // Assert
        assertEquals(cooId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.COO, result.getSubjectType());
        assertEquals(15, result.getRepliesCount());
        assertEquals(25, result.getLikesCount());
        assertEquals(35, result.getViewsCount());
        verify(statisticsRepository).save(any(Statistics.class));
    }

    @Test
    void createOrUpdateCooStatistics_WhenStatisticsDoesNotExist_ShouldCreateStatistics() {
        // Arrange
        Statistics inputStats = new Statistics();
        inputStats.setRepliesCount(15);
        inputStats.setLikesCount(25);
        inputStats.setViewsCount(35);

        Statistics savedStats = new Statistics();
        savedStats.setId(UUID.randomUUID());
        savedStats.setSubjectId(cooId);
        savedStats.setSubjectType(StatisticsSubjectType.COO);
        savedStats.setRepliesCount(15);
        savedStats.setLikesCount(25);
        savedStats.setViewsCount(35);
        savedStats.setUpdatedAt(OffsetDateTime.now());

        when(statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO))
                .thenReturn(Optional.empty());
        when(statisticsRepository.save(any(Statistics.class))).thenReturn(savedStats);

        // Act
        Statistics result = statisticsService.createOrUpdateCooStatistics(cooId, inputStats);

        // Assert
        assertEquals(cooId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.COO, result.getSubjectType());
        assertEquals(15, result.getRepliesCount());
        assertEquals(25, result.getLikesCount());
        assertEquals(35, result.getViewsCount());
        verify(statisticsRepository).save(any(Statistics.class));
    }

    @Test
    void createOrUpdateReplyStatistics_WhenStatisticsExists_ShouldUpdateStatistics() {
        // Arrange
        Statistics inputStats = new Statistics();
        inputStats.setRepliesCount(10);
        inputStats.setLikesCount(20);
        inputStats.setViewsCount(30);

        Statistics updatedStats = new Statistics();
        updatedStats.setId(replyStatistics.getId());
        updatedStats.setSubjectId(replyId);
        updatedStats.setSubjectType(StatisticsSubjectType.REPLY);
        updatedStats.setRepliesCount(10);
        updatedStats.setLikesCount(20);
        updatedStats.setViewsCount(30);
        updatedStats.setUpdatedAt(OffsetDateTime.now());

        when(statisticsRepository.findBySubjectIdAndSubjectType(replyId, StatisticsSubjectType.REPLY))
                .thenReturn(Optional.of(replyStatistics));
        when(statisticsRepository.save(any(Statistics.class))).thenReturn(updatedStats);

        // Act
        Statistics result = statisticsService.createOrUpdateReplyStatistics(replyId, inputStats);

        // Assert
        assertEquals(replyId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.REPLY, result.getSubjectType());
        assertEquals(10, result.getRepliesCount());
        assertEquals(20, result.getLikesCount());
        assertEquals(30, result.getViewsCount());
        verify(statisticsRepository).save(any(Statistics.class));
    }

    @Test
    void createOrUpdateReplyStatistics_WhenStatisticsDoesNotExist_ShouldCreateStatistics() {
        // Arrange
        Statistics inputStats = new Statistics();
        inputStats.setRepliesCount(10);
        inputStats.setLikesCount(20);
        inputStats.setViewsCount(30);

        Statistics savedStats = new Statistics();
        savedStats.setId(UUID.randomUUID());
        savedStats.setSubjectId(replyId);
        savedStats.setSubjectType(StatisticsSubjectType.REPLY);
        savedStats.setRepliesCount(10);
        savedStats.setLikesCount(20);
        savedStats.setViewsCount(30);
        savedStats.setUpdatedAt(OffsetDateTime.now());

        when(statisticsRepository.findBySubjectIdAndSubjectType(replyId, StatisticsSubjectType.REPLY))
                .thenReturn(Optional.empty());
        when(statisticsRepository.save(any(Statistics.class))).thenReturn(savedStats);

        // Act
        Statistics result = statisticsService.createOrUpdateReplyStatistics(replyId, inputStats);

        // Assert
        assertEquals(replyId, result.getSubjectId());
        assertEquals(StatisticsSubjectType.REPLY, result.getSubjectType());
        assertEquals(10, result.getRepliesCount());
        assertEquals(20, result.getLikesCount());
        assertEquals(30, result.getViewsCount());
        verify(statisticsRepository).save(any(Statistics.class));
    }

    @Test
    void getCoosStatistics_WhenStatisticsExist_ShouldReturnStatistics() {
        // Arrange
        UUID cooId2 = UUID.randomUUID();
        
        Statistics cooStatistics2 = new Statistics();
        cooStatistics2.setId(UUID.randomUUID());
        cooStatistics2.setSubjectId(cooId2);
        cooStatistics2.setSubjectType(StatisticsSubjectType.COO);
        cooStatistics2.setRepliesCount(5);
        cooStatistics2.setLikesCount(10);
        cooStatistics2.setViewsCount(15);
        cooStatistics2.setUpdatedAt(OffsetDateTime.now());
        
        List<UUID> cooIds = Arrays.asList(cooId, cooId2);
        List<Statistics> expectedStatistics = Arrays.asList(cooStatistics, cooStatistics2);
        
        when(statisticsRepository.findBySubjectIdsAndSubjectType(cooIds, StatisticsSubjectType.COO))
                .thenReturn(expectedStatistics);
        
        // Act
        List<Statistics> result = statisticsService.getCoosStatistics(cooIds);
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(cooId, result.get(0).getSubjectId());
        assertEquals(cooId2, result.get(1).getSubjectId());
        assertEquals(10, result.get(0).getRepliesCount());
        assertEquals(5, result.get(1).getRepliesCount());
    }

    @Test
    void getCoosStatistics_WhenSomeStatisticsDoNotExist_ShouldReturnMixedStatistics() {
        // Arrange
        UUID cooId2 = UUID.randomUUID();
        List<UUID> cooIds = Arrays.asList(cooId, cooId2);
        
        when(statisticsRepository.findBySubjectIdsAndSubjectType(cooIds, StatisticsSubjectType.COO))
                .thenReturn(Collections.singletonList(cooStatistics));
        
        // Act
        List<Statistics> result = statisticsService.getCoosStatistics(cooIds);
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(cooId, result.get(0).getSubjectId());
        assertEquals(cooId2, result.get(1).getSubjectId());
        assertEquals(10, result.get(0).getRepliesCount());
        assertEquals(0, result.get(1).getRepliesCount());
    }

    @Test
    void getCoosStatistics_WhenNoStatisticsExist_ShouldReturnDefaultStatistics() {
        // Arrange
        UUID cooId2 = UUID.randomUUID();
        List<UUID> cooIds = Arrays.asList(cooId, cooId2);
        
        when(statisticsRepository.findBySubjectIdsAndSubjectType(cooIds, StatisticsSubjectType.COO))
                .thenReturn(Collections.emptyList());
        
        // Act
        List<Statistics> result = statisticsService.getCoosStatistics(cooIds);
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(cooId, result.get(0).getSubjectId());
        assertEquals(cooId2, result.get(1).getSubjectId());
        assertEquals(0, result.get(0).getRepliesCount());
        assertEquals(0, result.get(1).getRepliesCount());
    }

    @Test
    void getRepliesStatistics_WhenStatisticsExist_ShouldReturnStatistics() {
        // Arrange
        UUID replyId2 = UUID.randomUUID();
        
        Statistics replyStatistics2 = new Statistics();
        replyStatistics2.setId(UUID.randomUUID());
        replyStatistics2.setSubjectId(replyId2);
        replyStatistics2.setSubjectType(StatisticsSubjectType.REPLY);
        replyStatistics2.setRepliesCount(3);
        replyStatistics2.setLikesCount(8);
        replyStatistics2.setViewsCount(12);
        replyStatistics2.setUpdatedAt(OffsetDateTime.now());
        
        List<UUID> replyIds = Arrays.asList(replyId, replyId2);
        List<Statistics> expectedStatistics = Arrays.asList(replyStatistics, replyStatistics2);
        
        when(statisticsRepository.findBySubjectIdsAndSubjectType(replyIds, StatisticsSubjectType.REPLY))
                .thenReturn(expectedStatistics);
        
        // Act
        List<Statistics> result = statisticsService.getRepliesStatistics(replyIds);
        
        // Assert
        assertEquals(2, result.size());
        assertEquals(replyId, result.get(0).getSubjectId());
        assertEquals(replyId2, result.get(1).getSubjectId());
        assertEquals(5, result.get(0).getRepliesCount());
        assertEquals(3, result.get(1).getRepliesCount());
    }

    @Test
    void getRepliesStatistics_WhenEmptyList_ShouldReturnEmptyList() {
        // Arrange
        List<UUID> emptyList = Collections.emptyList();
        
        // Act
        List<Statistics> result = statisticsService.getRepliesStatistics(emptyList);
        
        // Assert
        assertTrue(result.isEmpty());
    }
}