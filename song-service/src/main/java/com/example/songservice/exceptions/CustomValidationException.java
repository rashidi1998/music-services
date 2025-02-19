package com.example.songservice.exceptions;

import java.util.Map;

public class CustomValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public CustomValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
