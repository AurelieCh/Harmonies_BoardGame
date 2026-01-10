package com.common_game.decks;

import com.common_game.cards.AnimalCard;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class AnimalDeck {

    private final Deque<AnimalCard> cards = new ArrayDeque<>();

    /**
     * Constructs a new animal deck with the given list of initial cards.
     * The cards are shuffled to randomize their order.
     *
     * @param initialCards The list of animal cards to initialize the deck.
     */
    public AnimalDeck(List<AnimalCard> initialCards) {
        var list = new java.util.ArrayList<>(initialCards);
        java.util.Collections.shuffle(list);
        list.forEach(cards::push);
    }

    /**
     * Draws the top animal card from the deck.
     * This follows a LIFO (Last-In-First-Out) order due to the use of a deque with push/pop operations.
     *
     * @return The drawn animal card.
     * @throws IllegalStateException If the deck is empty.
     */
    public AnimalCard draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Animal deck is empty");
        }
        return cards.pop();
    }

    /**
     * Checks if the animal deck is empty.
     *
     * @return {@code true} if the deck is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
