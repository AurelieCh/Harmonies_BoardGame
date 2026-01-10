package com.api.controllers;

import com.api.dtos.ApiError;
import com.enums.ErrorCode;
import com.exceptions.GameException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<ApiError> handleGameException(GameException ex) {
        return ResponseEntity.badRequest().body(new ApiError(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(Exception ex) {
        return ResponseEntity.internalServerError().body(new ApiError(ErrorCode.INTERNAL_ERROR, "An unexpected error occurred"));
    }
}
