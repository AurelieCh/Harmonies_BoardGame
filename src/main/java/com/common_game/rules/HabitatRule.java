package com.common_game.rules;

import com.common_game.decks.Position;
import com.common_game.models.Board;

public interface HabitatRule {
    boolean isSatisfied(Board board, Position position);
}
