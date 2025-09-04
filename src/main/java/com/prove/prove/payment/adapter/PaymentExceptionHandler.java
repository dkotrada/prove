package com.prove.prove.payment.adapter;

import com.tagitech.provelib.ErrorResponse;
import com.tagitech.provelib.exceptions.OrderNotFoundException;
import com.prove.prove.payment.PaymentFailedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(assignableTypes = PaymentRestApi.class)
public class PaymentExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Validation Failed", "One or more fields have invalid values", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put(String.valueOf(cv.getPropertyPath()), cv.getMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Constraint Violation", "Invalid request parameters", errors);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), Map.of());
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ErrorResponse> handlePaymentFailedException(PaymentFailedException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Payment Failed", ex.getMessage(), Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), Map.of());
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status, String error, String message, Map<String, String> errors) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), status.value(), error, message, errors, "");
        return new ResponseEntity<>(response, status);
    }
}
