package com.bmstu_bureau_1440.orders.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bmstu_bureau_1440.orders.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.exc.InvalidTypeIdException;

@Slf4j
@RestControllerAdvice
public class OrdersExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return toResponse(ErrorCode.INVALID_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error in orders controller", ex);
        return toResponse(ErrorCode.INTERNAL_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode code = resolveValidationErrorCode(ex);
        return ResponseEntity.status(code.getStatus()).body(new ErrorResponse(code));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode code = ex.getMostSpecificCause() instanceof InvalidTypeIdException
                ? ErrorCode.UNKNOWN_PRODUCT_TYPE
                : ErrorCode.INVALID_REQUEST;
        return ResponseEntity.status(code.getStatus()).body(new ErrorResponse(code));
    }

    private static ErrorCode resolveValidationErrorCode(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(OrdersExceptionHandler::errorCodeOf)
                .orElse(ErrorCode.VALIDATION_FAILED);
    }

    private static ErrorCode errorCodeOf(FieldError fieldError) {
        return ErrorCode.byCode(fieldError.getDefaultMessage()).orElse(ErrorCode.INVALID_PAYLOAD);
    }

    private static ResponseEntity<ErrorResponse> toResponse(ErrorCode code) {
        return ResponseEntity.status(code.getStatus()).body(new ErrorResponse(code));
    }

}
