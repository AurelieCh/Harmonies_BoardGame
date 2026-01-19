package com.common_game.rules;

import com.common_game.decks.Position;
import com.enums.TerrainType;

import java.util.List;

/**
 * Représente un motif complexe pour placer un cube animal.
 *
 * @param relativePositions Positions relatives où les tuiles doivent être présentes
 * @param requiredStacks    Types de tuiles et hauteurs exactes attendues par position
 * @param placeCubeAt       Index dans requiredStacks où le cube animal doit être placé
 */
public record AnimalRequirement(
        List<Position> relativePositions,
        List<List<TerrainType>> requiredStacks,
        int placeCubeAt
) {}
