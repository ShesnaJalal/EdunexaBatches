package com.example.v1project.dao;

import com.example.v1project.dto.Batches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchDao extends JpaRepository<Batches, Long> {
    Batches findByBatchName(String batchName);
}
