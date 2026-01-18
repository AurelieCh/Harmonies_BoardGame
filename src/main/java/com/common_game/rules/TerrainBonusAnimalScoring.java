package com.common_game.rules;

import com.common_game.cards.AnimalCard;
import com.common_game.decks.Position;
import com.common_game.models.Board;
import com.common_game.models.Cell;
import com.enums.TerrainType;

public class TerrainBonusAnimalScoring implements AnimalScoringRule {

    private final TerrainType bonusTerrain;
    private final int bonusPerTile;

    public TerrainBonusAnimalScoring(TerrainType bonusTerrain, int bonusPerTile) {
        this.bonusTerrain = bonusTerrain;
        this.bonusPerTile = bonusPerTile;
    }

    @Override
    public int calculateScore(Board board, AnimalCard card) {
        int score = 0;

        for (Position pos : board.getPositionsWithAnimal(card.getType())) {
            Cell cell = board.getCells().get(pos);
            if (cell == null) continue;

            // 1 cube = points de la carte
            score += card.getPoints();

            // Bonus terrain
            if (cell.getTopTile() != null
                    && cell.getTopTile().getType() == bonusTerrain) {
                score += bonusPerTile;
            }
        }

        return score;
    }
}
