package com.common_game.rules;

import com.common_game.cards.AnimalCard;
import com.common_game.models.Board;

public interface AnimalScoringRule {

    /**
     * Calcule le score de la carte animale sur le plateau donné.
     *
     * @param board Le plateau du joueur
     * @param card La carte animale à scorer
     * @return Le score obtenu
     */
    int calculateScore(Board board, AnimalCard card);
}
