package com.common_game.models;

import com.common_game.decks.TerrainDeck;
import com.enums.TerrainType;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class CentralBoard {
    private final Map<Integer, List<TerrainTile>> slots; // 5 emplacements (1 à 5)
    private final TerrainDeck deck;
    private Integer nbOfAnimalCubeLeft;

    public CentralBoard(TerrainDeck deck) {
        this.deck = deck;
        this.slots = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            slots.put(i, drawInitialTiles());
        }
        this.nbOfAnimalCubeLeft = 60;
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
     */
    public void refillSlot(int slotNumber) {
        if (deck.isEmpty()) {
            slots.put(slotNumber, Collections.emptyList()); // Ne recharge pas si le sac est vide
        } else {
            slots.put(slotNumber, drawInitialTiles());
        }
    }

    private List<TerrainTile> drawInitialTiles() {
        return IntStream.range(0, 3)
                .mapToObj(j -> deck.draw())
                .collect(Collectors.toList());
    }

    public boolean isEmpty() {
        return slots.values().stream().allMatch(List::isEmpty);
    }
}