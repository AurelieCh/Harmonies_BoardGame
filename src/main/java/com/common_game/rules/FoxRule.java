package com.common_game.rules;

import com.common_game.decks.Position;
import com.common_game.models.Board;
import com.enums.TerrainType;

import java.util.Objects;

public class FoxRule implements HabitatRule {
    @Override
    public boolean isSatisfied(Board board, Position position) {
        // Verifies that the central position has 3 gray/brown neighbors or more
        long greyBrownNeighbors = position.adjacent().stream()
                .map(board::getTopTile)
                .filter(Objects::nonNull)
                .filter(tile -> tile.getType() == TerrainType.MOUNTAIN || tile.getType() == TerrainType.FOREST)
                .count();
        return greyBrownNeighbors >= 3;
    }
}
