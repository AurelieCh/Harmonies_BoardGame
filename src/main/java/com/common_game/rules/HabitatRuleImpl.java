package com.common_game.rules;

import com.common_game.decks.Position;
import com.common_game.models.Board;
import com.enums.TerrainType;

import java.util.List;
import java.util.Objects;

public class HabitatRuleImpl implements HabitatRule {

    private final List<TerrainType> requiredAdjacentTerrains;
    private final int minAdjacent;

    /**
     * Crée une règle de placement d'habitat pour un animal.
     * @param requiredAdjacentTerrains Les types de terrains nécessaires à côté.
     * @param minAdjacent Le nombre minimum de terrains requis autour.
     */
    public HabitatRuleImpl(List<TerrainType> requiredAdjacentTerrains, int minAdjacent) {
        this.requiredAdjacentTerrains = requiredAdjacentTerrains;
        this.minAdjacent = minAdjacent;
    }

    @Override
    public boolean isSatisfied(Board board, Position position) {
        long count = position.adjacent().stream()
                .map(board::getTopTile)
                .filter(Objects::nonNull)
                .filter(tile -> requiredAdjacentTerrains.contains(tile.getType()))
                .count();
        return count >= minAdjacent;
    }
}

