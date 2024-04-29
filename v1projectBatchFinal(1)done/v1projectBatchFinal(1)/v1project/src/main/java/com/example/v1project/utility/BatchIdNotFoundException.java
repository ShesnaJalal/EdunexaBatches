package com.example.v1project.utility;

public class BatchIdNotFoundException extends RuntimeException {
    public BatchIdNotFoundException(String message) {
        super(message);
    }

    public BatchIdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
