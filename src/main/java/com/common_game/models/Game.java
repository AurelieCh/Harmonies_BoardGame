package com.common_game.models;

import com.api.dtos.response.DrawnTilesResponse;
import com.api.dtos.response.ScoresResponse;
import com.common_game.cards.AnimalCard;
import com.common_game.decks.AnimalDeck;
import com.common_game.decks.Position;
import com.common_game.decks.TerrainDeck;
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
    private List<AnimalCard> availableAnimalCards;
    private Map<UUID, Board> boards;
    private final CentralBoard centralBoard;

    public Game(List<Player> players) {
        this.id = UUID.randomUUID();
        this.status = GameStatus.WAITING;
        this.players = players;
        this.currentPlayerUUID = players.getFirst().getId();
        this.terrainDeck = new TerrainDeck();
        this.availableAnimalCards = new AnimalDeck().getCards();
        this.boards = players.stream()
                .collect(Collectors.toMap(
                        Player::getId,
                        player -> new Board(player.getWaterFaceType())
                ));
        this.centralBoard = new CentralBoard(terrainDeck);
    }

    /**
     * Method to draw tiles from the central board
     * @param slotNumber Slot number where the player want to pick the tiles
     * @return A List of terrain tiles taken from the central board slot number slotNumber
     */
    public DrawnTilesResponse takeTilesFromCentralBoard(UUID playerId, int slotNumber) {
        // Checks
        if (!currentPlayerUUID.equals(playerId)) {
            throw new GameException(ErrorCode.NOT_YOUR_TURN, "Not your turn");
        }
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Game not started");
        }

        // Draw 3 tiles
        List<TerrainTile> tiles = centralBoard.takeTilesFromSlot(slotNumber);

        // Store tiles type
        tilesToPlace = tiles.stream().map(TerrainTile::getType).collect(Collectors.toList());
        this.slotNumberToRefill = slotNumber;

        // Mod WAITING
        status = GameStatus.WAITING_FOR_PLACEMENT;

        // Return drawn tiles
        return new DrawnTilesResponse(tilesToPlace);
    }

    /**
     * Method that allows a player to place a tile at a position
     * @param playerId Player's UUID
     * @param tilePlacements Position where the player want to place the tiles
     */
    public void placeTiles(UUID playerId, Map<Position, TerrainType> tilePlacements) {
        // Checks
        if (!currentPlayerUUID.equals(playerId)) {
            throw new GameException(ErrorCode.NOT_YOUR_TURN, "Not your turn");
        }
        if (status != GameStatus.WAITING_FOR_PLACEMENT) {
            throw new GameException(ErrorCode.INVALID_MOVE, "You must draw tiles first");
        }
        if (tilePlacements.size() != tilesToPlace.size()) {
            throw new GameException(ErrorCode.INVALID_MOVE, "You must place all 3 tiles");
        }

        // Checks that places tiles are the drawn type
        List<TerrainType> placedTypes = new ArrayList<>(tilePlacements.values());
        if (!new HashSet<>(placedTypes).containsAll(tilesToPlace) || !new HashSet<>(tilesToPlace).containsAll(placedTypes)) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Tile types do not match drawn tiles");
        }

        // Place tile on the board
        Board board = boards.get(playerId);
        for (Map.Entry<Position, TerrainType> entry : tilePlacements.entrySet()) {
            board.placeTile(entry.getKey(), new TerrainTile(entry.getValue()));
        }

        // Refill central board
        centralBoard.refillSlot(this.slotNumberToRefill);
        this.slotNumberToRefill = null;

        // Next player's turn
        nextTurn();
        status = GameStatus.IN_PROGRESS;
        tilesToPlace = null;
    }

    public void placeAnimalCube(UUID playerId, Position position, AnimalType animalType) {
        Player player = getPlayerById(playerId);
        AnimalCard card = player.getAnimalCards().stream()
                .filter(c -> c.getType() == animalType)
                .findFirst()
                .orElseThrow(() -> new GameException(ErrorCode.INVALID_MOVE, "Animal card not found"));

        Board board = boards.get(playerId);
        board.placeAnimalCube(position, animalType);
        card.incrementPlacedCubes();
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
     * Method that allows the actual player turn to end to move to the other
     */
    private void nextTurn() {
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
        Player player = getPlayerById(playerId);
        if (player.getAnimalCards().size() >= 4) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Maximum 4 animal cards allowed");
        }

        // Filter on all available card with the good animalType
        List<AnimalCard> availableCardsOfType = availableAnimalCards.stream()
                .filter(card -> card.getType() == animalType)
                .toList();

        if (availableCardsOfType.isEmpty()) {
            throw new GameException(ErrorCode.INVALID_MOVE, "No available card for " + animalType);
        }

        // Get one randomly
        AnimalCard chosenCard = availableCardsOfType.get(new Random().nextInt(availableCardsOfType.size()));
        player.addAnimalCard(chosenCard);
        availableAnimalCards.remove(chosenCard);

        // Refill the available card to stay at 5
        if (availableAnimalCards.size() < 5) {
            availableAnimalCards.add(new AnimalDeck().getCards().removeFirst());
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
