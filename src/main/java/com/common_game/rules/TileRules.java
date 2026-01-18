package com.common_game.rules;

import com.enums.TerrainType;

import java.util.List;
import java.util.Map;

/**
 * Règles d’empilement des tuiles
 */
public class TileRules {

    /**
     * Map terrain -> empilements autorisés
     * Chaque liste correspond à un stack autorisé (bas -> haut)
     */
    public static final Map<TerrainType, List<List<TerrainType>>> ALLOWED_STACKS = Map.of(
            TerrainType.MOUNTAIN, List.of(
                    List.of(TerrainType.MOUNTAIN),
                    List.of(TerrainType.MOUNTAIN, TerrainType.MOUNTAIN),
                    List.of(TerrainType.MOUNTAIN, TerrainType.BUILDING),
                    List.of(TerrainType.MOUNTAIN, TerrainType.MOUNTAIN, TerrainType.MOUNTAIN)
            ),
            TerrainType.BUILDING, List.of(
                    List.of(TerrainType.BUILDING),
                    List.of(TerrainType.BUILDING, TerrainType.BUILDING)
            ),
            TerrainType.BROWN, List.of(
                    List.of(TerrainType.BROWN),
                    List.of(TerrainType.BROWN, TerrainType.TREE),
                    List.of(TerrainType.BROWN, TerrainType.BROWN),
                    List.of(TerrainType.BROWN, TerrainType.BROWN, TerrainType.TREE)
            ),
            TerrainType.TREE, List.of(
                    List.of(TerrainType.TREE)
            ),
            TerrainType.WATER, List.of(
                    List.of(TerrainType.WATER)
            ),
            TerrainType.FIELD, List.of(
                    List.of(TerrainType.FIELD)
            )
    );

    /**
     * Vérifie si une nouvelle tuile peut être placée sur la stack actuelle
     */
    public static boolean isPlacementAllowed(List<TerrainType> currentStack, TerrainType newTile) {
        List<TerrainType> simulatedStack = new java.util.ArrayList<>(currentStack);
        simulatedStack.add(newTile);

        // Vérifie si le stack simulé correspond à un stack autorisé
        return ALLOWED_STACKS.values().stream()
                .flatMap(List::stream)
                .noneMatch(allowedStack -> allowedStack.equals(simulatedStack));
    }
}