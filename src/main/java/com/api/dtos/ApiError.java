package com.api.dtos;

import com.enums.ErrorCode;
import lombok.Getter;

import java.time.Instant;

@Getter
public class ApiError {

    private final String code;
    private final String message;
    private final Instant timestamp = Instant.now();

    public ApiError(ErrorCode code, String message) {
        this.code = code.name();
        this.message = message;
    }
}
