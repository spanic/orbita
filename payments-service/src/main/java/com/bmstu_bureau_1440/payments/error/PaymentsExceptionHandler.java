package com.bmstu_bureau_1440.payments.error;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.bmstu_bureau_1440.shared.error.BaseExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class PaymentsExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<Object> handleAccountAlreadyExists(AccountAlreadyExistsException ex) {
        return toResponse(PaymentsErrorCodeRegistry.ACCOUNT_ALREADY_EXISTS);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Object> handleAccountNotFound(AccountNotFoundException ex) {
        return toResponse(PaymentsErrorCodeRegistry.ACCOUNT_NOT_FOUND);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleOptimisticLockingFailure(OptimisticLockingFailureException ex) {
        return toResponse(PaymentsErrorCodeRegistry.ACCOUNT_UPDATE_CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        boolean invalidAmount = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.unwrap(ConstraintViolation.class))
                .anyMatch(violation -> violation.getConstraintDescriptor().getAnnotation()
                        .annotationType() == Positive.class);
        return toResponse(
                invalidAmount ? PaymentsErrorCodeRegistry.INVALID_AMOUNT : PaymentsErrorCodeRegistry.VALIDATION_FAILED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return toResponse(PaymentsErrorCodeRegistry.INVALID_REQUEST);
    }

}
