package com.example.v1project.dao;

import com.example.v1project.dto.BatchParticipants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchParticipantsDao extends JpaRepository<BatchParticipants, Long> {
    List<BatchParticipants> findByBatches_BatchId(long batchId);
    List<BatchParticipants> findByBatches_BatchIdAndUserId(long batchId, long userId);
    void deleteByBatches_BatchIdAndUserId(long batchId, long userId);
    int countByBatches_BatchId(long batchId);
    int countByBatchesBatchIdAndUserId(int batchId, int userId);
    boolean existsByBatchesBatchIdAndUserId(int batchId, int userId);
    List<BatchParticipants> findByUserId(int userId);
}
