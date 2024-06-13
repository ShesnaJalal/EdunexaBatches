package com.example.v1project.controller;
import com.example.v1project.dao.BatchParticipantsDao;
import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Batches;
import com.example.v1project.service.BatchParticipantsService;
import com.example.v1project.service.BatchServiceImpl;
import com.example.v1project.service.ServiceSupport;
import com.example.v1project.utility.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/batches/participants")
public class BatchParticipantsController {

    @Autowired
    private BatchParticipantsService batchParticipantsService;

    @Autowired
    private BatchServiceImpl batchService;

    @Autowired
    private BatchParticipantsDao batchParticipantsDao;

    @Autowired
    private ServiceSupport serviceSupport;


    @GetMapping
    public ResponseEntity<?> getParticipantsByBatchId(@RequestParam(name = "batchId", required = false) Integer batchId) {
        if (batchId == null) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "Required parameter is missing: batchId", null);
        }

        try {
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                List<Integer> participants = batchParticipantsService.getParticipantsByBatchId(batchId);
                if (!participants.isEmpty()) {
                    return ResponseBuilder.buildResponse(200, "Success", null, participants);
                } else {
                    return ResponseBuilder.buildResponse(200, "Success", null, "Participants don't exist in this batch");
                }
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }
        } catch (Exception e){
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving participants", e.getMessage(), null);
        }
    }

    @PostMapping
    public ResponseEntity<Object> addBatchParticipants(@RequestBody(required = false) BatchParticipantsRequest request) {
        try {
            if (request == null || request.getUserIds() == null || request.getUserIds().length == 0) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Invalid JSON format or empty userIds array", null);
            }

            int batchId = request.getBatchId();
            if (batchId == 0) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchId", null);
            }

            Batches batch = batchService.getBatchById(batchId);
            if (batch == null) {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }

            List<String> errors = new ArrayList<>();
            List<Map<String, Object>> addedParticipants = new ArrayList<>();

            for (int userId : request.getUserIds()) {
                if (userId == 0) {
                    errors.add("Required field is missing: userId for one or more participants");
                    continue;
                }
                try {
                    Map<String, Object> userData = serviceSupport.fetchUserData(userId);

                    if (userData == null || userData.isEmpty()) {
                        errors.add("Participant with ID " + userId + " not found");
                        continue;
                    }

                    boolean participantExists = batchParticipantsService.isParticipantInBatch(userId, batchId);
                    if (participantExists) {
                        errors.add("Participant already exists in the batch for userId: " + userId);
                        continue;
                    }

                    batchParticipantsService.addBatchParticipants(List.of(userId), batchId);
                    addedParticipants.add(userData);
                } catch (Exception e) {
                    errors.add("Error occurred while processing userId: " + userId + " - " + e.getMessage());
                }
            }
            if (!errors.isEmpty()) {
                return ResponseBuilder.buildResponse(207, "Participants cannot be added", "Some participants could not be added", errors);
            }

            return ResponseBuilder.buildResponse(200, "Success", null, addedParticipants);
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred while adding participants", e.getMessage(), null);
        }
    }



    @DeleteMapping
    public ResponseEntity<?> deleteParticipantFromBatch(@RequestParam  (required = false) Integer batchId, @RequestParam (required = false) Integer userId) {
        if (batchId == null) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "Required parameter is missing: batchId", null);
        }
        if (userId == null) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "Required parameter is missing: userId", null);
        }
        try {
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                boolean participantExists = batchParticipantsDao.existsByBatchesBatchIdAndUserId(batchId, userId);
                if (participantExists) {
                    batchParticipantsService.deleteParticipantFromBatch(batchId, userId);
                    return ResponseBuilder.buildResponse(200, "Success", "Deleted participant with ID: " + userId + " from batch with ID: " + batchId, null);
                } else {
                    return ResponseBuilder.buildResponse(404, "Participant not found", "Participant not found", null);
                }
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while deleting participant from batch", e.getMessage(), null);
        }
    }
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteParticipantFromAllBatches(@RequestParam(required = false) Integer userId) {
        if (userId == null) {
            return ResponseBuilder.buildResponse(400, "Bad Request", "Required parameter is missing: userId", null);
        }
        try {
            List<BatchParticipants> batchParticipants = batchParticipantsDao.findByUserId(userId);
            if (!batchParticipants.isEmpty()) {
                for (BatchParticipants bp : batchParticipants) {
                    batchParticipantsService.deleteParticipantFromBatch(bp.getBatches().getBatchId(), userId);
                }
                return ResponseBuilder.buildResponse(200, "Success", "Deleted participant with ID: " + userId + " from all batches", null);
            } else {
                return ResponseBuilder.buildResponse(200, "Participant not found in any batches", "Participant not found in any batches", null);
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while deleting participant from all batches", e.getMessage(), null);
        }
    }
    @Setter
    @Getter
    public static class BatchParticipantsRequest {
        private int[] userIds;
        private int batchId;

    }
}
