package com.example.v1project.dao;

import com.example.v1project.dto.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventDao extends JpaRepository<Events, Integer> {
    // Additional methods if needed
}

