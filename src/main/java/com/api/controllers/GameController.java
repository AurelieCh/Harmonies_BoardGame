package com.api.controllers;

import com.api.dtos.ApiError;
import com.api.dtos.request.PlaceTileRequest;
import com.api.dtos.request.PlayTurnRequest;
import com.api.dtos.request.TakeAnimalCardRequest;
import com.api.dtos.response.DrawnTileResponse;
import com.api.dtos.response.GameStateResponse;
import com.api.storages.GameStorage;
import com.common_game.models.Game;
import com.common_game.models.Player;
import com.common_game.models.TerrainTile;
import com.enums.ErrorCode;
import com.exceptions.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/game")
public class GameController {

    // TODO Remplacer par un stockage en BDD propre
    private final GameStorage gameStorage = new GameStorage();

    @PostMapping("/create")
    public ResponseEntity<GameStateResponse> createGame(@RequestBody List<String> playerNames) {
        List<Player> players = playerNames.stream()
                .map(name -> new Player(UUID.randomUUID(), name))
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

    // Étape 1 : Le joueur pioche une tuile
    @PostMapping("/{gameId}/draw-tile")
    public ResponseEntity<?> drawTile(@PathVariable UUID gameId, @RequestBody UUID playerId) {
        try {
            Game game = gameStorage.findById(gameId);
            TerrainTile tile = game.drawTile(playerId);
            return ResponseEntity.ok(new DrawnTileResponse(tile.getType()));
        } catch (GameException e) {
            return ResponseEntity.badRequest().body(new ApiError(e.getErrorCode(), e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiError(ErrorCode.INVALID_MOVE, "No more terrain tiles available"));
        }
    }


    // Étape 2 : Le joueur place la tuile piochée
    @PostMapping("/{gameId}/place-tile")
    public ResponseEntity<GameStateResponse> placeTile(
            @PathVariable UUID gameId,
            @RequestBody PlaceTileRequest request) {
        Game game = gameStorage.findById(gameId);
        game.placeTile(request.getPlayerId(), request.getPosition(), request.getTileType());
        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @PostMapping("/{gameId}/take-animal-card")
    public ResponseEntity<GameStateResponse> takeAnimalCard(
            @PathVariable UUID gameId,
            @RequestBody TakeAnimalCardRequest request) {
        Game game = gameStorage.findById(gameId);
        // Logique pour trouver la carte par cardId et l'assigner au joueur
        // game.takeAnimalCard(request.getPlayerId(), card);
        return ResponseEntity.ok(mapToGameStateResponse(game));
    }

    @GetMapping("/{gameId}/score")
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

    // Helper : Convertir Game en GameStateResponse
    private GameStateResponse mapToGameStateResponse(Game game) {
        GameStateResponse response = new GameStateResponse();
        response.setGameId(game.getId());
        response.setStatus(game.getStatus());
        response.setPlayers(game.getPlayers());
        response.setCurrentPlayerIndex(game.getCurrentPlayerIndex());
        return response;
    }
}
