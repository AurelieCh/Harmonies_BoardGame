package com.common_game.rules;

import com.common_game.cards.AnimalCard;
import com.common_game.decks.Position;
import com.common_game.models.Board;
import com.common_game.models.Cell;
import com.enums.ErrorCode;
import com.exceptions.GameException;

import java.util.Objects;

public class AnimalHabitatValidator {

    /**
     * Vérifie si l'animal peut être posé à la position donnée selon ses requirements
     */
    public static void assertCanPlaceAnimal(Board board, Position position, AnimalCard card) {
        for (TerrainRequirement req : card.getTerrainRequirements()) {
            int count;
            if (req.adjacent()) {
                // Vérifie les tuiles adjacentes à la cellule cible
                count = (int) position.adjacent().stream()
                        .map(board.getCells()::get)
                        .filter(Objects::nonNull)
                        .filter(c -> !c.isEmpty() && c.getTopTile().getType() == req.terrainType())
                        .count();
            } else {
                // Vérifie la tuile cible elle-même
                Cell targetCell = board.getCells().get(position);
                count = (!targetCell.isEmpty() && targetCell.getTopTile().getType() == req.terrainType()) ? 1 : 0;
            }
            if (count < req.count()) {
                throw new GameException(ErrorCode.INVALID_MOVE,
                        "Cannot place " + card.getType() + ": habitat requirement not satisfied");
            }
        }
    }
}
