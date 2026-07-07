package com.bmstu_bureau_1440.shared.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bmstu_bureau_1440.shared.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpected(Exception ex) {
        log.error("Unexpected error in controller", ex);
        return toResponse(ErrorCodesRegistry.INTERNAL_ERROR);
    }

    @ExceptionHandler(MissingUserIdException.class)
    public ResponseEntity<Object> handleMissingUserId(MissingUserIdException ex) {
        return toResponse(ErrorCodesRegistry.MISSING_USER_ID);
    }

    protected static ResponseEntity<Object> toResponse(ErrorCode code) {
        return ResponseEntity.status(code.status()).body(new ErrorResponse(code));
    }

}
