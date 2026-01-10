package com.common_game.models;

import com.common_game.cards.AnimalCard;
import com.common_game.decks.AnimalDeck;
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
        this.availableAnimalCards = new AnimalDeck().getCards();
        this.boards = players.stream().collect(Collectors.toMap(Player::getId, p -> new Board()));
    }

    // First step : Draw a card
    public TerrainTile drawTile(UUID playerId) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(ErrorCode.GAME_NOT_STARTED, "Game is not in progress");
        }
        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.getId().equals(playerId)) {
            throw new GameException(ErrorCode.NOT_YOUR_TURN, "It's not your turn");
        }
        if (terrainDeck.isEmpty()) {
            throw new GameException(ErrorCode.INVALID_MOVE, "No more terrain tiles available");
        }
        return terrainDeck.draw(); // Pioche une tuile du deck initialisé
    }

    // Second step : Place the tile
    public void placeTile(UUID playerId, Position position, TerrainType tileType) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new GameException(ErrorCode.GAME_NOT_STARTED, "Game is not in progress");
        }
        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.getId().equals(playerId)) {
            throw new GameException(ErrorCode.NOT_YOUR_TURN, "It's not your turn");
        }

        // Vérifie que la tuile correspond à celle piochée
        Board board = boards.get(playerId);
        board.placeTile(position, new TerrainTile(tileType));

        // Vérifie les règles d'habitat pour les cartes animales
        checkAnimalCards(playerId, position);

        // Passe au joueur suivant
        nextTurn();
    }

    // Méthode pour vérifier les cartes animales
    private void checkAnimalCards(UUID playerId, Position position) {
        Player player = getPlayerById(playerId);
        Board board = boards.get(playerId);

        for (AnimalCard card : player.getAnimalCards()) {
            if (!card.isComplete() && card.getRule().isSatisfied(board, position)) {
                board.placeAnimalCube(position, card.getType());
                card.incrementPlacedCubes();
                player.addPoints(card.getPoints());
            }
        }
    }

    // Helper pour obtenir le joueur actuel
    private Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // Helper pour obtenir un joueur par ID
    private Player getPlayerById(UUID playerId) {
        return players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new GameException(ErrorCode.INVALID_MOVE, "Player not found"));
    }

    // Méthode pour passer au tour suivant
    private void nextTurn() {
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
