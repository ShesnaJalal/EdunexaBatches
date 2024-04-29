package com.example.v1project.controller;

import com.example.v1project.dto.Batches;
import com.example.v1project.service.BatchServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches")
public class BatchController {

    @Autowired
    private BatchServiceImpl batchService;

    @Data
    public static class BatchRequest {
        private String batchName;
    }

    @GetMapping
    public ResponseEntity<List<Batches>> getAllBatches() {
        List<Batches> batches = batchService.getAllBatches();
        return new ResponseEntity<>(batches, HttpStatus.OK);
    }

    @GetMapping(params = "batchId")
    public ResponseEntity<Batches> getBatchById(@RequestParam int batchId) {
        Batches batch = batchService.getBatchById(batchId);
        if (batch != null) {
            return new ResponseEntity<>(batch, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = "batchName")
    public ResponseEntity<Batches> getBatchByName(@RequestParam String batchName) {
        Batches batch = batchService.getBatchByName(batchName);
        if (batch != null) {
            return new ResponseEntity<>(batch, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Batches> createBatch(@RequestBody BatchRequest batchRequest) {
        Batches createdBatch = batchService.createBatch(batchRequest);
        return new ResponseEntity<>(createdBatch, HttpStatus.CREATED);
    }

    @DeleteMapping("/{batchId}")
    public ResponseEntity<String> deleteBatch(@PathVariable int batchId) {
        batchService.deleteBatchById(batchId);
        return new ResponseEntity<>("Deleted batch with ID: " + batchId, HttpStatus.OK);
    }
}
