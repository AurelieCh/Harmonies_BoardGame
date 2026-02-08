package com.api.controllers;

import com.api.dtos.request.CreatePlayerRequest;
import com.api.dtos.request.PlaceAnimalCubeRequest;
import com.api.dtos.request.TakeFromSlotRequest;
import com.api.dtos.request.TilesPlacementRequest;
import com.api.dtos.response.DrawnTilesResponse;
import com.api.dtos.response.GameStateResponse;
import com.api.dtos.response.RemainingCardsResponse;
import com.api.dtos.response.ScoresResponse;
import com.api.storages.GameStorage;
import com.common_game.cards.AnimalCard;
import com.common_game.models.Board;
import com.common_game.models.Game;
import com.common_game.models.Player;
import com.enums.AnimalType;
import com.enums.TerrainType;
import com.exceptions.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {
    private final GameStorage gameStorage;

    @PostMapping("/create")
    public GameStateResponse createGame(@RequestBody List<CreatePlayerRequest> playersRequest) {

        List<Player> players = playersRequest.stream()
                .map(req -> new Player(UUID.randomUUID(), req.getName(), req.getWaterFaceType()))
                .toList();

        Game game = new Game(players);
        gameStorage.save(game);

        return mapToGameStateResponse(game);
    }

    @PostMapping("/{gameId}/start")
    public GameStateResponse startGame(@PathVariable UUID gameId) {
        Game game = getGameOrThrow(gameId);
        game.start();
        return mapToGameStateResponse(game);
    }

    @PostMapping("/{gameId}/take-from-slot")
    public DrawnTilesResponse takeFromSlot(
            @PathVariable UUID gameId,
            @RequestBody TakeFromSlotRequest request) {

        Game game = getGameOrThrow(gameId);
        return game.takeTilesFromCentralBoard(
                request.getPlayerUUID(),
                request.getSlotNumber()
        );
    }

    @PostMapping("/{gameId}/place-tiles")
    public GameStateResponse placeTiles(
            @PathVariable UUID gameId,
            @RequestBody List<TilesPlacementRequest> request) {

        Game game = getGameOrThrow(gameId);

        game.placeTiles(game.getCurrentPlayer().getId(), request);

        return mapToGameStateResponse(game);
    }


    @GetMapping("/{gameId}/available-animal-cards")
    public List<AnimalCard> getAvailableAnimalCards(@PathVariable UUID gameId) {
        Game game = getGameOrThrow(gameId);
        return game.getAnimalDeck().getAvailableCards();
    }

    @PostMapping("/{gameId}/take-animal-card")
    public GameStateResponse takeAnimalCard(
            @PathVariable UUID gameId,
            @RequestBody Map<String, AnimalType> request) {

        Game game = getGameOrThrow(gameId);
        game.takeAnimalCard(
                game.getCurrentPlayer().getId(),
                request.get("animalType")
        );

        return mapToGameStateResponse(game);
    }

    @PostMapping("/{gameId}/place-animal-cube")
    public GameStateResponse placeAnimalCube(
            @PathVariable UUID gameId,
            @RequestBody PlaceAnimalCubeRequest request) {

        Game game = getGameOrThrow(gameId);
        game.placeAnimalCube(
                game.getCurrentPlayer().getId(),
                request.getPosition(),
                request.getAnimalType()
        );

        return mapToGameStateResponse(game);
    }

    @PostMapping("/{gameId}/next")
    public GameStateResponse nextPersonTurn(@PathVariable UUID gameId) {
        Game game = getGameOrThrow(gameId);

        game.endTurn();
        return mapToGameStateResponse(game);
    }

    @GetMapping("/{gameId}/actual_scores")
    public List<ScoresResponse> getScores(@PathVariable UUID gameId) {
        return getGameOrThrow(gameId).calculateScores();
    }

    @GetMapping("/{gameId}/end-scores")
    public int calculateFinalScore(
            @PathVariable UUID gameId,
            @RequestParam UUID playerId,
            @RequestParam boolean isWaterFaceA) {

        return getGameOrThrow(gameId)
                .calculateFinalScore(playerId, isWaterFaceA);
    }

    @GetMapping("/{gameId}")
    public GameStateResponse getGameState(@PathVariable UUID gameId) {
        return mapToGameStateResponse(getGameOrThrow(gameId));
    }

    @GetMapping
    public List<GameStateResponse> getGames() {
        return gameStorage.getGames().stream()
                .map(this::mapToGameStateResponse)
                .toList();
    }

    @GetMapping("/{gameId}/central-board")
    public Map<Integer, List<TerrainType>> getCentralBoard(@PathVariable UUID gameId) {

        Game game = getGameOrThrow(gameId);
        Map<Integer, List<TerrainType>> board = new HashMap<>();

        for (int i = 1; i <= 5; i++) {
            board.put(i, game.getCentralBoard().getSlotTiles(i));
        }

        return board;
    }

    @GetMapping("/{gameId}/remaining-tiles")
    public RemainingCardsResponse getRemainingTiles(@PathVariable UUID gameId) {

        Game game = getGameOrThrow(gameId);
        return new RemainingCardsResponse(
                game.getTerrainDeck().getTiles().size(),
                game.getTerrainDeck().getTiles()
        );
    }

    @GetMapping("/{gameId}/players/{playerId}/board")
    public Map<String, Object> getPlayerBoard(
            @PathVariable UUID gameId,
            @PathVariable UUID playerId) {

        Game game = getGameOrThrow(gameId);
        Board board = game.getBoards().get(playerId);

        // JSON board
        String[] boardLines = board.toString().split("\n");

        // Create a response beautiful
        Map<String, Object> response = new HashMap<>();
        response.put("board", boardLines);
        response.put("playerId", playerId);
        response.put("gameId", gameId);

        return response;
    }


    // -------- Mapping --------

    private GameStateResponse mapToGameStateResponse(Game game) {
        GameStateResponse response = new GameStateResponse();
        response.setGameId(game.getId());
        response.setStatus(game.getStatus());
        response.setPlayers(game.getPlayers());
        response.setCurrentPlayerUUID(game.getCurrentPlayerUUID());
        response.setNbOfAnimalCardsLeft(game.getAnimalDeck().getAnimalCards().size());
        response.setNbOfTileLeft(game.getTerrainDeck().getTiles().size());
        response.setNbOfAnimalCubeLeft(game.getCentralBoard().getNbOfAnimalCubeLeft());
        return response;
    }

    // -------- Helper --------

    private Game getGameOrThrow(UUID gameId) {
        return gameStorage.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId.toString()));
    }
}

