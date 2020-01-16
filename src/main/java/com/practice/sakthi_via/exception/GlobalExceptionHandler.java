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
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Logger Object to log the details.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * To handle ResourceNotFoundException.
     *
     * @param e          Exception
     * @param webRequest WebRequest
     * @return ResponseEntity with the error details in body
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorDetails> resourceNotFoundException(
            final Exception e, final WebRequest webRequest) {
        LOGGER.error("ResourceNotFoundException : ", e);
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(), e.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    /**
     * To handle ConstraintViolationException.
     *
     * @param e          Exception
     * @param webRequest WebRequest
     * @return ResponseEntity with the error details in body
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public final ResponseEntity<ErrorDetails> constraintViolationException(
            final ConstraintViolationException e, final WebRequest webRequest) {
        LOGGER.error("ConstraintViolationException : ", e);
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(), e.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * To handle MethodArgumentNotValidException.
     *
     * @param e          Exception
     * @param webRequest WebRequest
     * @return ResponseEntity with the error details in body
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<ErrorDetails> methodArgumentNotValidException(
            final MethodArgumentNotValidException e,
            final WebRequest webRequest) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        LOGGER.error("MethodArgumentNotValidException {}", fieldErrors);
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(), fieldErrors.toString(),
                webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * To handle HttpClientErrorException.
     *
     * @param e          Exception
     * @param webRequest WebRequest
     * @return ResponseEntity with error details in body
     */
    @ExceptionHandler(BadRequest.class)
    public final ResponseEntity<ErrorDetails> httpClientErrorException(
            final BadRequest e, final WebRequest webRequest) {
        LOGGER.error("HttpClientErrorException-BadRequest: ", e);
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(), e.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * To handle all the Exceptions, except the exceptions defined separately.
     *
     * @param e          Exception
     * @param webRequest WebRequest
     * @return ResponseEntity with the error details in body
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> globalExceptionHandler(
            final Exception e, final WebRequest webRequest) {
        LOGGER.error("GlobalExceptionHandler :", e);
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(), e.getMessage(), webRequest.getDescription(false));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDetails);
    }
}
