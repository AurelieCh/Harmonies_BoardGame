package com.common_game.models;

import com.common_game.cards.AnimalCard;
import com.common_game.decks.Position;
import com.common_game.rules.AnimalHabitatValidator;
import com.common_game.rules.TileRules;
import com.enums.AnimalType;
import com.enums.ErrorCode;
import com.enums.TerrainType;
import com.enums.WaterFaceType;
import com.exceptions.GameException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Board {
    private Map<Position, Cell> cells;
    private final WaterFaceType waterFaceType;
    public static final int BOARD_SIZE = 5;

    public Board(WaterFaceType waterFaceType) {
        this.cells = new HashMap<>();
        this.waterFaceType = waterFaceType;

        for (int y = 0; y < BOARD_SIZE; y++) {
            int rowSize = (y % 2 == 0) ? 5 : 4;
            int xOffset = (y % 2 == 0) ? 0 : 1;

            for (int x = 0; x < rowSize; x++) {
                Position pos = new Position(x + xOffset, y);
                cells.put(pos, new Cell(pos));
            }
        }
    }

    /**
     * Place a tile at a position, optionally at a specific height (0 = level 1)
     */
    public void placeTile(Position pos, TerrainTile tile) {
        Cell cell = cells.computeIfAbsent(pos, Cell::new);

        if (cell.hasAnimalCube()) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Cannot place tile on a cell with an animal cube");
        }

        List<TerrainType> currentStack = cell.getStack().stream()
                .map(TerrainTile::getType)
                .toList();

        if (TileRules.isPlacementAllowed(currentStack, tile.getType())) {
            throw new GameException(ErrorCode.INVALID_MOVE,
                    "Tile " + tile.getType() + " cannot be placed here according to stack rules: " + currentStack);
        }

        cell.addTile(tile);
    }

    public void placeAnimalCube(Position position, AnimalType animalType, CentralBoard centralBoard, AnimalCard card) {
        Cell cell = cells.get(position);
        if (cell == null) throw new GameException(ErrorCode.INVALID_POSITION, "Invalid position");
        if (cell.isEmpty()) throw new GameException(ErrorCode.INVALID_MOVE, "Cannot place on empty cell");
        if (cell.hasAnimalCube()) throw new GameException(ErrorCode.INVALID_MOVE, "Cell already has an animal cube");

        AnimalHabitatValidator.assertCanPlaceAnimal(this, position, card);

        // placement
        centralBoard.setNbOfAnimalCubeLeft(centralBoard.getNbOfAnimalCubeLeft() - 1);
        cell.setAnimalCube(animalType);
    }

    public TerrainTile getTopTile(Position position) {
        Cell cell = cells.get(position);
        return cell != null && !cell.getStack().isEmpty() ? cell.getStack().peek() : null;
    }

    public int getEmptyCellsCount() {
        return (int) cells.values().stream()
                .filter(Cell::isEmpty)
                .count();
    }

    /**
     * Retourne le nombre total de cubes d'un type animal placé sur le plateau.
     * @param animalType le type d'animal
     * @return nombre de cubes placés
     */
    public int getAnimalCubeCount(AnimalType animalType) {
        return (int) cells.values().stream()
                .filter(cell -> cell.getAnimalCube() == animalType)
                .count();
    }

    /**
     * Retourne toutes les positions où au moins un cube du type animal est placé.
     * @param animalType le type d'animal
     * @return liste des positions
     */
    public List<Position> getPositionsWithAnimal(AnimalType animalType) {
        return cells.entrySet().stream()
                .filter(e -> e.getValue().getAnimalCube() == animalType)
                .map(Map.Entry::getKey)
                .toList();
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
            if (!cell.isEmpty() && cell.getTopTile().getType() == TerrainType.TREE) {
                int height = cell.getHeight();
                points += height == 1 ? 1 : height == 2 ? 2 : 4;  // 1/2/4 points
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
        Set<Position> visited = new HashSet<>();

        for (Cell cell : cells.values()) {
            Position position = cell.getPosition();
            if (!cell.isEmpty() &&
                    cell.getTopTile().getType() == TerrainType.MOUNTAIN &&
                    !visited.contains(position)) {

                // Trouve toutes les montagnes connectées à cette position
                Set<Position> mountainGroup = findConnectedMountains(position, new HashSet<>());
                visited.addAll(mountainGroup);

                // Chaque montagne du groupe rapporte des points (même isolée, si le groupe a au moins 2 montagnes)
                for (Position mountainPos : mountainGroup) {
                    Cell mountainCell = cells.get(mountainPos);
                    int height = mountainCell.getHeight();
                    // Une montagne rapporte des points si elle est connectée à au moins une autre
                    boolean isConnected = mountainGroup.size() > 1;
                    points += (!isConnected) ? 0 : (height == 1 ? 1 : height == 2 ? 3 : 6);
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

    public int calculateWaterPoints() {
        return waterFaceType == WaterFaceType.RIVER
                ? calculateWaterPointsFaceA()
                : calculateWaterPointsFaceB();
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

    @Override
    public String toString() {
        if (cells.isEmpty()) {
            return "=== Board State (3D) ===\n(Empty board)";
        }

        int minY = cells.keySet().stream().mapToInt(Position::y).min().orElse(0);
        int maxY = cells.keySet().stream().mapToInt(Position::y).max().orElse(0);

        StringBuilder sb = new StringBuilder();
        sb.append("=== Board State (3D) ===\n");

        // Déterminer la hauteur max du plateau
        int maxLevel = cells.values().stream().mapToInt(Cell::getHeight).max().orElse(0);

        for (int level = 1; level <= maxLevel; level++) {
            sb.append("--- Level ").append(level).append(" ---\n");

            for (int y = maxY; y >= minY; y--) {
                final int rowY = y;

                if (rowY % 2 == 1) sb.append("  "); // décalage hex

                List<Position> rowPositions = cells.keySet().stream()
                        .filter(p -> p.y() == rowY)
                        .sorted(Comparator.comparingInt(Position::x))
                        .toList();

                for (Position pos : rowPositions) {
                    Cell cell = cells.get(pos);

                    sb.append("(").append(pos.x()).append(",").append(pos.y()).append(")");

                    String tileSymbol = "  ";
                    if (cell.getStack().size() >= level) {
                        tileSymbol = getTileSymbol(cell.getStack().get(level - 1).getType());
                    }

                    // Affiche l’animal **sur le dernier niveau occupé** seulement
                    String animalSymbol = " ";
                    if (cell.hasAnimalCube() && level == cell.getHeight()) {
                        animalSymbol = getAnimalSymbol(cell.getAnimalCube());
                    }

                    sb.append("[").append(tileSymbol).append(animalSymbol).append("] ");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    private String getTileSymbol(TerrainType type) {
        if (type == null) return "?";
        return switch (type) {
            case WATER -> "🌊";
            case MOUNTAIN -> "⛰";
            case TREE -> "🌳";
            case FIELD -> "🌾";
            case BUILDING -> "🏠";
            default -> "❓";
        };
    }

    private String getAnimalSymbol(AnimalType type) {
        if (type == null) return "";
        return switch (type) {
            case DEER -> "🦌";
            case FOX -> "🦊";
            case WOLF -> "🐺";
            case FISH -> "🐟";
            case BAT -> "🦇";
            case HEDGEHOG -> "🦔";
            case BEAVER -> "🦫";
            case OTTER -> "🦦";
            case RABBIT -> "🐇";
            case FROG -> "🐸";
            case SQUIRREL -> "🐿️";
            case HAWK -> "🦅";
            case OWL -> "🦉";
            case TURTLE -> "🐢";
            case EAGLE -> "🦅";
            case RACCOON -> "🦝";
            case MOUSE -> "🐁";
            case BEAR -> "🐻";
            case DUCK -> "🦆";
            case SWAN -> "🦢";
            case HORSE -> "🐴";
            case COW -> "🐄";
            case SHEEP -> "🐑";
            case CAT -> "🐈";
            case DOG -> "🐕";
            case RAVEN -> "🐦";
            case CHICKEN -> "🐔";
            case SHEEPDOG -> "🐕‍🦺";
            case GOAT -> "🐐";
            case CRANE -> "🦩";
            case PIG -> "🐖";
            case TURKEY -> "🦃";
        };
    }
}
