package com.example.v1project.service;

import com.example.v1project.controller.BatchController;
import com.example.v1project.dao.BatchDao;
import com.example.v1project.dto.Batches;
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

    public Batches getBatchById(long batchId) {
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

    public void deleteBatchById(long batchId) {
        batchDao.deleteById(batchId);
    }

}
