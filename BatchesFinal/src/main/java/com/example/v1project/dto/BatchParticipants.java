package com.example.v1project.dto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.lang.Integer;

@Entity
@Table(name = "batches_participants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchParticipants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @JoinColumn(name = "user_id", nullable = false)
    @Column
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "batch_id", nullable = false)
    private Batches batches;

//    public BatchParticipants(int userId, int batchId) {
//        this.users = new Users(userId);
//        this.batches = new Batches(batchId);
//    }
}
