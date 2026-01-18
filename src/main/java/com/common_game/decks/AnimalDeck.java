package com.common_game.decks;

import com.common_game.cards.AnimalCard;
import com.enums.AnimalType;

import java.util.*;

import static com.common_game.factorys.AnimalDeckFactory.createAllCards;

public class AnimalDeck {
    private final Deque<AnimalCard> deck = new LinkedList<>();
    private final List<AnimalCard> availableCards = new ArrayList<>();
    private static final int MAX_AVAILABLE = 5;

    public AnimalDeck() {
        List<AnimalCard> cardsList = createAllCards();
        // Shuffle and fill the deck
        Collections.shuffle(cardsList);
        deck.addAll(cardsList);
        // Fill 5 first animal card
        refillAvailableCards();
    }

    private void refillAvailableCards() {
        while (availableCards.size() < MAX_AVAILABLE && !deck.isEmpty()) {
            availableCards.add(deck.removeFirst());
        }
    }

    /**
     * Pioche une carte du type demandé parmi les cartes visibles.
     * @param type Le type d'animal souhaité
     * @return La carte choisie
     * @throws IllegalArgumentException si aucune carte de ce type n'est disponible
     */
    public AnimalCard drawByType(AnimalType type) {
        for (int i = 0; i < availableCards.size(); i++) {
            AnimalCard card = availableCards.get(i);
            if (card.getType() == type) {
                availableCards.remove(i);
                refillAvailableCards();
                return card;
            }
        }
        throw new IllegalArgumentException("No card with the type" + type + " available");
    }

    /**
     * Return available cards (copy for safety)
     */
    public List<AnimalCard> getAvailableCards() {
        return new ArrayList<>(availableCards);
    }

    public List<AnimalCard> getAnimalCards() {
        return new ArrayList<>(deck);
    }

    public boolean isEmpty() {
        return deck.isEmpty() && availableCards.isEmpty();
    }
}
