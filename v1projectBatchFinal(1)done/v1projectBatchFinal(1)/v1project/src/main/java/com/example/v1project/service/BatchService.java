package com.example.v1project.service;

import com.example.v1project.dto.Batches;

import java.util.List;

public interface BatchService {

    List<Batches> getAllBatches();
    Batches createBatch(Batches batch);
    void deleteBatchById(int batchId);
}
