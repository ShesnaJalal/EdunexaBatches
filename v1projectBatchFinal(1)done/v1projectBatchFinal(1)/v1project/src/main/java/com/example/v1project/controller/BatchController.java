package com.example.v1project.controller;
import com.example.v1project.dto.Batches;
import com.example.v1project.service.BatchServiceImpl;
import com.example.v1project.service.BatchParticipantsServiceImpl;
import com.example.v1project.utility.ResponseBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/batches")
public class BatchController {

    @Autowired
    private BatchServiceImpl batchService;

    @Autowired
    private BatchParticipantsServiceImpl batchParticipantsService;

    @Data
    public static class BatchRequest {

        private String batchName;
        private Integer batchId;

    }

    @GetMapping
    public ResponseEntity<?> getAllBatches() {
        try {
            List<Batches> batches = batchService.getAllBatches();
            List<Map<String, Object>> response = new ArrayList<>();
            for (Batches batch : batches) {
                Map<String, Object> batchDetails = new HashMap<>();
                batchDetails.put("batchId", batch.getBatchId());
                batchDetails.put("batchName", batch.getBatchName());
                batchDetails.put("participantCount", batchParticipantsService.countParticipantsByBatchId(batch.getBatchId()));
                response.add(batchDetails);
            }
            return ResponseBuilder.buildResponse(200, "Success", null, response);
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batches", e.getMessage(), null);
        }
    }

    @GetMapping(params = "batchId")
    public ResponseEntity<?> getBatchById(@RequestParam(required = false) Integer batchId) {
        try {
            if (batchId == null) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is Missing: batchId", null);
            }
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                Map<String, Object> batchDetails = new HashMap<>();
                batchDetails.put("batchId", batch.getBatchId());
                batchDetails.put("batchName", batch.getBatchName());
                batchDetails.put("participantCount", batchParticipantsService.countParticipantsByBatchId(batchId));
                List<Map<String, Object>> batchList = new ArrayList<>();
                batchList.add(batchDetails);
                return ResponseBuilder.buildResponse(200, "Success", null, batchList);
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batch", e.getMessage(), null);
        }
    }
    @GetMapping(params = "batchName")
    public ResponseEntity<?> getBatchByName(@RequestParam(required = false) String batchName) {
        try {
            if (batchName == null || batchName.isEmpty()) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchName", null);
            }
            Batches batch = batchService.getBatchByName(batchName);
            if (batch != null) {
                Map<String, Object> batchDetails = new HashMap<>();
                batchDetails.put("batchId", batch.getBatchId());
                batchDetails.put("batchName", batch.getBatchName());
                batchDetails.put("participantCount", batchParticipantsService.countParticipantsByBatchId(batch.getBatchId()));
                List<Map<String, Object>> batchList = new ArrayList<>();
                batchList.add(batchDetails);
                return ResponseBuilder.buildResponse(200, "Success", null, batchList);
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batch", e.getMessage(), null);
        }
    }


    @PostMapping
    public ResponseEntity<?> createBatch(@RequestBody(required = false) BatchRequest batchRequest) {
        try {
            if (batchRequest == null) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Invalid JSON format", null);
            }

            if (batchRequest.getBatchName() == null || batchRequest.getBatchName().isEmpty()) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchName", null);
            }

            // Trim the batch name to remove leading and trailing white spaces
            String trimmedBatchName = batchRequest.getBatchName().trim();

            // Check if trimmed batch name is a valid string
            if (!isValidBatchName(trimmedBatchName)) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field can only contain letters, numbers, underscores, or spaces", null);
            }

            // Check if the batch name contains only numbers
            if (trimmedBatchName.matches("\\d+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only numbers", null);
            }

            // Check if the batch name contains only underscores
            if (trimmedBatchName.matches("_+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only underscore", null);
            }


            // Check if batch name already exists (after trimming)
            Batches existingBatch = batchService.getBatchByName(trimmedBatchName);
            if (existingBatch != null) {
                return ResponseBuilder.buildResponse(400, "batchName already exists", "Batch name already exists", null);
            }
            Batches createdBatch = batchService.createBatch(batchRequest);
            return ResponseBuilder.buildResponse(200, "Batch created successfully", null, createdBatch);
        } catch (Exception e){
            return ResponseBuilder.buildResponse(500, "Error occurred while creating batch", e.getMessage(), null);
        }

    }


    @DeleteMapping
    public ResponseEntity<?> deleteBatch(@RequestParam(required = false) Integer batchId) {
        // Check if batchId parameter is included in the endpoint
        if (batchId == null) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "Required parameters missing: batchId", null);
        }

        // Check if batchId value is provided
        if (batchId == 0) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "batchId cannot be null", null);
        }

        try {
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                try {
                    // Delete all associated batch participants
                    batchParticipantsService.deleteParticipantsByBatchId(batchId);

                    // Delete the batch
                    batchService.deleteBatchById(batchId);

                    return ResponseBuilder.buildResponse(200, "Deleted Successfully", null, null);
                } catch (Exception e) {
                    return ResponseBuilder.buildResponse(500, "Internal Server Error", e.getMessage(), null);
                }
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while processing request", e.getMessage(), null);
        }
    }
    @PutMapping
    public ResponseEntity<?> editBatchName(@RequestBody(required = false) BatchRequest batchRequest) {
        try {
            // Check if request body is null
            if (batchRequest == null ) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchId, batchName", null);
            }

            if (batchRequest.getBatchId() == null) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchId", null);
            }

            int batchId = batchRequest.getBatchId();

            // Check if batchName is null or empty.
            if (batchRequest.getBatchName() == null || batchRequest.getBatchName().isEmpty()) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchName", null);
            }

            // Trim white spaces before and after the batch name
            String trimmedBatchName = batchRequest.getBatchName().trim();

            // Check if batchName is a valid string
            if (!isValidBatchName(trimmedBatchName)) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field can only contain letters, numbers, underscores, or spaces", null);
            }

            // Check if the batch name contains only numbers
            if (trimmedBatchName.matches("\\d+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only numbers", null);
            }

            // Check if the batch name contains only underscores
            if (trimmedBatchName.matches("_+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only underscore", null);
            }

            Batches existingBatch = batchService.getBatchById(batchId);
            if (existingBatch == null) {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }

            // Check if the new batch name already exists
            // Check if batch name already exists (after trimming)
            Batches existingBatchName = batchService.getBatchByName(trimmedBatchName);
            if (existingBatchName != null && existingBatchName.getBatchId() != batchId) {
                return ResponseBuilder.buildResponse(400, "batchName already exists", "Batch name already exists", null);
            }

            // Modify the batch name
            existingBatch.setBatchName(trimmedBatchName);
            Batches updatedBatch = batchService.updateBatch(existingBatch);
            return ResponseBuilder.buildResponse(200, "Batch name updated successfully", null, updatedBatch);
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while updating batch name", e.getMessage(), null);
        }
    }

    // Method to validate batchName
    private boolean isValidBatchName(String batchName) {
        // Perform your custom validation logic here
        // For example, you can check if the batchName contains only letters, numbers, underscores, or spaces
        return batchName.matches("^[a-zA-Z0-9_ ]*$");
    }


}
