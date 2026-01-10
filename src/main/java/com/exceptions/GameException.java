package com.exceptions;

import com.enums.ErrorCode;

public class GameException extends AppException {
    public GameException(ErrorCode code, String message) {
        super(code, message);
    }
}
