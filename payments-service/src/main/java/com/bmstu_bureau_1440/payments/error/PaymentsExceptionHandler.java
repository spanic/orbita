package com.bmstu_bureau_1440.payments.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bmstu_bureau_1440.shared.dto.ErrorResponse;
import com.bmstu_bureau_1440.shared.error.CommonErrorCode;
import com.bmstu_bureau_1440.shared.error.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class PaymentsExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<Object> handleAccountAlreadyExists(AccountAlreadyExistsException ex) {
        return toResponse(PaymentsErrorCode.ACCOUNT_ALREADY_EXISTS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpected(Exception ex) {
        log.error("Unexpected error in payments controller", ex);
        return toResponse(CommonErrorCode.INTERNAL_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return toResponse(CommonErrorCode.VALIDATION_FAILED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return toResponse(CommonErrorCode.INVALID_REQUEST);
    }

    private static ResponseEntity<Object> toResponse(ErrorCode code) {
        return ResponseEntity.status(code.getStatus()).body(new ErrorResponse(code));
    }

}
