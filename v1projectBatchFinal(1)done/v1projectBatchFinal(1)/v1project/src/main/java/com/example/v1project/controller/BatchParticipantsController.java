package com.example.v1project.controller;


import com.example.v1project.dto.Users;
import com.example.v1project.service.BatchParticipantsService;
import com.example.v1project.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batches/participants")
public class BatchParticipantsController {

    @Autowired
    private BatchParticipantsService batchParticipantsService;

    @GetMapping("/{batchId}")
    public ResponseEntity<?> getParticipantsByBatchId(@PathVariable long batchId) {
        try {
            List<Users> participants = batchParticipantsService.getParticipantsByBatchId(batchId);
            return ResponseBuilder.buildResponse(200, "Success", null, participants);
        } catch (Exception e){
            return ResponseBuilder.buildResponse(500, "Error occurred while retrieving participants", e.getMessage(), null);
        }

    }

    @DeleteMapping("/{batchId}/{userId}")
    public ResponseEntity<?> deleteParticipantFromBatch(@PathVariable long batchId, @PathVariable long userId) {
        try {
            batchParticipantsService.deleteParticipantFromBatch(batchId, userId);
            return ResponseBuilder.buildResponse(200, "Success", "Deleted participant with ID: " + userId + " from batch with ID: " + batchId, null);
        } catch (BatchIdNotFoundException e) {
            return ResponseBuilder.buildResponse(404, "Batch not found", "Batch not found with the given ID", null);
        } catch (UserIdNotFoundException e) {
            return ResponseBuilder.buildResponse(404, "User not found", "User not found with the given ID", null);
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
        private long userId;
        private long batchId;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getBatchId() {
            return batchId;
        }

        public void setBatchId(long batchId) {
            this.batchId = batchId;
        }
    }

}
