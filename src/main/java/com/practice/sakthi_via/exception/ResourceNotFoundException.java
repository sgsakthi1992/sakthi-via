package com.practice.sakthi_via.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception, thrown when Resource is not available in the system.
 *
 * @author Sakthi_Subramaniam
 */
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
