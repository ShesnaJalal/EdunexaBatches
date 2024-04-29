package com.example.v1project.utility;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(String message) {
        super(message);
    }

    public ParticipantNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
