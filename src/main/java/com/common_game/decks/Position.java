package com.common_game.decks;

import java.util.List;

public record Position(int x, int y) {

    public List<Position> adjacent() {
        return List.of(
                new Position(x + 1, y),
                new Position(x - 1, y),
                new Position(x, y + 1),
                new Position(x, y - 1)
        );
    }
}