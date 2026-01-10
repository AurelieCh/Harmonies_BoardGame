package com.exceptions;

import com.enums.ErrorCode;

public class InvalidMoveException extends GameException {
    public InvalidMoveException(ErrorCode code, String message) {
        super(code, message);
    }
}
