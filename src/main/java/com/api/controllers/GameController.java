package com.api.controllers;

import com.api.dtos.request.CreatePlayerRequest;
import com.api.dtos.request.PlaceAnimalCubeRequest;
import com.api.dtos.request.PlaceTilesRequest;
import com.api.dtos.request.TakeFromSlotRequest;
import com.api.dtos.response.DrawnTilesResponse;
import com.api.dtos.response.GameStateResponse;
import com.api.storages.GameStorage;
import com.common_game.cards.AnimalCard;
import com.common_game.models.Game;
import com.common_game.models.Player;
import com.enums.AnimalType;
import com.enums.TerrainType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameController {

    // TODO Remplacer par un stockage en BDD propre maybe
    private final GameStorage gameStorage = new GameStorage();

    @PostMapping("/create")
    public ResponseEntity<GameStateResponse> createGame(@RequestBody List<CreatePlayerRequest> playersRequest) {
        List<Player> players = playersRequest.stream()
                .map(request -> new Player(UUID.randomUUID(), request.getName(), request.getWaterFaceType()))
                .collect(Collectors.toList());
        Game game = new Game(players);
        gameStorage.save(game);
        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @PostMapping("/{gameId}/start")
    public ResponseEntity<GameStateResponse> startGame(@PathVariable UUID gameId) {
        Game game = gameStorage.findById(gameId);
        game.start();
        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @PostMapping("/{gameId}/take-from-slot")
    public ResponseEntity<DrawnTilesResponse> takeFromSlot(
            @PathVariable UUID gameId,
            @RequestBody TakeFromSlotRequest request) {
        Game game = gameStorage.findById(gameId);
        DrawnTilesResponse response = game.takeTilesFromCentralBoard(request.getPlayerUUID(), request.getSlotNumber());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{gameId}/place-tiles")
    public ResponseEntity<GameStateResponse> placeTiles(
            @PathVariable UUID gameId,
            @RequestBody PlaceTilesRequest request) {

        Game game = gameStorage.findById(gameId);
        UUID currentPlayerId = game.getCurrentPlayer().getId();

        game.placeTiles(currentPlayerId, request.getTilePlacements(), request.getSlotNumber());

        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @GetMapping("/{gameId}/available-animal-cards")
    public ResponseEntity<List<AnimalCard>> getAvailableAnimalCards(@PathVariable UUID gameId) {
        Game game = gameStorage.findById(gameId);
        return ResponseEntity.ok(game.getAvailableAnimalCards().stream().limit(5).collect(Collectors.toList()));
    }

    @PostMapping("/{gameId}/take-animal-card")
    public ResponseEntity<GameStateResponse> takeAnimalCard(
            @PathVariable UUID gameId,
            @RequestBody Map<String, AnimalType> request) { // {"animalType": "DEER"}
        Game game = gameStorage.findById(gameId);
        game.takeAnimalCard(game.getCurrentPlayer().getId(), request.get("animalType"));
        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @PostMapping("/{gameId}/place-animal-cube")
    public ResponseEntity<GameStateResponse> placeAnimalCube(
            @PathVariable UUID gameId,
            @RequestBody PlaceAnimalCubeRequest request) { // {"animalType": "DEER", "position": {...}}
        Game game = gameStorage.findById(gameId);
        game.placeAnimalCube(game.getCurrentPlayer().getId(), request.getPosition(), request.getAnimalType());
        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @GetMapping("/{gameId}/acutal_scores")
    public ResponseEntity<Map<UUID, Integer>> getScores(@PathVariable UUID gameId) {
        Game game = gameStorage.findById(gameId);
        return ResponseEntity.ok(game.calculateScores());
    }


    @GetMapping("/{gameId}/end_scores")
    public ResponseEntity<Integer> calculateScore(
            @PathVariable UUID gameId,
            @RequestParam UUID playerId,
            @RequestParam boolean isWaterFaceA) {
        Game game = gameStorage.findById(gameId);
        int score = game.calculateFinalScore(playerId, isWaterFaceA);
        return ResponseEntity.ok(score);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateResponse> getGameState(@PathVariable UUID gameId) {
        Game game = gameStorage.findById(gameId);
        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @GetMapping("/{gameId}/central-board")
    public ResponseEntity<Map<Integer, List<TerrainType>>> getCentralBoard(@PathVariable UUID gameId) {
        Game game = gameStorage.findById(gameId);
        Map<Integer, List<TerrainType>> centralBoardState = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            centralBoardState.put(i, game.getCentralBoard().getSlotTiles(i));
        }
        return ResponseEntity.ok(centralBoardState);
    }

    // Helper : Convertir Game en GameStateResponse
    private GameStateResponse mapToGameStateResponse(Game game) {
        GameStateResponse response = new GameStateResponse();
        response.setGameId(game.getId());
        response.setStatus(game.getStatus());
        response.setPlayers(game.getPlayers());
        response.setCurrentPlayerUUID(game.getCurrentPlayerUUID());
        return response;
    }
}
