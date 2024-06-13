package com.example.v1project.service;

import com.example.v1project.dto.BatchParticipants;

import java.util.List;
import java.util.Optional;

public interface BatchParticipantsService {
    List<Integer> getParticipantsByBatchId(long batchId);
    Optional<BatchParticipants> getBatchParticipantById(long userId); // New method
    void deleteParticipantFromBatch(long batchId, long userId);
    void deleteParticipantsByBatchId(int batchId);
    int countParticipantsByBatchId(long batchId);
    boolean isParticipantInBatch(int userId, int batchId); // Add this method
    void addBatchParticipants(List<Integer> userIds, int batchId);

}
