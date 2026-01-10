package com.common_game.cards;

import com.common_game.rules.HabitatRule;
import com.enums.AnimalType;
import com.enums.TerrainType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class AnimalCard {
    private final UUID id;
    private final AnimalType type;
    private final int points;
    private final HabitatRule rule;
    private final List<TerrainType> requiredTerrains; // Required terrain arround pos
    private final int minTerrainCount; // Minimal amount of required terrain
    private final int cubeCount; // Number of animal cube field to place
    private int placedCubes; // Number of animal cube already placed

    /**
     * Checks if all cubes for this animal card have been placed on the board.
     * A card is considered complete when the number of placed cubes equals the total cube count.
     *
     * @return {@code true} if all cubes are placed, {@code false} otherwise.
     */
    public boolean isComplete() {
        return placedCubes >= cubeCount;
    }

    /**
     * Increments the count of placed cubes for this animal card.
     * This method should be called whenever a cube is successfully placed on the board
     * according to the card's habitat rules.
     */
    public void incrementPlacedCubes() {
        this.placedCubes++;
    }
}
