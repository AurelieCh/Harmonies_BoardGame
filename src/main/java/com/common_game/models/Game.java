package com.common_game.models;

import com.api.dtos.request.TilesPlacementRequest;
import com.api.dtos.response.DrawnTilesResponse;
import com.api.dtos.response.ScoresResponse;
import com.common_game.cards.AnimalCard;
import com.common_game.decks.AnimalDeck;
import com.common_game.decks.Position;
import com.common_game.decks.TerrainDeck;
import com.common_game.rules.TurnState;
import com.enums.AnimalType;
import com.enums.ErrorCode;
import com.enums.GameStatus;
import com.enums.TerrainType;
import com.exceptions.GameException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class Game {
    private UUID id;
    private GameStatus status;
    private List<Player> players;
    private UUID currentPlayerUUID;
    private Integer slotNumberToRefill;
    private List<TerrainType> tilesToPlace;
    private TerrainDeck terrainDeck;
    private AnimalDeck animalDeck;
    private Map<UUID, Board> boards;
    private final CentralBoard centralBoard;
    private TurnState turnState;

    public Game(List<Player> players) {
        this.id = UUID.randomUUID();
        this.status = GameStatus.WAITING;
        this.players = players;
        this.currentPlayerUUID = players.getFirst().getId();
        this.terrainDeck = new TerrainDeck();
        this.animalDeck = new AnimalDeck();
        this.boards = players.stream()
                .collect(Collectors.toMap(
                        Player::getId,
                        player -> new Board(player.getWaterFaceType())
                ));
        this.centralBoard = new CentralBoard(terrainDeck);
        this.turnState = new TurnState();
    }

    /**
     * Method to draw tiles from the central board
     * @param slotNumber Slot number where the player want to pick the tiles
     * @return A List of terrain tiles taken from the central board slot number slotNumber
     */
    public DrawnTilesResponse takeTilesFromCentralBoard(UUID playerId, int slotNumber) {
        // Checks
        if (!currentPlayerUUID.equals(playerId)) {
            throw new GameException(
                    ErrorCode.NOT_YOUR_TURN,
                    "Not your turn"
            );
        }
        if (status == GameStatus.WAITING_FOR_PLACEMENT) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "Tiles have already been drawn this turn"
            );
        }
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(
                    ErrorCode.GAME_NOT_STARTED,
                    "Game not started"
            );
        }

        // Draw 3 tiles
        List<TerrainTile> tiles = centralBoard.takeTilesFromSlot(slotNumber);

        // Store tiles type
        tilesToPlace = tiles.stream().map(TerrainTile::getType).collect(Collectors.toList());
        this.slotNumberToRefill = slotNumber;

        // TurnState -> Draw as been done
        turnState.drawTiles();

        // Mod WAITING
        status = GameStatus.WAITING_FOR_PLACEMENT;

        // Return drawn tiles
        return new DrawnTilesResponse(tilesToPlace);
    }

    /**
     * Method that allows a player to place a tile at positions
     * Transactional: either all tiles are placed, or none
     *
     * @param playerId UUID of the player
     * @param tilePlacements list of tiles to place (optionally with height)
     */
    public void placeTiles(UUID playerId, List<TilesPlacementRequest> tilePlacements) {
        checkTurnAndStatus(playerId, tilePlacements);
        validateTileCounts(tilePlacements);

        Board board = boards.get(playerId);

        List<TilesPlacementRequest> sortedTiles = sortByHeight(tilePlacements);

        validatePlacementRules(board, sortedTiles);

        placeTilesOnBoard(board, sortedTiles);

        refillAndFinishTurn();
    }

    private void checkTurnAndStatus(UUID playerId, List<TilesPlacementRequest> tilePlacements) {
        if (!currentPlayerUUID.equals(playerId)) {
            throw new GameException(ErrorCode.NOT_YOUR_TURN, "Not your turn");
        }
        if (status != GameStatus.WAITING_FOR_PLACEMENT) {
            throw new GameException(ErrorCode.INVALID_MOVE, "You must draw tiles first");
        }
        if (turnState.isTilesPlaced()) {
            throw new GameException(ErrorCode.INVALID_MOVE, "You already placed the tiles");
        }
        if (tilePlacements.size() != tilesToPlace.size()) {
            throw new GameException(ErrorCode.INVALID_MOVE, "You must place all tiles");
        }
    }

    private void validateTileCounts(List<TilesPlacementRequest> tilePlacements) {
        Map<TerrainType, Long> drawnCounts = tilesToPlace.stream()
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        Map<TerrainType, Long> placedCounts = tilePlacements.stream()
                .map(TilesPlacementRequest::getTerrainType)
                .collect(Collectors.groupingBy(t -> t, Collectors.counting()));

        if (!drawnCounts.equals(placedCounts)) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Tile types do not match drawn tiles");
        }
    }

    private List<TilesPlacementRequest> sortByHeight(List<TilesPlacementRequest> tilePlacements) {
        List<TilesPlacementRequest> sorted = new ArrayList<>(tilePlacements);
        sorted.sort(Comparator.comparing(tp -> tp.getHeight() != null ? tp.getHeight() : Integer.MAX_VALUE));
        return sorted;
    }

    private void validatePlacementRules(Board board, List<TilesPlacementRequest> sortedTiles) {
        boolean isFirstTile = board.getCells().values().stream().allMatch(c -> c.getStack().isEmpty());

        // Grouper par cellule pour simuler la pile
        Map<Position, List<TilesPlacementRequest>> byCell = sortedTiles.stream()
                .collect(Collectors.groupingBy(TilesPlacementRequest::getPosition));

        for (Map.Entry<Position, List<TilesPlacementRequest>> entry : byCell.entrySet()) {
            Position pos = entry.getKey();
            Cell cell = board.getCells().get(pos);

            if (cell == null) throw new GameException(ErrorCode.INVALID_MOVE, "Tile must be within the board range");

            // Trier les tuiles de cette cellule par height
            List<TilesPlacementRequest> cellPlacements = entry.getValue();
            cellPlacements.sort(Comparator.comparing(tp -> tp.getHeight() != null ? tp.getHeight() : Integer.MAX_VALUE));

            int simulatedHeight = cell.getHeight(); // Hauteur avant ce tour
            for (TilesPlacementRequest placement : cellPlacements) {
                if (simulatedHeight >= 3)
                    throw new GameException(ErrorCode.INVALID_MOVE, "Cannot place more than 3 tiles on a cell");

                if (placement.getHeight() != null && placement.getHeight() != simulatedHeight)
                    throw new GameException(ErrorCode.INVALID_MOVE, "Cannot skip levels when placing tiles");

                simulatedHeight++; // On simule l’empilement pour la prochaine tile sur cette cellule
            }
        }

        // Vérification adjacency après simulation
        for (TilesPlacementRequest placement : sortedTiles) {
            if (!isFirstTile) {
                Position pos = placement.getPosition();
                boolean hasAdjacent = pos.adjacent().stream().anyMatch(adj -> {
                    Cell c = board.getCells().get(adj);
                    return c != null && !c.getStack().isEmpty();
                });
                if (!hasAdjacent)
                    throw new GameException(ErrorCode.INVALID_MOVE, "All tiles must be adjacent to existing tiles");
            }
        }
    }


    private void placeTilesOnBoard(Board board, List<TilesPlacementRequest> sortedTiles) {
        for (TilesPlacementRequest placement : sortedTiles) {
            Integer height = placement.getHeight();
            TerrainTile tile = new TerrainTile(placement.getTerrainType());
            board.placeTile(placement.getPosition(), tile, height);
        }
    }

    private void refillAndFinishTurn() {
        centralBoard.refillSlot(this.slotNumberToRefill);
        this.slotNumberToRefill = null;
        turnState.placeTiles();
        status = GameStatus.IN_PROGRESS;
        tilesToPlace = null;
    }

    public void placeAnimalCube(UUID playerId, Position position, AnimalType animalType) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(
                    ErrorCode.GAME_NOT_STARTED,
                    "Game not started"
            );
        }
        if (turnState.isAnimalCubePlaced()) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "Cube already placed this turn"
            );
        }

        Player player = getPlayerById(playerId);
        AnimalCard card = player.getAnimalCards().stream()
                .filter(c -> c.getType() == animalType)
                .findFirst()
                .orElseThrow(() -> new GameException(ErrorCode.INVALID_MOVE, "Animal card not found"));

        Board board = boards.get(playerId);
        board.placeAnimalCube(position, animalType, centralBoard, card);
        card.incrementPlacedCubes();

        turnState.placeAnimalCube();
    }

    /**
     * Get current player
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return getPlayerById(currentPlayerUUID);
    }

    /**
     * Get à player from it's UUID
     * @param playerId Player's UUID
     * @return The player
     */
    private Player getPlayerById(UUID playerId) {
        return players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new GameException(ErrorCode.INVALID_MOVE, "Player not found"));
    }

    /**
     * Method to assert that the player as done the necessary actions to end his turn
     */
    public void endTurn() {
        turnState.assertCanEndTurn();
        nextTurn();
    }


    /**
     * Method that allows the actual player turn to end to move to the other
     */
    public void nextTurn() {
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(
                    ErrorCode.GAME_NOT_STARTED,
                    "Game not started"
            );
        }

        // Find actual index player
        int currentIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getId().equals(currentPlayerUUID)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex == -1) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Current player not found");
        }

        // Go to the next or the first player
        int nextIndex = (currentIndex + 1) % players.size();
        currentPlayerUUID = players.get(nextIndex).getId();

        // reinit the state of the TurnState class for the next player
        turnState.reinit();
    }


    /**
     * Assigns the specified animal card to the player's hand, if the player has fewer than 4 cards.
     * The card is removed from the global pool of available animal cards.
     *
     * @param playerId   The UUID of the player taking the card.
     * @param animalType The animal card type to be assigned to the player.
     * @throws GameException If the player is not found, or if the player already has 4 animal cards.
     */
    public void takeAnimalCard(UUID playerId, AnimalType animalType) {
        // Checks
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(
                    ErrorCode.GAME_NOT_STARTED,
                    "Game not started"
            );
        }
        if (!EnumSet.allOf(AnimalType.class).contains(animalType)) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "Unknown animal type: " + animalType
            );
        }


        Player player = getPlayerById(playerId);

        if (player.getAnimalCards().size() >= 4) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Maximum 4 animal cards allowed");
        }

        try {
            if (turnState.isAnimalCardDrawn()) {
                throw new GameException(
                        ErrorCode.INVALID_MOVE,
                        "Card has already been drawn"
                );
            }
            // Draw the animal type card among those visible
            AnimalCard chosenCard = animalDeck.drawByType(animalType);
            // Turnstate draw animal card have been done
            turnState.drawAnimalCard();
            player.addAnimalCard(chosenCard);
        } catch (IllegalArgumentException e) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "No available card for " + animalType
            );
        }
    }

    /**
     * Method that changes the status of the game to IN_PROGRESS for the start
     */
    public void start() {
        this.status = GameStatus.IN_PROGRESS;
    }

    /**
     * Method to check if the game is finished or not
     * @return A boolean
     */
    public boolean isGameOver() {
        boolean isTerrainDeckEmpty = terrainDeck.isEmpty();

        boolean anyPlayerBoardAlmostFull = boards.values().stream()
                .anyMatch(board -> board.getEmptyCellsCount() <= 2);

        return isTerrainDeckEmpty || anyPlayerBoardAlmostFull;
    }

    public List<ScoresResponse> calculateScores() {
        List<ScoresResponse> scores = new ArrayList<>();
        for (Map.Entry<UUID, Board> entry : boards.entrySet()) {
            UUID playerId = entry.getKey();
            Board board = entry.getValue();
            int landscapePoints = board.calculateTreePoints() +
                    board.calculateMountainPoints() +
                    board.calculateWaterPoints() +
                    board.calculateFieldPoints() +
                    board.calculateBuildingPoints();
            int animalPoints = players.stream()
                    .filter(p -> p.getId().equals(playerId))
                    .findFirst()
                    .map(player -> player.getAnimalCards().stream()
                            .mapToInt(AnimalCard::getPoints)
                            .sum())
                    .orElse(0);
            ScoresResponse scoresResponse = new ScoresResponse(this.getPlayerById(playerId).getName(), playerId, landscapePoints + animalPoints);
            scores.add(scoresResponse);
        }
        return scores;
    }

    /**
     * Calculates the final score for a player at the end of the game.
     * The score is the sum of points from landscapes (trees, mountains, fields, water, buildings)
     * and animal cards placed during the game.
     *
     * @param playerId The UUID of the player whose score is being calculated
     * @param isWaterFaceA A boolean indicating whether the player's board is using the "River" face (true)
     *                     or the "Islands" face (false) for water terrain scoring
     * @return The total score of the player, combining landscape and animal points.
     * @throws GameException If the player is not found in the current game.
     */
    public int calculateFinalScore(UUID playerId, boolean isWaterFaceA) {
        Player player = players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new GameException(ErrorCode.INVALID_MOVE, "Player not found"));
        Board board = boards.get(playerId);

        // Landscape points (trees, mountains, fields, water, buildings)
        int landscapePoints = board.calculateTotalLandscapePoints(isWaterFaceA);

        // Animal points (sum of points from all animal cards, completed or not)
        int animalPoints = player.getAnimalCards().stream()
                .mapToInt(AnimalCard::getPoints)
                .sum();

        return landscapePoints + animalPoints;
    }
}
