package com.common_game.decks;

import com.common_game.models.TerrainTile;
import com.enums.TerrainType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TerrainDeck {

    private final Deque<TerrainTile> tiles = new ArrayDeque<>();

    public TerrainDeck(List<TerrainType> initialTiles) {
        shuffleAndInit(initialTiles);
    }

    /**
     * Method that shuffle the cards
     * @param types A list of all terrain types available
     */
    private void shuffleAndInit(List<TerrainType> types) {
        var list = new java.util.ArrayList<>(types);
        java.util.Collections.shuffle(list);

        for (TerrainType type : list) {
            tiles.push(new TerrainTile(type));
        }
    }

    /**
     * Method that draw a terrain tile
     * @return A terrain tile if possible
     */
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
