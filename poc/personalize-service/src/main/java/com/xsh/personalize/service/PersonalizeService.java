package com.xsh.personalize.service;

import com.xsh.personalize.model.TimelineCursor;
import com.xsh.personalize.repository.PersonalizeRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.UUID;

@Service
public class PersonalizeService {

    private final PersonalizeRepository personalizeRepository;

    public PersonalizeService(PersonalizeRepository personalizeRepository) {
        this.personalizeRepository = personalizeRepository;
    }

    public TimelineCursor getTimeline(UUID userId, String cursor, int limit) {
        String decodedCursor = cursor != null ? new String(Base64.getDecoder().decode(cursor)) : null;
        return personalizeRepository.getTimeline(userId, decodedCursor, limit);
    }
}