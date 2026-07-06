package com.bmstu_bureau_1440.shared.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus getStatus();

    String getMessage();

    String name();

}
