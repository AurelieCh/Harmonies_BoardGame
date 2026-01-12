package com.common_game.decks;

import com.common_game.models.TerrainTile;
import com.enums.TerrainType;
import java.util.*;

public class TerrainDeck {
    private final Deque<TerrainTile> tiles = new LinkedList<>();

    public TerrainDeck() {
        List<TerrainTile> tileList = new ArrayList<>();
        // 24 tiles / type (120 total)
        for (int i = 0; i < 24; i++) {
            tileList.add(new TerrainTile(TerrainType.FOREST));    // Green (Tree)
            tileList.add(new TerrainTile(TerrainType.MOUNTAIN));  // Gray (Mountain)
            tileList.add(new TerrainTile(TerrainType.WATER));     // Blue (Water)
            tileList.add(new TerrainTile(TerrainType.FIELD));     // Yellow (Field)
            tileList.add(new TerrainTile(TerrainType.BUILDING));  // Red (Building)
        }
        Collections.shuffle(tileList);
        tiles.addAll(tileList);
    }

    public TerrainTile draw() {
        if (tiles.isEmpty()) {
            throw new IllegalStateException("Terrain deck is empty");
        }
        return tiles.pop();
    }

    public boolean isEmpty() {
        return tiles.isEmpty();
    }
}
