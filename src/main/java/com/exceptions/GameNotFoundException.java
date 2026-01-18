package com.exceptions;

import com.enums.ErrorCode;

public class GameNotFoundException extends GameException {
    public GameNotFoundException(String gameId) {
        super(ErrorCode.GAME_NOT_FOUND, "Game with ID " + gameId + " not found");
    }
}