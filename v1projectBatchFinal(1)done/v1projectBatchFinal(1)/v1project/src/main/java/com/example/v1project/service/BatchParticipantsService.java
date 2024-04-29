package com.example.v1project.service;

import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Users;

import java.util.List;
import java.util.Optional;

public interface BatchParticipantsService {
    List<Users> getParticipantsByBatchId(long batchId);
    Optional<BatchParticipants> getBatchParticipantById(long userId); // New method
    //    String deleteBatchParticipant(BatchParticipants batchParticipant); // New method
    void deleteParticipantFromBatch(long batchId, long userId);
    //    void addBatchParticipant(BatchParticipants batchParticipants);
    void addBatchParticipant(long userId, long batchId);
    void deleteParticipantsByBatchId(long batchId);


}
