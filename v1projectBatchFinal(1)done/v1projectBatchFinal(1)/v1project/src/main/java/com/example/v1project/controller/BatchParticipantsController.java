package com.example.v1project.controller;


import com.example.v1project.dao.BatchParticipantsDao;
import com.example.v1project.dao.UsersDao;
import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Batches;
import com.example.v1project.dto.Users;
import com.example.v1project.service.BatchParticipantsService;
import com.example.v1project.service.BatchServiceImpl;
import com.example.v1project.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/batches/participants")
public class BatchParticipantsController {

    @Autowired
    private BatchParticipantsService batchParticipantsService;

    @Autowired
    private BatchServiceImpl batchService;

    @Autowired
    private UsersDao usersDao;

    @Autowired
<<<<<<< HEAD
    private BatchParticipantsDao BatchParticipantDao;
=======
    private BatchParticipantsDao batchParticipantsDao;
>>>>>>> d06ccb71bd22526355b16dc06e7512b8c9470f81

    @GetMapping(params = "batchId")
    public ResponseEntity<?> getParticipantsByBatchId(@RequestParam int batchId) {
        try {
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                List<Users> participants = batchParticipantsService.getParticipantsByBatchId(batchId);
                if (!participants.isEmpty()) {
                    return ResponseBuilder.buildResponse(200, "Success", null, participants);
                } else {
                    return ResponseBuilder.buildResponse(200, "Success", null, "Participants doesn't exist in this batch");
                }
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found with the given ID", null);
            }
        } catch (Exception e){
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving participants", e.getMessage(), null);
        }
    }

    @DeleteMapping(params = {"batchId","userId"})
    public ResponseEntity<?> deleteParticipantFromBatch(@RequestParam int batchId, @RequestParam long userId) {
        try {
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                // Check if the user exists
                Optional<BatchParticipants> userOptional = batchParticipantsDao.findById(userId);
                if (!userOptional.isPresent()) {
                    throw new UserIdNotFoundException("User not found with ID: " + userId);
                }
                batchParticipantsService.deleteParticipantFromBatch(batchId, userId);
                return ResponseBuilder.buildResponse(200, "Success", "Deleted participant with ID: " + userId + " from batch with ID: " + batchId, null);
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found with the given ID", null);
            }
        } catch (UserIdNotFoundException e) {
            return ResponseBuilder.buildResponse(404, "User ID not found", e.getMessage(), null);
        } catch (ParticipantNotFoundException e) {
            return ResponseBuilder.buildResponse(404, "Participant not found", "Participant not found with the given ID in the batch", null);
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while deleting participant from batch", e.getMessage(), null);
        }
    }


    @PostMapping
    public ResponseEntity<Object> addBatchParticipants(@RequestBody BatchParticipantsRequest request) {
        try {
            batchParticipantsService.addBatchParticipant(request.getUserId(), request.getBatchId());
            return ResponseBuilder.buildResponse(200, "Success", null, null);
        } catch (BatchIdNotFoundException e) {
            return ResponseBuilder.buildResponse(404, "Batch ID not found", e.getMessage(), null);
        } catch (UserIdNotFoundException e) {
            return ResponseBuilder.buildResponse(404, "User ID not found", e.getMessage(), null);
        } catch (ParticipantAlreadyExistsException e) {
            return ResponseBuilder.buildResponse(404, "Participant already exists", e.getMessage(), null);
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", e.getMessage(), null);
        }
    }

    public static class BatchParticipantsRequest {
        private int userId;
        private int batchId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getBatchId() {
            return batchId;
        }

        public void setBatchId(int batchId) {
            this.batchId = batchId;
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> countParticipantsByBatchId(@RequestParam int batchId) {
        try {
            Batches batch = batchService.getBatchById(batchId);
            if (batch != null) {
                int count = batchParticipantsService.countParticipantsByBatchId(batchId);
                return buildCountResponse(count);
            } else {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found with the given ID", null);
            }
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(500, "Error occurred while counting participants", e.getMessage(), null);
        }
    }

    private ResponseEntity<?> buildCountResponse(int count) {
        // Create a map to represent the count value
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("count", count);

        // Return the response with the count value inside the responseData
        return ResponseBuilder.buildResponse(200, "Success", null, countMap);
    }
}
