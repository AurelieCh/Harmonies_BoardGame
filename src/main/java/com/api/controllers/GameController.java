package com.api.controllers;

import com.api.dtos.request.PlayTurnRequest;
import com.api.dtos.request.TakeAnimalCardRequest;
import com.api.dtos.response.GameStateResponse;
import com.api.storages.GameStorage;
import com.common_game.models.Game;
import com.common_game.models.Player;
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

    @PostMapping("/{gameId}/play-turn")
    public ResponseEntity<GameStateResponse> playTurn(
            @PathVariable UUID gameId,
            @RequestBody PlayTurnRequest request) {
        Game game = gameStorage.findById(gameId);
        game.playTurn(request.getPlayerId(), request.getPosition());
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
