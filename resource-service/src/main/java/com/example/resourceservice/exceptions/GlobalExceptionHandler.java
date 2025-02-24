package com.example.resourceservice.exceptions;

import com.example.resourceservice.entity.response.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("errorMessage", "Validation error");
        response.put("details", errors);
        response.put("errorCode", "400");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("errorMessage", "An error occurred: " + ex.getMessage());
        response.put("errorCode", HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("errorMessage", "Invalid resource ID: '" + ex.getValue() + "'. ID must be a number.");
        response.put("errorCode", "400");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("errorMessage", ex.getMessage());
        response.put("errorCode", "404");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Object> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex, WebRequest request) {
        Map<String, String> bodyOfResponse = new HashMap<>();
        bodyOfResponse.put("error", "400 - Media type not supported: " + ex.getContentType());
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = Collections.singletonMap("error", ex.getReason());

        return ResponseEntity.status(ex.getStatusCode())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        Map<String, String> bodyOfResponse = new HashMap<>();
        bodyOfResponse.put("error", " validation error: " + ex.getMessage());
        return new ResponseEntity<>(bodyOfResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<Object> handleCustomValidationException(CustomValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorMessage", ex.getMessage());
        response.put("details", ex.getErrors());
        response.put("errorCode", 400);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CustomConflictException.class)
    public ResponseEntity<Object> handleCustomConflictException(CustomConflictException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("errorMessage", ex.getMessage());
        response.put("errorCode", 409);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Object> handleEmptyFileException(EmptyFileException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse("File is empty", ex.getMessage()));
    }

    @ExceptionHandler(MetadataExtractException.class)
    public ResponseEntity<Object> handleMetadataExtractionException(MetadataExtractException ex) {
        return ResponseEntity.internalServerError().body(new ErrorResponse("Metadata extraction failed", ex.getLocalizedMessage()));
    }

}
