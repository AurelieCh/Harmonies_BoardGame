package com.common_game.decks;

import com.common_game.models.TerrainTile;
import com.enums.TerrainType;
import java.util.*;

public class TerrainDeck {
    private final Deque<TerrainTile> tiles = new LinkedList<>();

    public TerrainDeck() {
        List<TerrainTile> tileList = new ArrayList<>();
        // Ajoute les tuiles (ex: 20 de chaque type)
        for (int i = 0; i < 20; i++) {
            tileList.add(new TerrainTile(TerrainType.FOREST));
            tileList.add(new TerrainTile(TerrainType.MOUNTAIN));
            tileList.add(new TerrainTile(TerrainType.WATER));
            tileList.add(new TerrainTile(TerrainType.FIELD));
            tileList.add(new TerrainTile(TerrainType.BUILDING));
        }
        Collections.shuffle(tileList);
        tiles.addAll(tileList);
    }

    // Retourne une copie de la Deque sous forme de List
    public List<TerrainTile> getTiles() {
        return new ArrayList<>(tiles);
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
