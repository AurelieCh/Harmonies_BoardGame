package com.common_game.rules;

import com.common_game.decks.Position;
import com.common_game.models.Board;
import com.enums.TerrainType;

import java.util.Objects;

public class FishRule implements HabitatRule {
    @Override
    public boolean isSatisfied(Board board, Position position) {
        return position.adjacent().stream()
                .map(board::getTopTile)
                .filter(Objects::nonNull)
                .anyMatch(tile -> tile.getType() == TerrainType.WATER);
    }
}
