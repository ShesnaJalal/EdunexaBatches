package com.example.v1project.utility;

public class UserIdNotFoundException extends RuntimeException {
    public UserIdNotFoundException(String message) {
        super(message);
    }

    public UserIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
