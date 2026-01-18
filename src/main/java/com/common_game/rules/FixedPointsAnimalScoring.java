package com.common_game.rules;

import com.common_game.cards.AnimalCard;
import com.common_game.models.Board;

public class FixedPointsAnimalScoring implements AnimalScoringRule {

    @Override
    public int calculateScore(Board board, AnimalCard card) {
        int cubeCount = board.getAnimalCubeCount(card.getType());
        return cubeCount * card.getPoints();
    }
}
