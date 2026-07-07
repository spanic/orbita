package com.bmstu_bureau_1440.shared.error;

import org.springframework.http.HttpStatus;

public record ErrorCode(HttpStatus status, String message, String name) {
}
