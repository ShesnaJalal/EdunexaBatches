package com.example.v1project.utility;

import com.example.v1project.utility.ResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        //return ResponseBuilder.buildResponse(400, "failed", "Data type error or invalid argument", null);
        String message = e.getMessage();

        // Check if the error message contains information about the data type
        if (message != null) {
            if (message.contains("date")) {
                return ResponseBuilder.buildResponse(400, "failed", "Invalid date format", null);
            } else if (message.contains("time")) {
                return ResponseBuilder.buildResponse(400, "failed", "Invalid time format", null);
            } else if (message.contains("id")) {
                return ResponseBuilder.buildResponse(400, "failed", "Invalid ID format", null);
            }
        }
        return ResponseBuilder.buildResponse(400, "failed", "Data type error or invalid argument", null);
    }
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        return ResponseBuilder.buildResponse(500, "failed", e.getMessage(), null);

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Method argument type mismatch: " + e.getName();
        return ResponseBuilder.buildResponse(400, "bad_request", message, null);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        String message = "Missing parameter: " + e.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Object> handleNullPointerException(NullPointerException e) {
        String message = "NullPointerException occurred: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(BatchIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleBatchIdNotFoundException(BatchIdNotFoundException e) {
        return ResponseBuilder.buildResponse(404, "Batch ID not found", e.getMessage(), null);
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleUserIdNotFoundException(UserIdNotFoundException e) {
        return ResponseBuilder.buildResponse(404, "User ID not found", e.getMessage(), null);
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleParticipantNotFoundException(ParticipantNotFoundException e) {
        return ResponseBuilder.buildResponse(404, "Participant not found", e.getMessage(), null);
    }

}

