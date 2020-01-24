package com.practice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {
    /**
     * Parameterized constructor.
     *
     * @param message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }
}
