package com.api.storages;

import com.common_game.models.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GameStorage {
    private final Map<UUID, Game> games = new HashMap<>();

    public void save(Game game) {
        games.put(game.getId(), game);
    }

    public Game findById(UUID gameId) {
        return games.get(gameId);
    }
}
