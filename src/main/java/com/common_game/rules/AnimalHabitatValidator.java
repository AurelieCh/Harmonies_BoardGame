package com.common_game.rules;

import com.common_game.cards.AnimalCard;
import com.common_game.models.Board;
import com.common_game.decks.Position;
import com.common_game.decks.AnimalDeck;
import com.enums.AnimalType;

import java.util.List;

public class AnimalHabitatValidator {

    /**
     * Vérifie si la position est valide pour placer un cube animal
     * @param board Le plateau
     * @param position La position où placer l'animal
     * @param animalType Le type d'animal à placer
     * @param deck Le deck utilisé dans le jeu (important pour refléter les cartes visibles)
     * @return true si la règle est respectée
     */
    public static boolean isHabitatValid(Board board, Position position, AnimalType animalType, AnimalDeck deck) {
        // On regarde seulement les cartes disponibles
        List<AnimalCard> visibleCards = deck.getAvailableCards().stream()
                .filter(card -> card.getType() == animalType)
                .toList();

        if (visibleCards.isEmpty()) {
            return false; // aucune carte visible du type demandé
        }

        // Si au moins une carte permet de placer l'animal sur cette position
        return visibleCards.stream()
                .anyMatch(card -> card.getRule().isSatisfied(board, position));
    }
}
