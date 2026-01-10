package com.exceptions;

import com.api.dtos.ApiError;
import com.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiError> handleAppException(AppException ex) {
        log.warn("Handled exception: {}", ex.getErrorCode(), ex);
        return ResponseEntity
                .status(resolveHttpStatus(ex.getErrorCode()))
                .body(new ApiError(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(ErrorCode.INTERNAL_ERROR, "An unexpected error occurred"));
    }

    private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case GAME_NOT_FOUND, INVALID_POSITION -> HttpStatus.NOT_FOUND;
            case NOT_YOUR_TURN, UNAUTHORIZED -> HttpStatus.FORBIDDEN;
            case INVALID_MOVE, CELL_MAX_HEIGHT_REACHED -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
