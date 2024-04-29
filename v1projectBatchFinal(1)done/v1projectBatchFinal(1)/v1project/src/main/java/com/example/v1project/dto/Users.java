package com.example.v1project.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int userId;

    public Users(int userId) {
        this.userId = userId;
    }

    @Column(name = "email_id", length = 45, nullable = false, unique = true)
    private String emailId;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "phone_no", nullable = false)
    private int phoneNo;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "college_id", nullable = false)
    private int collegeId;

    @Column(name = "profile_picture", length = 45, columnDefinition = "VARCHAR(45) DEFAULT 'default.jpg'")
    private String profilePicture;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "google_id", length = 45, nullable = false, columnDefinition = "VARCHAR(45) DEFAULT 'google_id'") // Making google_id not null with a default value
    private String googleId;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "role_id", nullable = false, columnDefinition = "INT DEFAULT 0") // Making role_id not null with a default value of 0
    private int roleId;

}