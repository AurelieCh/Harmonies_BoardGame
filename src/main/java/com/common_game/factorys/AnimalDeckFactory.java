package com.common_game.factorys;

import com.common_game.cards.AnimalCard;
import com.common_game.rules.FixedPointsAnimalScoring;
import com.common_game.rules.TerrainBonusAnimalScoring;
import com.common_game.rules.TerrainRequirement;
import com.enums.AnimalType;
import com.enums.TerrainType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimalDeckFactory {

    public static List<AnimalCard> createAllCards() {
        List<AnimalCard> cards = new ArrayList<>();

        // 1. BAT
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.BAT,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.TREE, 1),
                List.of(new TerrainRequirement(TerrainType.TREE, 1, false))));

        // 2. FOX
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.FOX,
                1, 3,
                new TerrainBonusAnimalScoring(TerrainType.FIELD, 1),
                List.of(new TerrainRequirement(TerrainType.FIELD, 1, false))));

        // 3. HEDGEHOG
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.HEDGEHOG,
                2, 2,
                new FixedPointsAnimalScoring(),
                List.of()));

        // 4. SQUIRREL
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.SQUIRREL,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.TREE, 1),
                List.of(new TerrainRequirement(TerrainType.TREE, 1, false))));

        // 5. RABBIT
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.RABBIT,
                1, 3,
                new TerrainBonusAnimalScoring(TerrainType.FIELD, 1),
                List.of(new TerrainRequirement(TerrainType.FIELD, 1, false))));

        // 6. OWL
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.OWL,
                3, 1,
                new TerrainBonusAnimalScoring(TerrainType.TREE, 2),
                List.of(new TerrainRequirement(TerrainType.TREE, 1, false))));

        // 7. WOLF
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.WOLF,
                3, 2,
                new FixedPointsAnimalScoring(),
                List.of()));

        // 8. DEER
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.DEER,
                2, 3,
                new TerrainBonusAnimalScoring(TerrainType.TREE, 1),
                List.of(new TerrainRequirement(TerrainType.TREE, 1, false))));

        // 9. MOUSE
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.MOUSE,
                1, 2,
                new TerrainBonusAnimalScoring(TerrainType.FIELD, 1),
                List.of(new TerrainRequirement(TerrainType.FIELD, 1, false))));

        // 10. BEAR
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.BEAR,
                4, 2,
                new FixedPointsAnimalScoring(),
                List.of()));

        // 11. FROG
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.FROG,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 1),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 12. DUCK
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.DUCK,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 1),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 13. TURTLE
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.TURTLE,
                2, 3,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 1),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 14. BEAVER
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.BEAVER,
                3, 2,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 2),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 15. SWAN
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.SWAN,
                3, 1,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 2),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 16. FISH
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.FISH,
                1, 3,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 1),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 17. HORSE
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.HORSE,
                3, 2,
                new TerrainBonusAnimalScoring(TerrainType.FIELD, 1),
                List.of(new TerrainRequirement(TerrainType.FIELD, 1, false))));

        // 18. COW
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.COW,
                2, 3,
                new TerrainBonusAnimalScoring(TerrainType.FIELD, 1),
                List.of(new TerrainRequirement(TerrainType.FIELD, 1, false))));

        // 19. SHEEP
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.SHEEP,
                1, 3,
                new TerrainBonusAnimalScoring(TerrainType.FIELD, 1),
                List.of(new TerrainRequirement(TerrainType.FIELD, 1, false))));

        // 20. CHICKEN
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.CHICKEN,
                1, 2,
                new TerrainBonusAnimalScoring(TerrainType.BUILDING, 1),
                List.of(new TerrainRequirement(TerrainType.BUILDING, 1, false))));

        // 21. CAT
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.CAT,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.BUILDING, 1),
                List.of(new TerrainRequirement(TerrainType.BUILDING, 1, false))));

        // 22. DOG
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.DOG,
                2, 2,
                new FixedPointsAnimalScoring(),
                List.of()));

        // 23. RAVEN
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.RAVEN,
                3, 1,
                new TerrainBonusAnimalScoring(TerrainType.MOUNTAIN, 1),
                List.of(new TerrainRequirement(TerrainType.MOUNTAIN, 1, false))));

        // 24. EAGLE
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.EAGLE,
                4, 1,
                new TerrainBonusAnimalScoring(TerrainType.MOUNTAIN, 2),
                List.of(new TerrainRequirement(TerrainType.MOUNTAIN, 1, false))));

        // 25. GOAT
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.GOAT,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.MOUNTAIN, 1),
                List.of(new TerrainRequirement(TerrainType.MOUNTAIN, 1, false))));

        // 26. SHEEPDOG
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.SHEEPDOG,
                3, 2,
                new FixedPointsAnimalScoring(),
                List.of()));

        // 27. OTTER
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.OTTER,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 1),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 28. CRANE
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.CRANE,
                3, 1,
                new TerrainBonusAnimalScoring(TerrainType.WATER, 2),
                List.of(new TerrainRequirement(TerrainType.WATER, 1, false))));

        // 29. HAWK
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.HAWK,
                4, 1,
                new TerrainBonusAnimalScoring(TerrainType.MOUNTAIN, 2),
                List.of(new TerrainRequirement(TerrainType.MOUNTAIN, 1, false))));

        // 30. PIG
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.PIG,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.FIELD, 1),
                List.of(new TerrainRequirement(TerrainType.FIELD, 1, false))));

        // 31. TURKEY
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.TURKEY,
                2, 2,
                new TerrainBonusAnimalScoring(TerrainType.BUILDING, 1),
                List.of(new TerrainRequirement(TerrainType.BUILDING, 1, false))));

        // 32. RACCOON
        cards.add(new AnimalCard(UUID.randomUUID(),
                AnimalType.RACCOON,
                3, 1,
                new FixedPointsAnimalScoring(),
                List.of(
                        new TerrainRequirement(TerrainType.FIELD, 1, false),
                        new TerrainRequirement(TerrainType.WATER, 3, true)
                )));

        return cards;
    }
}
