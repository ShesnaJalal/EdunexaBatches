package com.example.v1project.dao;

import com.example.v1project.dto.BatchParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchParticipantsDao extends JpaRepository<BatchParticipants, Long> {
    List<BatchParticipants> findByBatches_BatchId(long batchId);
    List<BatchParticipants> findByBatches_BatchIdAndUsers_UserId(long batchId, long userId);
    void deleteByBatches_BatchIdAndUsers_UserId(long batchId, long userId);
    int countByBatches_BatchId(long batchId);
    int countByBatchesBatchIdAndUsersUserId(int batchId, int userId);
    boolean existsByBatchesBatchIdAndUsersUserId(int batchId, int userId); // Define the method here// Define the method here

}
