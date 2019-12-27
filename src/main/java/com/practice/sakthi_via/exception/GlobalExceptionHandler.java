package com.practice.sakthi_via.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionSystemException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(Exception e){
        Throwable cause = (TransactionSystemException) e.getCause();
        if(cause instanceof ConstraintViolationException){
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) cause).getConstraintViolations();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(constraintViolations);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
