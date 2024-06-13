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
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<?> getBatches(@RequestParam(required = false) Integer batchId, @RequestParam(required = false) String batchName) {
        try {
            if (batchId != null) {
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
            } else if (batchName != null && !batchName.isEmpty()) {
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
            } else {
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
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batches", e.getMessage(), null);
        }
    }




//    @GetMapping
//    public ResponseEntity<?> getAllBatches() {
//        try {
//            List<Batches> batches = batchService.getAllBatches();
//            List<Map<String, Object>> response = new ArrayList<>();
//            for (Batches batch : batches) {
//                Map<String, Object> batchDetails = new HashMap<>();
//                batchDetails.put("batchId", batch.getBatchId());
//                batchDetails.put("batchName", batch.getBatchName());
//                batchDetails.put("participantCount", batchParticipantsService.countParticipantsByBatchId(batch.getBatchId()));
//                response.add(batchDetails);
//            }
//            return ResponseBuilder.buildResponse(200, "Success", null, response);
//        } catch (Exception e) {
//            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batches", e.getMessage(), null);
//        }
//    }

//
//    @GetMapping(params = "batchId")
//    public ResponseEntity<?> getBatchById(@RequestParam(required = false) Integer batchId) {
//        try {
//            if (batchId == null) {
//                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is Missing: batchId", null);
//            }
//            Batches batch = batchService.getBatchById(batchId);
//            if (batch != null) {
//                Map<String, Object> batchDetails = new HashMap<>();
//                batchDetails.put("batchId", batch.getBatchId());
//                batchDetails.put("batchName", batch.getBatchName());
//                batchDetails.put("participantCount", batchParticipantsService.countParticipantsByBatchId(batchId));
//                List<Map<String, Object>> batchList = new ArrayList<>();
//                batchList.add(batchDetails);
//                return ResponseBuilder.buildResponse(200, "Success", null, batchList);
//            } else {
//                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
//            }
//        } catch (Exception e) {
//            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batch", e.getMessage(), null);
//        }
//    }
//
//
//    @GetMapping(params = "batchName")
//    public ResponseEntity<?> getBatchByName(@RequestParam(required = false) String batchName) {
//        try {
//            if (batchName == null || batchName.isEmpty()) {
//                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchName", null);
//            }
//            Batches batch = batchService.getBatchByName(batchName);
//            if (batch != null) {
//                Map<String, Object> batchDetails = new HashMap<>();
//                batchDetails.put("batchId", batch.getBatchId());
//                batchDetails.put("batchName", batch.getBatchName());
//                batchDetails.put("participantCount", batchParticipantsService.countParticipantsByBatchId(batch.getBatchId()));
//                List<Map<String, Object>> batchList = new ArrayList<>();
//                batchList.add(batchDetails);
//                return ResponseBuilder.buildResponse(200, "Success", null, batchList);
//            } else {
//                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
//            }
//        } catch (Exception e) {
//            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving batch", e.getMessage(), null);
//        }
//    }


    @PostMapping
    public ResponseEntity<?> createBatch(@RequestBody(required = false) BatchRequest batchRequest) {

        try {
            if (batchRequest == null) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Invalid JSON format", null);
            }
            if (batchRequest.getBatchName() == null || batchRequest.getBatchName().isEmpty()) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchName", null);
            }

            String trimmedBatchName = batchRequest.getBatchName().trim();
            if (!isValidBatchName(trimmedBatchName)) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field can only contain letters, numbers, underscores, or spaces", null);
            }
            if (trimmedBatchName.matches("\\d+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only numbers", null);
            }
            if (trimmedBatchName.matches("_+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only underscore", null);
            }
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

        if (batchId == null) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "Required parameters missing: batchId", null);
        }
        if (batchId == 0) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "batchId cannot be null", null);
        }

        try {
            Batches batch = batchService.getBatchById(batchId);

            if (batch != null) {
                try {
                    batchParticipantsService.deleteParticipantsByBatchId(batchId);
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

            if (batchRequest == null ) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchId, batchName", null);
            }
            if (batchRequest.getBatchId() == null) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchId", null);
            }

            int batchId = batchRequest.getBatchId();
            if (batchRequest.getBatchName() == null || batchRequest.getBatchName().isEmpty()) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchName", null);
            }

            String trimmedBatchName = batchRequest.getBatchName().trim();
            if (!isValidBatchName(trimmedBatchName)) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field can only contain letters, numbers, underscores, or spaces", null);
            }
            if (trimmedBatchName.matches("\\d+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only numbers", null);
            }
            if (trimmedBatchName.matches("_+")) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "batchName field cannot contain only underscore", null);
            }

            Batches existingBatch = batchService.getBatchById(batchId);
            if (existingBatch == null) {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }

            Batches existingBatchName = batchService.getBatchByName(trimmedBatchName);
            if (existingBatchName != null && existingBatchName.getBatchId() != batchId) {
                return ResponseBuilder.buildResponse(400, "batchName already exists", "Batch name already exists", null);
            }
            existingBatch.setBatchName(trimmedBatchName);
            Batches updatedBatch = batchService.updateBatch(existingBatch);
            return ResponseBuilder.buildResponse(200, "Batch name updated successfully", null, updatedBatch);

        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while updating batch name", e.getMessage(), null);
        }
    }

    private boolean isValidBatchName(String batchName) {
        return batchName.matches("^[a-zA-Z0-9_ ]*$");
    }

}
