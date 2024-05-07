package com.example.v1project.service;

import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Users;

import java.util.List;
import java.util.Optional;

public interface BatchParticipantsService {
    List<Users> getParticipantsByBatchId(long batchId);
    Optional<BatchParticipants> getBatchParticipantById(long userId); // New method
    void deleteParticipantFromBatch(long batchId, long userId);
    void addBatchParticipant(int userId, int batchId);
    void deleteParticipantsByBatchId(int batchId);
    int countParticipantsByBatchId(long batchId);
    boolean isParticipantInBatch(int userId, int batchId); // Add this method

}
