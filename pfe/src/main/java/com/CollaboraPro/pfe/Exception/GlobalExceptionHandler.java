package com.CollaboraPro.pfe.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // Log the full stack trace
        System.err.println("=== GLOBAL EXCEPTION HANDLER ===");
        System.err.println("Request: " + request.getDescription(false));
        System.err.println("Exception: " + ex.getClass().getName());
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("=== END GLOBAL EXCEPTION ===");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erreur serveur: " + ex.getMessage());
        response.put("type", ex.getClass().getSimpleName());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        System.err.println("=== RUNTIME EXCEPTION ===");
        System.err.println("Request: " + request.getDescription(false));
        System.err.println("Exception: " + ex.getClass().getName());
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("=== END RUNTIME EXCEPTION ===");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Erreur: " + ex.getMessage());
        response.put("type", ex.getClass().getSimpleName());
        response.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
