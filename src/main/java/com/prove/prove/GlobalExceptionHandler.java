package com.prove.prove;

import com.prove.prove.inventory.InsufficientStockException;
import com.prove.prove.inventory.ProductNotFoundException;
import com.prove.prove.payment.PaymentFailedException;
import com.prove.prove.payment.PaymentNotFoundException;
import com.tagitech.provelib.exceptions.OrderNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import com.tagitech.provelib.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        logger.warn("Validation failed for request {}: {}", request.getRequestURI(), errors);
        return buildResponse(HttpStatus.BAD_REQUEST, "Validation Failed", "One or more fields have invalid values", errors, request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> errors.put(String.valueOf(cv.getPropertyPath()), cv.getMessage()));
        logger.warn("Constraint violation for request {}: {}", request.getRequestURI(), errors);
        return buildResponse(HttpStatus.BAD_REQUEST, "Constraint Violation", "Invalid request parameters", errors, request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex, HttpServletRequest request) {
        logger.warn("Order not found for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Order Not Found", ex.getMessage(), Map.of(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex, HttpServletRequest request) {
        logger.warn("Payment not found for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Payment Not Found", ex.getMessage(), Map.of(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlePaymentFailedException(PaymentFailedException ex, HttpServletRequest request) {
        logger.warn("Payment failed for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Payment Failed", ex.getMessage(), Map.of(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInsufficientStockException(InsufficientStockException ex, HttpServletRequest request) {
        logger.warn("Insufficient stock for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Insufficient Stock", ex.getMessage(), Map.of(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex, HttpServletRequest request) {
        logger.warn("Product not found for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Product Not Found", ex.getMessage(), Map.of(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest request) {
        logger.error("Data integrity violation for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Data Integrity Violation", ex.getMessage(), Map.of(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        logger.warn("Malformed request for {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", "Malformed JSON or invalid request body", Map.of(), request.getRequestURI());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAll(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error for request {}: {}", request.getRequestURI(), ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred", Map.of(), request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String errorName, String exceptionMessage, Map<String, String> errors, String path) {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), status.value(), errorName, exceptionMessage, errors, path);
        return new ResponseEntity<>(response, status);
    }
}
