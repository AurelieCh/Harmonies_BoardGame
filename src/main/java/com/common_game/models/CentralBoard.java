package com.common_game.models;

import com.common_game.decks.TerrainDeck;
import com.enums.TerrainType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CentralBoard {
    private final Map<Integer, List<TerrainTile>> slots; // 5 emplacements (1 à 5)

    public CentralBoard(TerrainDeck deck) {
        this.slots = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            this.slots.put(i, IntStream.range(0, 3).mapToObj(j -> deck.draw()).collect(Collectors.toList()));
        }
    }

    /**
     * Return tiles on a slot number position of the central board
     * @param slotNumber The slot number where we want to get the Tiles
     * @return The tiles present on the slot number
     */
    public List<TerrainType> getSlotTiles(int slotNumber) {
        return slots.getOrDefault(slotNumber, Collections.emptyList())
                .stream()
                .map(TerrainTile::getType)
                .collect(Collectors.toList());
    }

    /**
     * Draw the tiles present on the slot number position of the central board
     * @param slotNumber The slot number position of the central board where we want to get the tiles
     * @return The tiles present on the slot number
     */
    public List<TerrainTile> takeTilesFromSlot(int slotNumber) {
        List<TerrainTile> tiles = new ArrayList<>(slots.getOrDefault(slotNumber, Collections.emptyList()));
        slots.get(slotNumber).clear();
        return tiles;
    }

    /**
     * Method to refill a slot of the central board at the position slotNumber
     * @param slotNumber Position of the slot where we want to refill
     * @param deck The terrain deck to draw only available cards
     */
    public void refillSlot(int slotNumber, TerrainDeck deck) {
        slots.put(slotNumber, IntStream.range(0, 3).mapToObj(j -> deck.draw()).collect(Collectors.toList()));
    }

    public boolean isEmpty() {
        return slots.values().stream().allMatch(List::isEmpty);
    }
}