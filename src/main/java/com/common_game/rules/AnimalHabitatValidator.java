package com.common_game.rules;

import com.common_game.cards.AnimalCard;
import com.common_game.decks.Position;
import com.common_game.models.Board;
import com.common_game.models.Cell;
import com.enums.ErrorCode;
import com.enums.TerrainType;
import com.exceptions.GameException;

import java.util.List;

public class AnimalHabitatValidator {

    public static void assertCanPlaceAnimal(Board board, Position target, AnimalCard card) {

        for (AnimalRequirement req : card.getAnimalRequirements()) {

            Position cubeRelativePos = req.relativePositions().get(req.placeCubeAt());
            Position expectedCubePos = target.add(cubeRelativePos);

            if (!expectedCubePos.equals(target)) {
                throw new GameException(
                        ErrorCode.INVALID_MOVE,
                        "Animal must be placed at the correct motif position"
                );
            }

            for (int i = 0; i < req.relativePositions().size(); i++) {

                Position absolutePos = target.add(req.relativePositions().get(i));
                Cell cell = board.getCells().get(absolutePos);

                if (cell == null) {
                    throw new GameException(
                            ErrorCode.INVALID_MOVE,
                            "Motif goes out of board bounds"
                    );
                }

                List<TerrainType> actualStack = cell.getStackSignature();
                List<TerrainType> expectedStack = req.requiredStacks().get(i);

                if (actualStack.size() < expectedStack.size()) {
                    throw new GameException(ErrorCode.INVALID_MOVE, "Stack too small");
                }

                for (int h = 0; h < expectedStack.size(); h++) {
                    if (actualStack.get(h) != expectedStack.get(h)) {
                        throw new GameException(
                                ErrorCode.INVALID_MOVE,
                                "Habitat motif mismatch for " + card.getType()
                        );
                    }
                }

            }
            return;
        }

        throw new GameException(
                ErrorCode.INVALID_MOVE,
                "No valid habitat motif found for " + card.getType()
        );
    }
}
