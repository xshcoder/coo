package com.xsh.statistics.service;

import com.xsh.statistics.model.Statistics;
import com.xsh.statistics.model.StatisticsSubjectType;
import com.xsh.statistics.repository.StatisticsRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    public StatisticsService(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    /**
     * Get statistics for a coo
     * 
     * @param cooId the ID of the coo
     * @return the statistics for the coo
     */
    public Statistics getCooStatistics(UUID cooId) {
        return statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO)
                .orElse(new Statistics(null, cooId, StatisticsSubjectType.COO, 0, 0, 0, OffsetDateTime.now()));
    }

    /**
     * Get statistics for a reply
     * 
     * @param replyId the ID of the reply
     * @return the statistics for the reply
     */
    public Statistics getReplyStatistics(UUID replyId) {
        return statisticsRepository.findBySubjectIdAndSubjectType(replyId, StatisticsSubjectType.REPLY)
                .orElse(new Statistics(null, replyId, StatisticsSubjectType.REPLY, 0, 0, 0, OffsetDateTime.now()));
    }

    /**
     * Create or update statistics for a coo
     * 
     * @param cooId the ID of the coo
     * @param statistics the statistics to create or update
     * @return the created or updated statistics
     */
    public Statistics createOrUpdateCooStatistics(UUID cooId, Statistics statistics) {
        statistics.setSubjectId(cooId);
        statistics.setSubjectType(StatisticsSubjectType.COO);
        statistics.setUpdatedAt(OffsetDateTime.now());
        
        return statisticsRepository.findBySubjectIdAndSubjectType(cooId, StatisticsSubjectType.COO)
                .map(existingStats -> {
                    existingStats.setRepliesCount(statistics.getRepliesCount());
                    existingStats.setLikesCount(statistics.getLikesCount());
                    existingStats.setViewsCount(statistics.getViewsCount());
                    existingStats.setUpdatedAt(OffsetDateTime.now());
                    return statisticsRepository.save(existingStats);
                })
                .orElseGet(() -> statisticsRepository.save(statistics));
    }

    /**
     * Create or update statistics for a reply
     * 
     * @param replyId the ID of the reply
     * @param statistics the statistics to create or update
     * @return the created or updated statistics
     */
    public Statistics createOrUpdateReplyStatistics(UUID replyId, Statistics statistics) {
        statistics.setSubjectId(replyId);
        statistics.setSubjectType(StatisticsSubjectType.REPLY);
        statistics.setUpdatedAt(OffsetDateTime.now());
        
        return statisticsRepository.findBySubjectIdAndSubjectType(replyId, StatisticsSubjectType.REPLY)
                .map(existingStats -> {
                    existingStats.setRepliesCount(statistics.getRepliesCount());
                    existingStats.setLikesCount(statistics.getLikesCount());
                    existingStats.setViewsCount(statistics.getViewsCount());
                    existingStats.setUpdatedAt(OffsetDateTime.now());
                    return statisticsRepository.save(existingStats);
                })
                .orElseGet(() -> statisticsRepository.save(statistics));
    }

    /**
     * Get statistics for multiple coos
     * 
     * @param cooIds the list of coo IDs
     * @return the list of statistics for the coos
     */
    public List<Statistics> getCoosStatistics(List<UUID> cooIds) {
        if (cooIds == null || cooIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Statistics> existingStatistics = statisticsRepository.findBySubjectIdsAndSubjectType(
                cooIds, StatisticsSubjectType.COO);
        
        // Create a map of existing statistics by subject ID for quick lookup
        Map<UUID, Statistics> statisticsMap = existingStatistics.stream()
                .collect(Collectors.toMap(Statistics::getSubjectId, s -> s));
        
        // Create a list of statistics for all requested IDs
        List<Statistics> result = new ArrayList<>();
        for (UUID cooId : cooIds) {
            if (statisticsMap.containsKey(cooId)) {
                result.add(statisticsMap.get(cooId));
            } else {
                // Create default statistics for coos that don't have statistics yet
                result.add(new Statistics(null, cooId, StatisticsSubjectType.COO, 0, 0, 0, OffsetDateTime.now()));
            }
        }
        
        return result;
    }

    /**
     * Get statistics for multiple replies
     * 
     * @param replyIds the list of reply IDs
     * @return the list of statistics for the replies
     */
    public List<Statistics> getRepliesStatistics(List<UUID> replyIds) {
        if (replyIds == null || replyIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Statistics> existingStatistics = statisticsRepository.findBySubjectIdsAndSubjectType(
                replyIds, StatisticsSubjectType.REPLY);
        
        // Create a map of existing statistics by subject ID for quick lookup
        Map<UUID, Statistics> statisticsMap = existingStatistics.stream()
                .collect(Collectors.toMap(Statistics::getSubjectId, s -> s));
        
        // Create a list of statistics for all requested IDs
        List<Statistics> result = new ArrayList<>();
        for (UUID replyId : replyIds) {
            if (statisticsMap.containsKey(replyId)) {
                result.add(statisticsMap.get(replyId));
            } else {
                // Create default statistics for replies that don't have statistics yet
                result.add(new Statistics(null, replyId, StatisticsSubjectType.REPLY, 0, 0, 0, OffsetDateTime.now()));
            }
        }
        
        return result;
    }
}