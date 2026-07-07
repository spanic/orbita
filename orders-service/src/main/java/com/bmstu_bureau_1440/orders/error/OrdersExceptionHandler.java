package com.bmstu_bureau_1440.orders.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.bmstu_bureau_1440.shared.error.BaseExceptionHandler;
import com.bmstu_bureau_1440.shared.error.ErrorCode;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.exc.InvalidTypeIdException;

@Slf4j
@RestControllerAdvice
public class OrdersExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return toResponse(OrdersErrorCodeRegistry.UNKNOWN_PRODUCT_TYPE);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> handleOrderNotFound(OrderNotFoundException ex) {
        return toResponse(OrdersErrorCodeRegistry.ORDER_NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return toResponse(OrdersErrorCodeRegistry.INVALID_PAYLOAD);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorCode code = (ex.getMostSpecificCause() instanceof InvalidTypeIdException
                ? OrdersErrorCodeRegistry.UNKNOWN_PRODUCT_TYPE
                : OrdersErrorCodeRegistry.INVALID_REQUEST);
        return toResponse(code);
    }

}
