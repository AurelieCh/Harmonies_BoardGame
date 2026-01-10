package com.common_game.models;

import com.common_game.cards.AnimalCard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class Player {
    private final UUID id;
    private final String name;
    private int score;
    private final List<AnimalCard> animalCards = new ArrayList<>(); // Cartes animales en possession

    /**
     * Méthods that adds a card to the player hand
     * @param card Card added to the player hand
     */
    public void addAnimalCard(AnimalCard card) {
        if (animalCards.size() >= 4) {
            throw new IllegalStateException("Maximum 4 animal cards allowed.");
        }
        animalCards.add(card);
    }

    /**
     * Method that adds the amont of points to the player's score
     * @param points Number of points to add
     */
    public void addPoints(int points) {
        this.score += points;
    }
}
