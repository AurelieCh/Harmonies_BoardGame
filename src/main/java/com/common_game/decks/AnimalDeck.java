package com.common_game.decks;

import com.common_game.cards.AnimalCard;
import com.common_game.rules.HabitatRuleImpl;
import com.enums.AnimalType;
import com.enums.TerrainType;

import java.util.*;

public class AnimalDeck {

    private final Deque<AnimalCard> deck = new LinkedList<>();
    private final List<AnimalCard> availableCards = new ArrayList<>();
    private static final int MAX_AVAILABLE = 5;

    public AnimalDeck() {
        List<AnimalCard> cardsList = this.initCards();
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

    private List<AnimalCard> initCards() {
        List<AnimalCard> cardsList = new ArrayList<>();

        // =====================================================
        // 32 cartes animales uniques avec patterns plausibles
        // =====================================================

        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.DEER, 4,
                new HabitatRuleImpl(List.of(TerrainType.FOREST), 2),
                List.of(TerrainType.FOREST), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.FOX, 3,
                new HabitatRuleImpl(List.of(TerrainType.FOREST, TerrainType.MOUNTAIN), 2),
                List.of(TerrainType.FOREST, TerrainType.MOUNTAIN), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.BIRD, 2,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 1),
                List.of(TerrainType.WATER), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.FISH, 2,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 1),
                List.of(TerrainType.WATER), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.BAT, 3,
                new HabitatRuleImpl(List.of(TerrainType.FOREST, TerrainType.MOUNTAIN), 1),
                List.of(TerrainType.FOREST, TerrainType.MOUNTAIN), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.HEDGEHOG, 2,
                new HabitatRuleImpl(List.of(TerrainType.FIELD, TerrainType.FOREST), 2),
                List.of(TerrainType.FIELD, TerrainType.FOREST), 1, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.BEAVER, 3,
                new HabitatRuleImpl(List.of(TerrainType.WATER, TerrainType.FOREST), 2),
                List.of(TerrainType.WATER, TerrainType.FOREST), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.OTTER, 3,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 2),
                List.of(TerrainType.WATER), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.BUTTERFLY, 1,
                new HabitatRuleImpl(List.of(TerrainType.FIELD), 1),
                List.of(TerrainType.FIELD), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.RABBIT, 2,
                new HabitatRuleImpl(List.of(TerrainType.FIELD), 1),
                List.of(TerrainType.FIELD), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.SNAIL, 2,
                new HabitatRuleImpl(List.of(TerrainType.FIELD), 1),
                List.of(TerrainType.FIELD), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.FROG, 3,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 2),
                List.of(TerrainType.WATER), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.SNAKE, 3,
                new HabitatRuleImpl(List.of(TerrainType.FIELD, TerrainType.FOREST), 2),
                List.of(TerrainType.FIELD, TerrainType.FOREST), 1, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.SQUIRREL, 2,
                new HabitatRuleImpl(List.of(TerrainType.FOREST), 1),
                List.of(TerrainType.FOREST), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.HAWK, 3,
                new HabitatRuleImpl(List.of(TerrainType.MOUNTAIN), 2),
                List.of(TerrainType.MOUNTAIN), 1, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.OWL, 2,
                new HabitatRuleImpl(List.of(TerrainType.FOREST), 1),
                List.of(TerrainType.FOREST), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.CRAB, 2,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 1),
                List.of(TerrainType.WATER), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.TURTLE, 3,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 2),
                List.of(TerrainType.WATER), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.SALAMANDER, 3,
                new HabitatRuleImpl(List.of(TerrainType.FOREST, TerrainType.WATER), 2),
                List.of(TerrainType.FOREST, TerrainType.WATER), 1, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.MOLE, 2,
                new HabitatRuleImpl(List.of(TerrainType.FIELD), 1),
                List.of(TerrainType.FIELD), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.DRAGONFLY, 2,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 1),
                List.of(TerrainType.WATER), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.LADYBUG, 1,
                new HabitatRuleImpl(List.of(TerrainType.FIELD), 1),
                List.of(TerrainType.FIELD), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.ANT, 1,
                new HabitatRuleImpl(List.of(TerrainType.FIELD, TerrainType.FOREST), 1),
                List.of(TerrainType.FIELD, TerrainType.FOREST), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.BEAVER2, 3,
                new HabitatRuleImpl(List.of(TerrainType.WATER, TerrainType.MOUNTAIN), 2),
                List.of(TerrainType.WATER, TerrainType.MOUNTAIN), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.OTTER2, 3,
                new HabitatRuleImpl(List.of(TerrainType.FOREST, TerrainType.WATER), 2),
                List.of(TerrainType.FOREST, TerrainType.WATER), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.BUTTERFLY2, 2,
                new HabitatRuleImpl(List.of(TerrainType.FIELD), 1),
                List.of(TerrainType.FIELD), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.HAWK2, 3,
                new HabitatRuleImpl(List.of(TerrainType.MOUNTAIN), 2),
                List.of(TerrainType.MOUNTAIN), 1, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.OWL2, 2,
                new HabitatRuleImpl(List.of(TerrainType.FOREST), 1),
                List.of(TerrainType.FOREST), 1, 1));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.SALAMANDER2, 3,
                new HabitatRuleImpl(List.of(TerrainType.WATER, TerrainType.FIELD), 2),
                List.of(TerrainType.WATER, TerrainType.FIELD), 1, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.SALMON, 3,
                new HabitatRuleImpl(List.of(TerrainType.WATER), 2),
                List.of(TerrainType.WATER), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.EAGLE, 4,
                new HabitatRuleImpl(List.of(TerrainType.MOUNTAIN), 2),
                List.of(TerrainType.MOUNTAIN), 2, 2));
        cardsList.add(new AnimalCard(UUID.randomUUID(), AnimalType.RACCOON, 2,
                new HabitatRuleImpl(List.of(TerrainType.FOREST, TerrainType.FIELD), 1),
                List.of(TerrainType.FOREST, TerrainType.FIELD), 1, 1));


        return cardsList;
    }

    public boolean isEmpty() {
        return deck.isEmpty() && availableCards.isEmpty();
    }
}
