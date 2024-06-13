package com.example.v1project.service;

import com.example.v1project.dao.BatchDao;
import com.example.v1project.dao.BatchParticipantsDao;
import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Batches;
import com.example.v1project.utility.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatchParticipantsServiceImpl implements BatchParticipantsService {

    @Autowired
    private BatchParticipantsDao batchParticipantsDao;

    @Autowired
    private BatchDao batchesRepository;
    private final ServiceSupport serviceSupport;

    public BatchParticipantsServiceImpl(BatchParticipantsDao batchParticipantsDao, BatchDao batchesRepository, ServiceSupport serviceSupport) {
        this.batchParticipantsDao = batchParticipantsDao;
        this.batchesRepository = batchesRepository;
        this.serviceSupport = serviceSupport;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getParticipantsByBatchId(long batchId) {
        List<BatchParticipants> batchParticipants = batchParticipantsDao.findByBatches_BatchId(batchId);
        return batchParticipants.stream()
                .map(BatchParticipants::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BatchParticipants> getBatchParticipantById(long userId) {
        return batchParticipantsDao.findById(userId);
    }


    @Override
    @Transactional
    public void deleteParticipantFromBatch(long batchId, long userId) {
        batchParticipantsDao.deleteByBatches_BatchIdAndUserId(batchId, userId);
    }

    @Override
    @Transactional
    public void addBatchParticipants(List<Integer> userIds, int batchId) {
        Batches batch = batchesRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found: " + batchId));

        // Fetch existing userIds associated with the batch
        List<Integer> existingUserIds = batchParticipantsDao.findByBatches_BatchId(batchId)
                .stream()
                .map(BatchParticipants::getUserId)
                .toList();

        List<Integer> conflictingUserIds = new ArrayList<>();

        for (Integer userId : userIds) {
            if (!existingUserIds.contains(userId)) {
                BatchParticipants batchParticipants = new BatchParticipants();
                batchParticipants.setUserId(userId);
                batchParticipants.setBatches(batch);

                batchParticipantsDao.save(batchParticipants);
            } else {
                conflictingUserIds.add(userId);
            }
        }

        if (!conflictingUserIds.isEmpty()) {
            throw new IllegalArgumentException("Participants already exist in the batch for userIds: " + conflictingUserIds);
        }
    }

//    @Override
//    @Transactional
//    public void addBatchParticipants(List<Integer> userIds, int batchId) {
//        Batches batch = batchesRepository.findById(batchId)
//                .orElseThrow(() -> new IllegalArgumentException("Batch not found with ID: " + batchId));
//
//        List<Integer> nonExistentUserIds = new ArrayList<>();
//        List<Integer> conflictingUserIds = new ArrayList<>();
//
//        for (int userId : userIds) {
//            try {
//                // Check if user exists by fetching user data
//                Map<String, Object> userData = serviceSupport.fetchUserData(userId);
//                if (userData == null || userData.isEmpty()) {
//                    nonExistentUserIds.add(userId);                    continue; // Skip adding this user
//                }
//
//                boolean participantExists = batchParticipantsDao.existsByBatchesBatchIdAndUserId(batchId, userId);
//                if (participantExists) {
//                    conflictingUserIds.add(userId);
//                    continue; // Skip adding this user
//                } else {
//                    BatchParticipants batchParticipants = new BatchParticipants();
//                    batchParticipants.setUserId(userId);
//                    batchParticipants.setBatches(batch);
//                    batchParticipantsDao.save(batchParticipants);
//                }
//            } catch (Exception e) {
//                nonExistentUserIds.add(userId);
//            }
//        }
//        if (!nonExistentUserIds.isEmpty() || !conflictingUserIds.isEmpty()) {
//            String errorMessage = "";
//            if (!nonExistentUserIds.isEmpty()) {
//                errorMessage += "Participants not found: " + nonExistentUserIds;
//            }
//            if (!conflictingUserIds.isEmpty()) {
//                if (!errorMessage.isEmpty()) errorMessage += "; ";
//                errorMessage += "Participants already exist in the batch: " + conflictingUserIds;
//            }
//            throw new CustomException(errorMessage);
//        }
//    }

    @Override
    @Transactional
    public void deleteParticipantsByBatchId(int batchId) {
        List<BatchParticipants> participants = batchParticipantsDao.findByBatches_BatchId(batchId);
        for (BatchParticipants participant : participants) {
            batchParticipantsDao.delete(participant);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public int countParticipantsByBatchId(long batchId) {
        return batchParticipantsDao.countByBatches_BatchId(batchId);
    }

    public boolean isParticipantInBatch(int userId, int batchId) {
        return batchParticipantsDao.existsByBatchesBatchIdAndUserId(batchId, userId);
    }


}