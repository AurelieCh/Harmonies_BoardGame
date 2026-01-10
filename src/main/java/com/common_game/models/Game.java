package com.common_game.models;

import com.common_game.cards.AnimalCard;
import com.common_game.decks.Position;
import com.common_game.decks.TerrainDeck;
import com.common_game.rules.BirdRule;
import com.common_game.rules.DeerRule;
import com.common_game.rules.FishRule;
import com.common_game.rules.FoxRule;
import com.enums.AnimalType;
import com.enums.ErrorCode;
import com.enums.GameStatus;
import com.enums.TerrainType;
import com.exceptions.GameException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class Game {
    private UUID id;
    private GameStatus status;
    private List<Player> players;
    private int currentPlayerIndex;
    private TerrainDeck terrainDeck;
    private List<AnimalCard> availableAnimalCards;
    private Map<UUID, Board> boards;

    public Game(List<Player> players) {
        this.id = UUID.randomUUID();
        this.status = GameStatus.WAITING;
        this.players = players;
        this.currentPlayerIndex = 0;
        this.terrainDeck = new TerrainDeck();
        this.availableAnimalCards = new ArrayList<>(List.of(
                new AnimalCard(UUID.randomUUID(), AnimalType.DEER, 3, new DeerRule(), List.of(TerrainType.FOREST), 1, 2),
                new AnimalCard(UUID.randomUUID(), AnimalType.FOX, 2, new FoxRule(), List.of(TerrainType.MOUNTAIN, TerrainType.FOREST), 2, 1),
                new AnimalCard(UUID.randomUUID(), AnimalType.BIRD, 2, new BirdRule(), List.of(TerrainType.WATER), 1, 1),
                new AnimalCard(UUID.randomUUID(), AnimalType.FISH, 2, new FishRule(), List.of(TerrainType.WATER), 1, 1)
        ));
        this.boards = players.stream().collect(Collectors.toMap(Player::getId, p -> new Board()));
    }

    /**
     * Method capable to play a turn of the game for one player
     * @param playerId The UUID of the player
     * @param position Position where the player want to place the tile
     */
    public void playTurn(UUID playerId, Position position) {
        // 1. Checks
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(ErrorCode.GAME_NOT_STARTED, "Game is not in progress");
        }
        Player currentPlayer = players.get(currentPlayerIndex);
        if (!currentPlayer.getId().equals(playerId)) {
            throw new GameException(ErrorCode.NOT_YOUR_TURN, "It's not your turn");
        }

        // 2. Place the tile
        TerrainTile tile = terrainDeck.draw();
        Board board = boards.get(playerId);
        board.placeTile(position, tile);

        // 3. check the habitat rules for ALL animal cards of the player
        for (AnimalCard card : currentPlayer.getAnimalCards()) {
            if (!card.isComplete() && card.getRule().isSatisfied(board, position)) {
                board.placeAnimalCube(position, card.getType());
                card.incrementPlacedCubes();
                currentPlayer.addPoints(card.getPoints());
            }
        }

        // 4. Next player turn
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Assigns the specified animal card to the player's hand, if the player has fewer than 4 cards.
     * The card is removed from the global pool of available animal cards.
     *
     * @param playerId The UUID of the player taking the card.
     * @param card The animal card to be assigned to the player.
     * @throws GameException If the player is not found, or if the player already has 4 animal cards.
     */
    public void takeAnimalCard(UUID playerId, AnimalCard card) {
        Player player = players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new GameException(ErrorCode.INVALID_MOVE, "Player not found"));
        if (player.getAnimalCards().size() >= 4) {
            throw new GameException(ErrorCode.INVALID_MOVE, "Maximum 4 animal cards allowed");
        }
        player.addAnimalCard(card);
        availableAnimalCards.remove(card);
    }

    /**
     * Draws the first available animal card from the global pool.
     * This is typically used to replenish the visible cards on the central board or for game setup.
     *
     * @return The drawn animal card
     * @throws GameException If no animal cards are left in the pool
     */
    public AnimalCard drawAnimalCard() {
        if (availableAnimalCards.isEmpty()) {
            throw new GameException(ErrorCode.INVALID_MOVE, "No more animal cards available");
        }
        return availableAnimalCards.removeFirst();
    }

    /**
     * Method that changes the status of the game to IN_PROGRESS for the start
     */
    public void start() {
        this.status = GameStatus.IN_PROGRESS;
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
