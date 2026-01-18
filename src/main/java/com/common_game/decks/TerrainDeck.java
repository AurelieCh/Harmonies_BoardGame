package com.common_game.decks;

import com.common_game.models.TerrainTile;
import com.enums.ErrorCode;
import com.enums.TerrainType;
import com.exceptions.GameException;
import lombok.Getter;

import java.util.*;
import java.util.stream.IntStream;

@Getter
public class TerrainDeck {
    private final List<TerrainTile> tiles;
    private static final Map<TerrainType, Integer> TILE_COUNT = Map.of(
            TerrainType.WATER, 23,    // Blue
            TerrainType.MOUNTAIN, 23, // Gray
            TerrainType.BROWN, 21, // Brown
            TerrainType.TREE, 19,   // Green
            TerrainType.FIELD, 19,    // Yellow
            TerrainType.BUILDING, 15 // Red

    );

    public TerrainDeck() {
        this.tiles = new ArrayList<>();
        TILE_COUNT.forEach((type, count) ->
                IntStream.range(0, count).forEach(i ->
                        tiles.add(new TerrainTile(type))
                )
        );
        Collections.shuffle(tiles); // Mélange les jetons au début
    }

    public TerrainTile draw() {
        if (tiles.isEmpty()) {
            throw new GameException(ErrorCode.GAME_OVER, "No more tiles in the bag");
        }
        return tiles.removeFirst();
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }
}

