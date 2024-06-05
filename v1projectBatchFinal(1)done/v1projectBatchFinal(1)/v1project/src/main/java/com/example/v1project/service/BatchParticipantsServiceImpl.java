package com.example.v1project.service;

import com.example.v1project.dao.BatchDao;
import com.example.v1project.dao.BatchParticipantsDao;
import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Batches;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatchParticipantsServiceImpl implements BatchParticipantsService {

    @Autowired
    private BatchParticipantsDao batchParticipantsDao;

    //@Autowired
    //private UsersDao usersRepository;

    @Autowired
    private BatchDao batchesRepository;
    private final ServiceSupport serviceSupport;

    public BatchParticipantsServiceImpl(ServiceSupport serviceSupport) {
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
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with ID: " + batchId));

        for (int userId : userIds) {
            Map<String, Object> userData = serviceSupport.fetchUserData(userId);
//            Users user = usersRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            boolean participantExists = batchParticipantsDao.existsByBatchesBatchIdAndUserId(batchId, userId);
            if (participantExists) {
                continue; // Skip if the participant already exists
            }

            BatchParticipants batchParticipants = new BatchParticipants();
            batchParticipants.setUserId(userId);
            batchParticipants.setBatches(batch);
            batchParticipantsDao.save(batchParticipants);
        }
    }


//    @Override
//    @Transactional
//    public void addBatchParticipants(List<Integer> userIds, int batchId) {
//        Batches batch = batchesRepository.findById(batchId)
//                .orElseThrow(() -> new IllegalArgumentException("Required fields missing: batchId"));
//        for (int userId : userIds) {
//            Users user = usersRepository.findById(userId)
//                    .orElseThrow(() -> new IllegalArgumentException("Required fields missing: userId"));
//            // Check if the participant already exists
//            int participantCount = batchParticipantsDao.countByBatchesBatchIdAndUsersUserId(batchId, userId);
//            if (participantCount > 0) {
//                continue; // Skip if the participant already exists
//            }
//            // Create BatchParticipants entity and set Users and Batches
//            BatchParticipants batchParticipants = new BatchParticipants();
//            batchParticipants.setUsers(user);
//            batchParticipants.setBatches(batch);
//            // Save BatchParticipants entity
//            batchParticipantsDao.save(batchParticipants);
//        }
//    }
//

    @Override
    @Transactional
    public void addBatchParticipant(int userId, int batchId) {
        int participantCount = batchParticipantsDao.countByBatchesBatchIdAndUserId(batchId, userId);

//        Users user = usersRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("Required fields missing: userId"));
        Map<String, Object> userData = serviceSupport.fetchUserData(userId);

        Batches batch = batchesRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Required fields missing: batchId"));

        // Create BatchParticipants entity and set Users and Batches
        BatchParticipants batchParticipants = new BatchParticipants();
        batchParticipants.setUserId(userId);
        batchParticipants.setBatches(batch);

        // Save BatchParticipants entity
        batchParticipantsDao.save(batchParticipants);
    }

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