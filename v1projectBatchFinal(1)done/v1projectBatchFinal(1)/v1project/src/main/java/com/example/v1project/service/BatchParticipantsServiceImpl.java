package com.example.v1project.service;

import com.example.v1project.dao.BatchDao;
import com.example.v1project.dao.BatchParticipantsDao;
import com.example.v1project.dao.UsersDao;
import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Batches;
import com.example.v1project.dto.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BatchParticipantsServiceImpl implements BatchParticipantsService {

    @Autowired
    private BatchParticipantsDao batchParticipantsDao;

    @Autowired
    private UsersDao usersRepository;

    @Autowired
    private BatchDao batchesRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Users> getParticipantsByBatchId(long batchId) {
        List<BatchParticipants> batchParticipants = batchParticipantsDao.findByBatches_BatchId(batchId);
        return batchParticipants.stream()
                .map(BatchParticipants::getUsers)
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
        batchParticipantsDao.deleteByBatches_BatchIdAndUsers_UserId(batchId, userId);
    }

    @Override
    @Transactional
    public void addBatchParticipant(int userId, int batchId) {
        int participantCount = batchParticipantsDao.countByBatchesBatchIdAndUsersUserId(batchId, userId);

//        if (participantCount > 0) {
//            throw new ParticipantAlreadyExistsException("Participant with ID: " + userId + " already exists in batch with ID: " + batchId);
//        }

        // Fetch Users and Batches entities from database
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Batches batch = batchesRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException("Batch not found with ID: " + batchId));

        // Create BatchParticipants entity and set Users and Batches
        BatchParticipants batchParticipants = new BatchParticipants();
        batchParticipants.setUsers(user);
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
        return batchParticipantsDao.existsByBatchesBatchIdAndUsersUserId(batchId, userId);
    }


}