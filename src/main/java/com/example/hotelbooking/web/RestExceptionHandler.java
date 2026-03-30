package com.example.hotelbooking.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    public record ErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        var statusCode = ex.getStatusCode();
        int status = statusCode.value();
        HttpStatus httpStatus = (statusCode instanceof HttpStatus hs) ? hs : HttpStatus.valueOf(status);
        String message = ex.getReason() != null ? ex.getReason() : httpStatus.getReasonPhrase();

        // For most cases ex.getBody() is null; path is set by Spring in the request mapping context.
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        Instant.now(),
                        status,
                        httpStatus.getReasonPhrase(),
                        message,
                        null
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(
                        Instant.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        message,
                        null
                ));
    }
}

