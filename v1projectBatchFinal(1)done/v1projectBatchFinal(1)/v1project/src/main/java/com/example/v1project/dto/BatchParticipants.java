package com.example.v1project.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "batches_participants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batches batches;

    public BatchParticipants(int userId, int batchId) {
        this.users = new Users(userId);
        this.batches = new Batches(batchId);
    }
}
