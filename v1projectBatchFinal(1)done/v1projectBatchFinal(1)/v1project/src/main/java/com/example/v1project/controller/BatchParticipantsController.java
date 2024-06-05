package com.example.v1project.controller;


import com.example.v1project.dao.BatchParticipantsDao;
import com.example.v1project.dto.Batches;
import com.example.v1project.service.BatchParticipantsService;
import com.example.v1project.service.BatchServiceImpl;
import com.example.v1project.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/batches/participants")
public class BatchParticipantsController {

    @Autowired
    private BatchParticipantsService batchParticipantsService;

    @Autowired
    private BatchServiceImpl batchService;
//
//    @Autowired
//    private UsersDao usersDao;

    @Autowired
    private BatchParticipantsDao batchParticipantsDao;


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
            if (request == null) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Invalid JSON format", null);
            }
            if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: userIds", null);
            }
            if (request.getBatchId() == 0) {
                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchId", null);
            }

            Batches batch = batchService.getBatchById(request.getBatchId());
            if (batch == null) {
                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
            }

            for (Integer userId : request.getUserIds()) {
//                Users user = usersDao.findByUserId(userId);
//                if (user == null) {
//                    return ResponseBuilder.buildResponse(404, "User not found", "User not found with ID: " + userId, null);
//                }

                boolean participantExists = batchParticipantsService.isParticipantInBatch(userId, request.getBatchId());
                if (participantExists) {
                    return ResponseBuilder.buildResponse(409, "Conflict", "Participant with ID: " + userId + " already exists in the batch", null);
                }
            }

            batchParticipantsService.addBatchParticipants(request.getUserIds(), request.getBatchId());
            return ResponseBuilder.buildResponse(200, "Success", null, null);
        } catch (Exception e) {
            return ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred while adding participants", e.getMessage(), null);
        }
    }

    public static class BatchParticipantsRequest {
        private List<Integer> userIds;
        private int batchId;

        public List<Integer> getUserIds() {
            return userIds;
        }

        public void setUserIds(List<Integer> userIds) {
            this.userIds = userIds;
        }

        public int getBatchId() {
            return batchId;
        }

        public void setBatchId(int batchId) {
            this.batchId = batchId;
        }
    }

//
//    @PostMapping
//    public ResponseEntity<Object> addBatchParticipants(@RequestBody(required = false) BatchParticipantsRequest request) {
//        try {
//            if (request == null || request.getUserIds() == null || request.getUserIds().isEmpty()) {
//                return ResponseBuilder.buildResponse(400, "Bad Request", "Invalid or empty request body", null);
//            }
//            if (request.getBatchId() == 0) {
//                return ResponseBuilder.buildResponse(400, "Bad Request", "Required field is missing: batchId", null);
//            }
//
//            Batches batch = batchService.getBatchById(request.getBatchId());
//            if (batch == null) {
//                return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found", null);
//            }
//
//            List<Integer> userIds = request.getUserIds();
//            for (Integer userId : userIds) {
//                Users user = usersDao.findByUserId(userId);
//                if (user == null) {
//                    // If any user is not found, return 404
//                    return ResponseBuilder.buildResponse(404, "User not found", "User not found with ID: " + userIds, null);
//                }
//
//                boolean participantExists = batchParticipantsService.isParticipantInBatch(userId, request.getBatchId());
//                if (participantExists) {
//                    // If any participant already exists, return 409 Conflict
//                    return ResponseBuilder.buildResponse(409, "Conflict", "Participant with ID: " + userIds + " already exists in the batch", null);
//                }
//                // Add each user to the batch
//                batchParticipantsService.addBatchParticipants(userIds, request.getBatchId());
//            }
//            return ResponseBuilder.buildResponse(200, "Success", null, null);
//        } catch (Exception e) {
//            return ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred while adding participants", e.getMessage(), null);
//        }
//    }
//
//    public static class BatchParticipantsRequest {
//        private List<Integer> userIds; // Changed to list of user IDs
//        private int batchId;
//
//        public List<Integer> getUserIds() {
//            return userIds;
//        }
//
//        public void setUserIds(List<Integer> userIds) {
//            this.userIds = userIds;
//        }
//
//        public int getBatchId() {
//            return batchId;
//        }
//
//        public void setBatchId(int batchId) {
//            this.batchId = batchId;
//        }
//    }
//





//
//    @PostMapping
//    public ResponseEntity<Object> addBatchParticipants(@RequestBody(required = false) BatchParticipantsRequest request) {
//        try {
//            if (request==null) {
//                return ResponseBuilder.buildResponse(400,"Bad Request","Invalid JSON format",null);
//            }
//            if (request.getUserId() == 0) {
//                return ResponseBuilder.buildResponse(400,"Bad Request","Required field is missing: userId",null);
//            }
//            if (request.getBatchId() == 0) {
//                return ResponseBuilder.buildResponse(400,"Bad Request","Required field is missing: batchId",null);
//            }
//
//            Batches batch = batchService.getBatchById(request.getBatchId());
//            if (batch == null){
//                return ResponseBuilder.buildResponse(404,"Batch not found","Batch not found",null);
//            }
//
//            Users user = usersDao.findByUserId(request.getUserId());
//            if (user == null){
//                return ResponseBuilder.buildResponse(404,"User not found","User not found",null);
//            }
//
//            boolean participantExists = batchParticipantsService.isParticipantInBatch(request.getUserId(), request.getBatchId());
//            if (participantExists) {
//                return ResponseBuilder.buildResponse(409, "Conflict", "Participant already exists in the batch", null);
//            }
//            batchParticipantsService.addBatchParticipant(request.getUserId(), request.getBatchId());
//            return ResponseBuilder.buildResponse(200, "Success", null, null);
//        }  catch (Exception e) {
//            return ResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred while adding participants", e.getMessage(), null);
//        }
//    }
//
//
//    public static class BatchParticipantsRequest {
//        private int userId;
//        private int batchId;
//
//        public int getUserId() {
//            return userId;
//        }
//
//        public void setUserId(int userId) {
//            this.userId = userId;
//        }
//
//        public int getBatchId() {
//            return batchId;
//        }
//
//        public void setBatchId(int batchId) {
//            this.batchId = batchId;
//        }
//    }

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
}
