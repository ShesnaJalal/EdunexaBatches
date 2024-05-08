package com.example.v1project.dao;
import com.example.v1project.dto.Batches;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.v1project.dto.Users;



public interface UsersDao  extends JpaRepository<Users, Integer>{
    Users findByUserId(int userId);
}
