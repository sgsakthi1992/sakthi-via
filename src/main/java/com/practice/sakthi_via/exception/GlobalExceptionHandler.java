package com.practice.sakthi_via.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<?> resourceNotFoundException(Exception e, WebRequest webRequest) {
        logger.error("ResourceNotFoundException" + e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public final ResponseEntity<?> constraintViolationException(ConstraintViolationException e, WebRequest webRequest) {
        logger.error("ConstraintViolationException" + e.getMessage());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest webRequest) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        logger.error("MethodArgumentNotValidException " + fieldErrors.toString());
        ErrorDetails errorDetails = new ErrorDetails(new Date(), fieldErrors.toString(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> globalExceptionHandler(Exception e, WebRequest webRequest) {
        logger.error("GlobalExceptionHandler" + e.getMessage());
        e.printStackTrace();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}
