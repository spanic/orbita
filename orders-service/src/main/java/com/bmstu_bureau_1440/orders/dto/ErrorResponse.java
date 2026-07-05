package com.bmstu_bureau_1440.orders.dto;

import java.time.Instant;

import org.springframework.http.HttpStatus;

import com.bmstu_bureau_1440.orders.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

    @JsonIgnore
    final HttpStatus status;

    @JsonProperty("timestamp")
    final Instant timestamp;

    @JsonProperty("error_code")
    final String errorCode;

    @JsonProperty("message")
    final String message;

    public ErrorResponse(ErrorCode code) {
        this.status = code.getStatus();
        this.timestamp = Instant.now();
        this.errorCode = code.getCode();
        this.message = code.getMessage();
    }

}
