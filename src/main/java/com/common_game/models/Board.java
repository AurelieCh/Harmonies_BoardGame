package com.common_game.models;

import com.common_game.decks.Position;
import com.enums.AnimalType;
import com.enums.ErrorCode;
import com.enums.TerrainType;
import com.exceptions.GameException;

import java.util.*;
import java.util.stream.Collectors;

public class Board {
    private final Map<Position, Cell> cells = new HashMap<>();
    private final Map<Position, AnimalType> animalCubes = new HashMap<>(); // Animal cubes placed
    private static final int MAX_HEIGHT = 3;

    public void placeTile(Position position, TerrainTile tile) {
        Cell cell = cells.computeIfAbsent(position, Cell::new);
        if (cell.getStack().size() >= MAX_HEIGHT) {
            throw new GameException(ErrorCode.CELL_MAX_HEIGHT_REACHED, "Max height reached for this cell");
        }
        cell.getStack().push(tile);
    }

    public TerrainTile getTopTile(Position position) {
        Cell cell = cells.get(position);
        return cell != null && !cell.getStack().isEmpty() ? cell.getStack().peek() : null;
    }

    public void placeAnimalCube(Position position, AnimalType animalType) {
        if (animalCubes.containsKey(position)) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Animal cube already placed here");
        }
        animalCubes.put(position, animalType);
    }

    public boolean hasAnimalCube(Position position) {
        return animalCubes.containsKey(position);
    }

    // =============================================
    // Methods to calculate points by landscapes
    // =============================================

    /**
     * Method capable to calculate all the terrain trees points
     * @return The number of points for terrain trees type
     */
    public int calculateTreePoints() {
        int points = 0;
        for (Cell cell : cells.values()) {
            if (!cell.getStack().isEmpty()) {
                TerrainTile topTile = cell.getStack().peek();
                if (topTile.getType() == TerrainType.FOREST) {
                    points += cell.getStack().size(); // 1 point by lvl
                }
            }
        }
        return points;
    }

    /**
     * Method capable to calculate all the terrain mountains points
     * @return The number of points for terrain mountains type
     */
    public int calculateMountainPoints() {
        int points = 0;
        // Find all connected groups of moutains
        Set<Position> visited = new HashSet<>();
        for (Position position : cells.keySet()) {
            if (!visited.contains(position)) {
                TerrainTile topTile = getTopTile(position);
                if (topTile != null && topTile.getType() == TerrainType.MOUNTAIN) {
                    Set<Position> mountainGroup = findConnectedMountains(position, visited);
                    if (!mountainGroup.isEmpty()) {
                        // Calculate average size of the groups
                        int groupSize = mountainGroup.size();
                        int groupHeight = mountainGroup.stream()
                                .mapToInt(p -> cells.get(p).getStack().size())
                                .sum();
                        int avgHeight = groupHeight / groupSize;
                        points += avgHeight * groupSize; // Example : 1 point by lvl of mountains
                    }
                }
            }
        }
        return points;
    }

    /**
     * Helper capable to find the connected mountains
     * @param start Start position
     * @param visited Tiles that has been visited or not by Set<Position>
     */
    private Set<Position> findConnectedMountains(Position start, Set<Position> visited) {
        Set<Position> group = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            group.add(current);

            for (Position neighbor : current.adjacent()) {
                if (!visited.contains(neighbor) && cells.containsKey(neighbor)) {
                    TerrainTile topTile = getTopTile(neighbor);
                    if (topTile != null && topTile.getType() == TerrainType.MOUNTAIN) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return group;
    }

    /**
     * Method capable to calculate all the terrain fields points
     * @return The number of points for terrain fields type
     */
    public int calculateFieldPoints() {
        int points = 0;
        Set<Position> visited = new HashSet<>();
        for (Position position : cells.keySet()) {
            if (!visited.contains(position)) {
                TerrainTile topTile = getTopTile(position);
                if (topTile != null && topTile.getType() == TerrainType.FIELD) {
                    Set<Position> fieldGroup = findConnectedFields(position, visited);
                    if (fieldGroup.size() >= 2) {
                        points += 5; // 5 points / 2+ yellow tile
                    }
                }
            }
        }
        return points;
    }

    /**
     * Helper capable to find the connected fields
     * @param start Start position
     * @param visited Tiles that has been visited or not by Set<Position>
     */
    private Set<Position> findConnectedFields(Position start, Set<Position> visited) {
        Set<Position> group = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            group.add(current);

            for (Position neighbor : current.adjacent()) {
                if (!visited.contains(neighbor) && cells.containsKey(neighbor)) {
                    TerrainTile topTile = getTopTile(neighbor);
                    if (topTile != null && topTile.getType() == TerrainType.FIELD) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
        return group;
    }

    /**
     * Method capable to calculate all the terrain water (A type - river) points
     * @return The number of points for terrain water (A type - river) type
     */
    public int calculateWaterPointsFaceA() {
        // Find the longest path of blue tile
        Set<Position> visited = new HashSet<>();
        int maxLength = 0;
        for (Position position : cells.keySet()) {
            if (!visited.contains(position)) {
                TerrainTile topTile = getTopTile(position);
                if (topTile != null && topTile.getType() == TerrainType.WATER) {
                    int currentLength = findLongestRiver(position, visited);
                    if (currentLength > maxLength) {
                        maxLength = currentLength;
                    }
                }
            }
        }
        return maxLength > 6 ? 4 * (maxLength - 6) + 6 : maxLength; // Example : 6 tiles = 6pts, 7 tiles = 10pts, etc.
    }

    /**
     * Helper capable to find the longest river
     * @param start Start position
     * @param visited Tiles that has been visited or not by Set<Position>
     */
    private int findLongestRiver(Position start, Set<Position> visited) {
        int maxLength = 0;
        Stack<Position> stack = new Stack<>();
        stack.push(start);
        visited.add(start);

        while (!stack.isEmpty()) {
            Position current = stack.pop();
            int currentLength = 1;

            for (Position neighbor : current.adjacent()) {
                if (!visited.contains(neighbor) && cells.containsKey(neighbor)) {
                    TerrainTile topTile = getTopTile(neighbor);
                    if (topTile != null && topTile.getType() == TerrainType.WATER) {
                        visited.add(neighbor);
                        stack.push(neighbor);
                        currentLength++;
                    }
                }
            }
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
        }
        return maxLength;
    }

    /**
     * Method capable to calculate all the terrain water (B type - island) points
     * @return The number of points for terrain water (B type - island) type
     */
    public int calculateWaterPointsFaceB() {
        Set<Position> visited = new HashSet<>();
        int islandCount = 0;
        for (Position position : cells.keySet()) {
            if (!visited.contains(position)) {
                TerrainTile topTile = getTopTile(position);
                if (topTile != null && topTile.getType() == TerrainType.WATER) {
                    findIsland(position, visited);
                    islandCount++;
                }
            }
        }
        return islandCount * 5; // 5 points / island
    }

    /**
     * Helper capable to find island
     * @param start Start position
     * @param visited Tiles that has been visited or not by Set<Position>
     */
    private void findIsland(Position start, Set<Position> visited) {
        Queue<Position> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.poll();
            for (Position neighbor : current.adjacent()) {
                if (!visited.contains(neighbor) && cells.containsKey(neighbor)) {
                    TerrainTile topTile = getTopTile(neighbor);
                    if (topTile != null && topTile.getType() == TerrainType.WATER) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
    }

    /**
     * Method capable to calculate all the terrain building points
     * @return The number of points for building field type
     */
    public int calculateBuildingPoints() {
        int points = 0;
        for (Position position : cells.keySet()) {
            TerrainTile topTile = getTopTile(position);
            if (topTile != null && topTile.getType() == TerrainType.BUILDING) {
                Set<TerrainType> adjacentColors = position.adjacent().stream()
                        .map(this::getTopTile)
                        .filter(Objects::nonNull)
                        .map(TerrainTile::getType)
                        .collect(Collectors.toSet());
                if (adjacentColors.size() >= 3) {
                    points += 5; // 5 points if surrounded by 3 differents colors
                }
            }
        }
        return points;
    }

    /**
     * Method capable to calculate all the terrain points
     * @param isWaterFaceA Is the water face A (river) or B (island)
     * @return Total points of the board
     */
    public int calculateTotalLandscapePoints(boolean isWaterFaceA) {
        return calculateTreePoints()
                + calculateMountainPoints()
                + calculateFieldPoints()
                + (isWaterFaceA ? calculateWaterPointsFaceA() : calculateWaterPointsFaceB())
                + calculateBuildingPoints();
    }
}
