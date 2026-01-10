package com.api.manager;

import com.common_game.models.Game;
import com.common_game.models.Player;

public class TurnManager {
    /**
     * Retrieves the current player based on the game's state.
     * The current player is determined by the {@code currentPlayerIndex} in the provided game.
     *
     * @param game The game instance from which to retrieve the current player.
     * @return The player whose turn it is.
     */
    public static Player getCurrentPlayer(Game game) {
        return game.getPlayers().get(game.getCurrentPlayerIndex());
    }

    /**
     * Advances the game to the next player's turn.
     * The turn order follows the sequence of players in the game's player list.
     * If the current player is the last in the list, the turn moves to the first player (circular turn order).
     *
     * @param game The game instance for which to advance the turn.
     */
    public static void nextTurn(Game game) {
        game.setCurrentPlayerIndex((game.getCurrentPlayerIndex() + 1) % game.getPlayers().size());
    }
}
