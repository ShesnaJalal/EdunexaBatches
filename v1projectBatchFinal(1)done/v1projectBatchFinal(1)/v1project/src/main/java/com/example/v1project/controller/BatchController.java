package com.example.v1project.controller;

import com.example.v1project.dto.Batches;
import com.example.v1project.service.BatchServiceImpl;
import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.service.BatchParticipantsServiceImpl;

import com.example.v1project.utility.ResponseBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/batches")
public class BatchController {

    @Autowired
    private BatchServiceImpl batchService;
    @Autowired
    private BatchParticipantsServiceImpl batchParticipantsService;


    @Data
    public static class BatchRequest {
        private String batchName;
    }

    @GetMapping
    public ResponseEntity<?> getAllBatches() {
        try {
            List<Batches> batches = batchService.getAllBatches();
            return ResponseBuilder.buildResponse(200, "Success", null, batches);
        }catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batches", e.getMessage(), null);
        }

    }

    @GetMapping(params = "batchId")
    public ResponseEntity<?> getBatchById(@RequestParam int batchId) {
        try {
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                return ResponseBuilder.buildResponse(200, "Success", null, batch);
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found with the given ID", null);
            }
        }catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batch", e.getMessage(), null);
        }
    }

    @GetMapping(params = "batchName")
    public ResponseEntity<?> getBatchByName(@RequestParam String batchName) {
        try {
            Batches batch = batchService.getBatchByName(batchName);
            if (batch != null) {
                return ResponseBuilder.buildResponse(200, "Success", null, batch);
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found with the given name", null);
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batch", e.getMessage(), null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBatch(@RequestBody BatchRequest batchRequest) {
        try {
            Batches existingBatch = batchService.getBatchByName(batchRequest.getBatchName());
            if (existingBatch != null) {
                return ResponseBuilder.buildResponse(409, "Batch name already exists", "Batch name already exists", null);
            }
            Batches createdBatch = batchService.createBatch(batchRequest);
            return ResponseBuilder.buildResponse(201, "Batch created successfully", null, createdBatch);
        }
        catch (Exception e){
            return ResponseBuilder.buildResponse(500, "Error occurred while creating batch", e.getMessage(), null);
        }
    }
    //
//    @DeleteMapping("/{batchId}")
//    public ResponseEntity<?> deleteBatch(@PathVariable int batchId) {
//        Batches batch = batchService.getBatchById(batchId);
//        if(batch != null) {
//            try {
//                batchService.deleteBatchById(batchId);
//                return ResponseBuilder.buildResponse(200, "Deleted Successfully", null, null);
//            } catch (ResponseStatusException e) {
//                return ResponseBuilder.buildResponse(e.getStatusCode().value(), "Error occurred while deleting batch", e.getMessage(), null);
//            }
//        }
//        else {
//            return ResponseBuilder.buildResponse(404, "Batch not found", null, null);
//        }
//    }
    @DeleteMapping("/{batchId}")
    public ResponseEntity<?> deleteBatch(@PathVariable int batchId) {
        Batches batch = batchService.getBatchById(batchId);
        if (batch != null) {
            try {
                // Delete all associated batch participants
                batchParticipantsService.deleteParticipantsByBatchId(batchId);

                // Delete the batch
                batchService.deleteBatchById(batchId);

                return ResponseBuilder.buildResponse(200, "Deleted Successfully", null, null);
            } catch (ResponseStatusException e) {
                return ResponseBuilder.buildResponse(e.getStatusCode().value(), "Error occurred while deleting batch", e.getMessage(), null);
            } catch (Exception e) {
                return ResponseBuilder.buildResponse(500, "Internal Server Error", e.getMessage(), null);
            }
        } else {
            return ResponseBuilder.buildResponse(404, "Batch not found", null, null);
        }
    }

}
