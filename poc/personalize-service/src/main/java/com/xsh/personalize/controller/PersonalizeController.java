package com.xsh.personalize.controller;

import com.xsh.personalize.model.TimelineCursor;
import com.xsh.personalize.service.PersonalizeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/personalize")
public class PersonalizeController {

    private final PersonalizeService personalizeService;

    public PersonalizeController(PersonalizeService personalizeService) {
        this.personalizeService = personalizeService;
    }

    @GetMapping("/timeline/{userId}")
    public ResponseEntity<TimelineCursor> getTimeline(
            @PathVariable UUID userId,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(personalizeService.getTimeline(userId, cursor, limit));
    }
}