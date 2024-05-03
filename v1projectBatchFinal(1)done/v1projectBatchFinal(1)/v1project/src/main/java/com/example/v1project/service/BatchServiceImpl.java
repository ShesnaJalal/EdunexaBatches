package com.example.v1project.service;

import com.example.v1project.controller.BatchController;
import com.example.v1project.dao.BatchDao;
import com.example.v1project.dto.Batches;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BatchServiceImpl {

    @Autowired
    private BatchDao batchDao;

    public List<Batches> getAllBatches() {
        return batchDao.findAll();
    }

    public Batches getBatchById(int batchId) {
        Optional<Batches> optionalBatch = batchDao.findById(batchId);
        return optionalBatch.orElse(null);
    }

    public Batches getBatchByName(String batchName) {
        return batchDao.findByBatchName(batchName);
    }

    public Batches createBatch(BatchController.BatchRequest batchRequest) {
        Batches batch = new Batches();
        batch.setBatchName(batchRequest.getBatchName());
        return batchDao.save(batch);
    }

    public void deleteBatchById(int batchId) {
        batchDao.deleteById(batchId);
    }

    public Batches updateBatch(Batches batch) {
        // Check if the batch exists in the database
        Optional<Batches> existingBatchOptional = batchDao.findById(batch.getBatchId());
        if (existingBatchOptional.isPresent()) {
            Batches existingBatch = existingBatchOptional.get();
            existingBatch.setBatchName(batch.getBatchName());
            return batchDao.save(existingBatch);
        } else {
            throw new EntityNotFoundException("Batch not found with id: " + batch.getBatchId());
        }
    }

}
