package com.common_game.decks;

import com.common_game.cards.AnimalCard;
import com.common_game.models.TerrainTile;
import com.common_game.rules.BirdRule;
import com.common_game.rules.DeerRule;
import com.common_game.rules.FishRule;
import com.common_game.rules.FoxRule;
import com.enums.AnimalType;
import com.enums.TerrainType;

import java.util.*;

public class AnimalDeck {

    private final Deque<AnimalCard> cards = new LinkedList<>();

    /**
     * Constructs a new animal deck with the given list of initial cards.
     * The cards are shuffled to randomize their order.
     */
    public AnimalDeck() {
        List<AnimalCard> cardsList = new ArrayList<>();
        // Ajoute toutes les cartes animales
        cardsList.addAll(createDeerCards());
        cardsList.addAll(createFoxCards());
        cardsList.addAll(createBirdCards());
        cardsList.addAll(createFishCards());
        // Mélange les cartes
        Collections.shuffle(cardsList);
        this.cards.addAll(cardsList);
    }

    private List<AnimalCard> createDeerCards() {
        return List.of(
                new AnimalCard(UUID.randomUUID(), AnimalType.DEER, 4, new DeerRule(), List.of(TerrainType.FOREST), 2, 2),
                new AnimalCard(UUID.randomUUID(), AnimalType.DEER, 3, new DeerRule(), List.of(TerrainType.FOREST), 1, 2)
        );
    }

    private List<AnimalCard> createFoxCards() {
        return List.of(
                new AnimalCard(UUID.randomUUID(), AnimalType.FOX, 3, new FoxRule(), List.of(TerrainType.MOUNTAIN, TerrainType.FOREST), 2, 1),
                new AnimalCard(UUID.randomUUID(), AnimalType.FOX, 2, new FoxRule(), List.of(TerrainType.MOUNTAIN, TerrainType.FOREST), 2, 1)
        );
    }

    private List<AnimalCard> createBirdCards() {
        return List.of(
                new AnimalCard(UUID.randomUUID(), AnimalType.BIRD, 2, new BirdRule(), List.of(TerrainType.WATER), 1, 1),
                new AnimalCard(UUID.randomUUID(), AnimalType.BIRD, 3, new BirdRule(), List.of(TerrainType.WATER, TerrainType.FOREST), 1, 1)
        );
    }

    private List<AnimalCard> createFishCards() {
        return List.of(
                new AnimalCard(UUID.randomUUID(), AnimalType.FISH, 2, new FishRule(), List.of(TerrainType.WATER), 1, 1),
                new AnimalCard(UUID.randomUUID(), AnimalType.FISH, 3, new FishRule(), List.of(TerrainType.WATER), 1, 2)
        );
    }

    // Retourne une copie pour éviter les modifications externes
    public List<AnimalCard> getCards() {
        return new ArrayList<>(cards);
    }

    public AnimalCard draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Animal deck is empty");
        }
        return cards.removeFirst();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
