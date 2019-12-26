package com.practice.sakthi_via.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNameFoundException extends Exception {
    public UserNameFoundException(String message) {
        super(message);
    }
}
