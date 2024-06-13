package com.example.v1project.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "batches")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Batches {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batch_ID")
    private int batchId;
    // Constructor with batchId parameter
    public Batches(int batchId) {
        this.batchId = batchId;
    }
    @Column(name = "batch_name", nullable = false, columnDefinition = "LONGTEXT")
    private String batchName;

}
