package com.enums;

public enum ErrorCode {

    // GAME
    GAME_NOT_FOUND,
    GAME_NOT_STARTED,
    NOT_YOUR_TURN,
    INVALID_MOVE,

    // BOARD
    CELL_MAX_HEIGHT_REACHED,
    INVALID_POSITION,

    // CHAT
    CHAT_FORBIDDEN,

    // GENERIC
    UNAUTHORIZED,
    INTERNAL_ERROR
}
