package com.common_game.rules;

import com.common_game.decks.Position;
import com.common_game.models.Board;
import com.common_game.models.Cell;
import com.enums.AnimalType;
import com.enums.TerrainType;

public final class AnimalHabitatValidator {

    private AnimalHabitatValidator() {}

    public static boolean isHabitatValid(Board board, Position position, AnimalType animalType) {
        return switch (animalType) {
            case DEER -> isDeerHabitatValid(board, position);
            case FOX -> isFoxHabitatValid(board, position);
            case BIRD -> isBirdHabitatValid(board, position);
            case FISH -> isFishHabitatValid(board, position);
            case FENNEC -> isFennecHabitatValid(board, position);
            case MOUSE -> isMouseHabitatValid(board, position);
        };
    }

    private static boolean isDeerHabitatValid(Board board, Position position) {
        Cell cell = board.getCells().get(position);
        if (cell.isEmpty() || cell.getTopTile().getType() != TerrainType.FOREST) {
            return false;
        }
        return countAdjacentTerrains(board, position, TerrainType.FOREST) >= 2;
    }

    private static boolean isFoxHabitatValid(Board board, Position position) {
        Cell cell = board.getCells().get(position);
        if (cell.isEmpty() || (!isMountainOrForest(cell))) {
            return false;
        }
        return countAdjacentTerrains(board, position, TerrainType.MOUNTAIN) >= 1 &&
                countAdjacentTerrains(board, position, TerrainType.FOREST) >= 1;
    }

    private static boolean isBirdHabitatValid(Board board, Position position) {
        Cell cell = board.getCells().get(position);
        if (cell.isEmpty() || cell.getTopTile().getType() != TerrainType.WATER) {
            return false;
        }
        return countAdjacentTerrains(board, position, TerrainType.WATER) >= 1;
    }

    private static boolean isFishHabitatValid(Board board, Position position) {
        return isBirdHabitatValid(board, position);
    }

    private static boolean isFennecHabitatValid(Board board, Position position) {
        Cell cell = board.getCells().get(position);
        if (cell.isEmpty() || cell.getTopTile().getType() != TerrainType.MOUNTAIN) {
            return false;
        }
        return countAdjacentTerrains(board, position, TerrainType.MOUNTAIN) >= 2;
    }

    private static boolean isMouseHabitatValid(Board board, Position position) {
        Cell cell = board.getCells().get(position);
        if (cell.isEmpty() || cell.getTopTile().getType() != TerrainType.FIELD) {
            return false;
        }
        return countAdjacentTerrains(board, position, TerrainType.FIELD) >= 1;
    }

    private static boolean isMountainOrForest(Cell cell) {
        TerrainType type = cell.getTopTile().getType();
        return type == TerrainType.MOUNTAIN || type == TerrainType.FOREST;
    }

    private static long countAdjacentTerrains(Board board, Position position, TerrainType type) {
        return board.getAdjacentPositions(position).stream()
                .map(board.getCells()::get)
                .filter(cell -> !cell.isEmpty())
                .filter(cell -> cell.getTopTile().getType() == type)
                .count();
    }
}

