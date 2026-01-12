package com.api.storages;

import com.common_game.models.Game;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Component
public class GameStorage {
    private final Map<UUID, Game> games = new HashMap<>();

    public void save(Game game) {
        games.put(game.getId(), game);
    }

    public Optional<Game> findById(UUID gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    public List<Game> getGames() {
        return new ArrayList<>(games.values());
    }
}
