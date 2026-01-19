package com.common_game.cards;

import com.common_game.models.Board;
import com.common_game.rules.AnimalRequirement;
import com.common_game.rules.AnimalScoringRule;
import com.enums.AnimalType;
import lombok.Getter;


import java.util.List;
import java.util.UUID;

@Getter
public class AnimalCard {

    private final UUID id;
    private final AnimalType type;
    private final int points;
    private final int cubeCount;
    private final AnimalScoringRule scoringRule;
    private final List<AnimalRequirement> animalRequirements;
    private int placedCubes;

    public AnimalCard(UUID id, AnimalType type, int points, int cubeCount,
                      AnimalScoringRule scoringRule, List<AnimalRequirement> animalRequirements) {
        this.id = id;
        this.type = type;
        this.points = points;
        this.cubeCount = cubeCount;
        this.scoringRule = scoringRule;
        this.animalRequirements = animalRequirements;
        this.placedCubes = 0;
    }

    public boolean isComplete() {
        return placedCubes >= cubeCount;
    }

    public void incrementPlacedCubes() {
        this.placedCubes++;
    }

    public int calculateScore(Board board) {
        return scoringRule.calculateScore(board, this);
    }
}
