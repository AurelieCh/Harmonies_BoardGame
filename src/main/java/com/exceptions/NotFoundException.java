package com.exceptions;

import com.enums.ErrorCode;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(ErrorCode.GAME_NOT_FOUND, message);
    }
}
