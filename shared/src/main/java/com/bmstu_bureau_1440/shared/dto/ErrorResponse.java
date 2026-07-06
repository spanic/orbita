package com.bmstu_bureau_1440.shared.dto;

import java.time.Instant;

import com.bmstu_bureau_1440.shared.error.ErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(
        @JsonProperty("timestamp") Instant timestamp,
        @JsonProperty("error_code") String errorCode,
        @JsonProperty("message") String message) {

    public ErrorResponse(ErrorCode code) {
        this(Instant.now(), code.name(), code.getMessage());
    }

}
