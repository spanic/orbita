package com.bmstu_bureau_1440.payments.error;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import com.bmstu_bureau_1440.shared.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentsErrorCode implements ErrorCode {

    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "Account already exists for this user");

    private final HttpStatus status;
    private final String message;

    private static final Map<String, PaymentsErrorCode> BY_NAME =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(Enum::name, Function.identity()));

    public static Optional<PaymentsErrorCode> byCode(String code) {
        return Optional.ofNullable(BY_NAME.get(code));
    }

}
