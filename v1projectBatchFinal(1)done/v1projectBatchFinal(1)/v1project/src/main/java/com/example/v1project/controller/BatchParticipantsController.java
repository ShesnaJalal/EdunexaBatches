package com.example.v1project.controller;


import com.example.v1project.dto.BatchParticipants;
import com.example.v1project.dto.Users;
import com.example.v1project.dto.Batches;
//import com.example.v1project.utility.ResponseBuilder;
import com.example.v1project.service.BatchParticipantsService;
//import com.example.v1project.dto.BatchParticipantRequest;
import com.example.v1project.service.BatchParticipantsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/batches/participants")
public class BatchParticipantsController {

    @Autowired
    private BatchParticipantsService batchParticipantsService;

    @GetMapping("/{batchId}")
    public ResponseEntity<List<Users>> getParticipantsByBatchId(@PathVariable long batchId) {
        List<Users> participants = batchParticipantsService.getParticipantsByBatchId(batchId);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

//    @DeleteMapping("/{userId}")
//    public String deleteBatchParticipant(@PathVariable long userId){
//        Optional<BatchParticipants> deletedBatchParticipantOptional = batchParticipantsService.getBatchParticipantById(userId);
//        if (deletedBatchParticipantOptional.isPresent()) {
//            BatchParticipants deletedBatchParticipant = deletedBatchParticipantOptional.get();
//            return batchParticipantsService.deleteBatchParticipant(deletedBatchParticipant);
//        } else {
//            throw new RuntimeException("User with ID " + userId + " not found");
//        }
//    }

    @DeleteMapping("/{batchId}/{userId}")
    public ResponseEntity<String> deleteParticipantFromBatch(@PathVariable long batchId, @PathVariable long userId) {
        batchParticipantsService.deleteParticipantFromBatch(batchId, userId);
        return new ResponseEntity<>("Deleted participant with ID: " + userId + " from batch with ID: " + batchId, HttpStatus.OK);
    }



//    @PostMapping
//    public ResponseEntity<Batches> addBatchParticipants(@RequestBody  BatchParticipants batchParticipants){
//        batchParticipantsService.addBatchParticipant(batchParticipants);
//
//        return ResponseEntity.ok().build();
//
//
//    }
//post
    @PostMapping
    public ResponseEntity<Void> addBatchParticipants(@RequestBody BatchParticipantsRequest request) {
        batchParticipantsService.addBatchParticipant(request.getUserId(), request.getBatchId());
        return ResponseEntity.ok().build();
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





}
