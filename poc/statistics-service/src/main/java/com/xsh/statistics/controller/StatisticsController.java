package com.xsh.statistics.controller;

import com.xsh.statistics.model.Statistics;
import com.xsh.statistics.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Get statistics for a coo
     * 
     * @param cooId the ID of the coo
     * @return the statistics for the coo
     */
    @GetMapping("/coo/{cooId}")
    public ResponseEntity<Statistics> getCooStatistics(@PathVariable UUID cooId) {
        return ResponseEntity.ok(statisticsService.getCooStatistics(cooId));
    }

    /**
     * Create or update statistics for a coo
     * 
     * @param cooId the ID of the coo
     * @param statistics the statistics to create or update
     * @return the created or updated statistics
     */
    @PostMapping("/coo/{cooId}")
    public ResponseEntity<Statistics> createOrUpdateCooStatistics(
            @PathVariable UUID cooId,
            @RequestBody Statistics statistics) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statisticsService.createOrUpdateCooStatistics(cooId, statistics));
    }

    /**
     * Get statistics for a reply
     * 
     * @param replyId the ID of the reply
     * @return the statistics for the reply
     */
    @GetMapping("/reply/{replyId}")
    public ResponseEntity<Statistics> getReplyStatistics(@PathVariable UUID replyId) {
        return ResponseEntity.ok(statisticsService.getReplyStatistics(replyId));
    }

    /**
     * Create or update statistics for a reply
     * 
     * @param replyId the ID of the reply
     * @param statistics the statistics to create or update
     * @return the created or updated statistics
     */
    @PostMapping("/reply/{replyId}")
    public ResponseEntity<Statistics> createOrUpdateReplyStatistics(
            @PathVariable UUID replyId,
            @RequestBody Statistics statistics) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(statisticsService.createOrUpdateReplyStatistics(replyId, statistics));
    }

    /**
     * Get statistics for multiple coos
     * 
     * @param cooIds the list of coo IDs
     * @return the list of statistics for the coos
     */
    @PostMapping("/coos")
    public ResponseEntity<List<Statistics>> getCoosStatistics(@RequestBody List<UUID> cooIds) {
        return ResponseEntity.ok(statisticsService.getCoosStatistics(cooIds));
    }

    /**
     * Get statistics for multiple replies
     * 
     * @param replyIds the list of reply IDs
     * @return the list of statistics for the replies
     */
    @PostMapping("/replies")
    public ResponseEntity<List<Statistics>> getRepliesStatistics(@RequestBody List<UUID> replyIds) {
        return ResponseEntity.ok(statisticsService.getRepliesStatistics(replyIds));
    }
}