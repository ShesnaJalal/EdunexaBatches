package com.example.v1project.dao;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.v1project.dto.Users;



public interface UsersDao  extends JpaRepository<Users, Long>{
}
