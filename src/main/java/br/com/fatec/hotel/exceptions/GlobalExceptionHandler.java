package br.com.fatec.hotel.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException e) {
        Map<String, Object> errorParams = new HashMap<>();
        errorParams.put("timestamp", Instant.now());
        errorParams.put("status", HttpStatus.NOT_FOUND.value());
        errorParams.put("error", "Resource not found");
        errorParams.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorParams);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, Object> errorParams = new HashMap<>();
        errorParams.put("timestamp", Instant.now());
        errorParams.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        errorParams.put("error", "Validation exception");
        
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        errorParams.put("errors", errors);
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorParams);
    }
}
