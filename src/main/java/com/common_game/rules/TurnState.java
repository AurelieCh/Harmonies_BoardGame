package com.common_game.rules;

import com.enums.ErrorCode;
import com.exceptions.GameException;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents what a player is allowed to do during their turn.
 * Each action can be performed at most once.
 */
@Getter
@Setter
public class TurnState {

    // Mandatory actions
    private boolean tilesDrawn = false;
    private boolean tilesPlaced = false;

    // Optional actions
    private boolean animalCubePlaced = false;
    private boolean animalCardDrawn = false;

    /* =========================
       Reinit for next player
       ========================= */

    /**
     * Réinitialise l'état du tour pour le prochain joueur.
     */
    public void reinit() {
        this.tilesDrawn = false;
        this.tilesPlaced = false;
        this.animalCubePlaced = false;
        this.animalCardDrawn = false;
    }


    /* =========================
       Mandatory actions
       ========================= */

    public void drawTiles() {
        if (tilesDrawn) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "Tiles have already been drawn this turn"
            );
        }
        tilesDrawn = true;
    }

    public void placeTiles() {
        if (!tilesDrawn) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "You must draw tiles before placing them"
            );
        }
        if (tilesPlaced) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "Tiles have already been placed this turn"
            );
        }
        tilesPlaced = true;
    }

    /* =========================
       Optional actions
       ========================= */

    public void drawAnimalCard() {
        if (!tilesPlaced) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "You must place tiles before drawing an animal card"
            );
        }
        if (animalCardDrawn) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "Animal card has already been drawn this turn"
            );
        }
        animalCardDrawn = true;
    }

    public void placeAnimalCube() {
        if (!tilesPlaced) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "You must place tiles before placing an animal cube"
            );
        }
        if (animalCubePlaced) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "Animal cube has already been placed this turn"
            );
        }
        animalCubePlaced = true;
    }

    /* =========================
       Turn ending
       ========================= */

    public boolean canEndTurn() {
        return tilesDrawn && tilesPlaced;
    }

    public void assertCanEndTurn() {
        if (!canEndTurn()) {
            throw new GameException(
                    ErrorCode.INVALID_MOVE,
                    "You must draw and place tiles before ending your turn"
            );
        }
    }

}
