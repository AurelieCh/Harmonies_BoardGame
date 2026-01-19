package com.common_game.factorys;

import com.common_game.cards.AnimalCard;
import com.common_game.decks.Position;
import com.common_game.rules.AnimalRequirement;
import com.common_game.rules.FixedPointsAnimalScoring;
import com.common_game.rules.TerrainBonusAnimalScoring;
import com.enums.AnimalType;
import com.enums.TerrainType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.enums.TerrainType.*;

public class AnimalDeckFactory {

    public static List<AnimalCard> createAllCards() {
        List<AnimalCard> cards = new ArrayList<>();

        // 1. BAT – forêt en hauteur
        cards.add(card(AnimalType.BAT, 2, 2,
                new TerrainBonusAnimalScoring(TREE, 1),
                req(
                        pos(0,0), stack(BROWN, TREE),
                        pos(1,-1), stack(TREE),
                        0
                )
        ));

        // 2. FOX – ligne de forêt
        cards.add(card(AnimalType.FOX, 1, 3,
                new TerrainBonusAnimalScoring(TREE, 1),
                req(
                        pos(0,0), stack(TREE),
                        pos(1,-1), stack(TREE),
                        pos(2,-2), stack(TREE),
                        1
                )
        ));

        // 3. HEDGEHOG – champs adjacents
        cards.add(card(AnimalType.HEDGEHOG, 2, 2,
                new FixedPointsAnimalScoring(),
                req(
                        pos(0,0), stack(FIELD),
                        pos(-1,0), stack(FIELD),
                        0
                )
        ));

        // 4. SQUIRREL – arbre central + arbres
        cards.add(card(AnimalType.SQUIRREL, 2, 2,
                new TerrainBonusAnimalScoring(TREE, 1),
                req(
                        pos(0,0), stack(BROWN, TREE),
                        pos(-1,1), stack(TREE),
                        pos(1,1), stack(TREE),
                        0
                )
        ));

        // 5. RABBIT – champs en ligne
        cards.add(card(AnimalType.RABBIT, 1, 3,
                new TerrainBonusAnimalScoring(FIELD, 1),
                req(
                        pos(0,0), stack(FIELD),
                        pos(1,0), stack(FIELD),
                        pos(-1,0), stack(FIELD),
                        0
                )
        ));

        // 6. OWL – arbre haut isolé
        cards.add(card(AnimalType.OWL, 3, 1,
                new TerrainBonusAnimalScoring(TREE, 2),
                req(
                        pos(0,0), stack(BROWN, BROWN, TREE),
                        0
                )
        ));

        // 7. WOLF – montagnes adjacentes
        cards.add(card(AnimalType.WOLF, 3, 2,
                new FixedPointsAnimalScoring(),
                req(
                        pos(0,0), stack(MOUNTAIN),
                        pos(1,0), stack(MOUNTAIN),
                        pos(-1,0), stack(MOUNTAIN),
                        0
                )
        ));

        // 8. DEER – clairière forestière
        cards.add(card(AnimalType.DEER, 2, 3,
                new TerrainBonusAnimalScoring(TREE, 1),
                req(
                        pos(0,0), stack(FIELD),
                        pos(-1,1), stack(TREE),
                        pos(1,1), stack(TREE),
                        0
                )
        ));

        // 9. MOUSE – arbre + champ
        cards.add(card(AnimalType.MOUSE, 1, 2,
                new TerrainBonusAnimalScoring(FIELD, 1),
                req(
                        pos(0,0), stack(BROWN, TREE),
                        pos(1,-1), stack(FIELD),
                        0
                )
        ));

        // 10. BEAR – montagnes autour
        cards.add(card(AnimalType.BEAR, 4, 2,
                new FixedPointsAnimalScoring(),
                req(
                        pos(0,0), stack(MOUNTAIN, MOUNTAIN),
                        pos(1,0), stack(MOUNTAIN),
                        pos(-1,0), stack(MOUNTAIN),
                        pos(0,1), stack(MOUNTAIN),
                        0
                )
        ));

        // 11. FROG – eaux adjacentes
        cards.add(card(AnimalType.FROG, 2, 2,
                new TerrainBonusAnimalScoring(WATER, 1),
                req(
                        pos(0,0), stack(WATER),
                        pos(1,-1), stack(WATER),
                        pos(-1,1), stack(WATER),
                        0
                )
        ));

        // 12. DUCK – étang
        cards.add(card(AnimalType.DUCK, 2, 2,
                new TerrainBonusAnimalScoring(WATER, 1),
                req(
                        pos(0,0), stack(WATER),
                        pos(1,0), stack(WATER),
                        0
                )
        ));

        // 13. TURTLE – eau calme
        cards.add(card(AnimalType.TURTLE, 2, 3,
                new TerrainBonusAnimalScoring(WATER, 1),
                req(
                        pos(0,0), stack(WATER),
                        pos(1,-1), stack(WATER),
                        pos(2,-2), stack(WATER),
                        1
                )
        ));

        // 14. BEAVER – barrage
        cards.add(card(AnimalType.BEAVER, 3, 2,
                new TerrainBonusAnimalScoring(WATER, 2),
                req(
                        pos(0,0), stack(WATER),
                        pos(1,0), stack(BROWN, TREE),
                        pos(-1,0), stack(BROWN, TREE),
                        0
                )
        ));

        // 15. SWAN – lac isolé
        cards.add(card(AnimalType.SWAN, 3, 1,
                new TerrainBonusAnimalScoring(WATER, 2),
                req(
                        pos(0,0), stack(WATER),
                        pos(1,-1), stack(WATER),
                        pos(-1,1), stack(WATER),
                        0
                )
        ));

        // 16. FISH – ligne d’eau
        cards.add(card(AnimalType.FISH, 1, 3,
                new TerrainBonusAnimalScoring(WATER, 1),
                req(
                        pos(0,0), stack(WATER),
                        pos(1,0), stack(WATER),
                        pos(2,0), stack(WATER),
                        1
                )
        ));

        // 17. HORSE – prairie
        cards.add(card(AnimalType.HORSE, 3, 2,
                new TerrainBonusAnimalScoring(FIELD, 1),
                req(
                        pos(0,0), stack(FIELD),
                        pos(1,-1), stack(FIELD),
                        pos(-1,1), stack(FIELD),
                        0
                )
        ));

        // 18. COW – champs groupés
        cards.add(card(AnimalType.COW, 2, 3,
                new TerrainBonusAnimalScoring(FIELD, 1),
                req(
                        pos(0,0), stack(FIELD),
                        pos(1,0), stack(FIELD),
                        pos(-1,0), stack(FIELD),
                        pos(0,1), stack(FIELD),
                        0
                )
        ));

        // 19. SHEEP – pâturage
        cards.add(card(AnimalType.SHEEP, 1, 3,
                new TerrainBonusAnimalScoring(FIELD, 1),
                req(
                        pos(0,0), stack(FIELD),
                        pos(1,0), stack(FIELD),
                        0
                )
        ));

        // 20. CHICKEN – ferme
        cards.add(card(AnimalType.CHICKEN, 1, 2,
                new TerrainBonusAnimalScoring(BUILDING, 1),
                req(
                        pos(0,0), stack(BUILDING),
                        pos(1,0), stack(FIELD),
                        0
                )
        ));

        // 21. CAT – village
        cards.add(card(AnimalType.CAT, 2, 2,
                new TerrainBonusAnimalScoring(BUILDING, 1),
                req(
                        pos(0,0), stack(BUILDING),
                        pos(1,0), stack(BUILDING),
                        0
                )
        ));

        // 22. DOG – maison + champ
        cards.add(card(AnimalType.DOG, 2, 2,
                new FixedPointsAnimalScoring(),
                req(
                        pos(0,0), stack(BUILDING),
                        pos(1,0), stack(FIELD),
                        0
                )
        ));

        // 23. RAVEN – montagne isolée
        cards.add(card(AnimalType.RAVEN, 3, 1,
                new TerrainBonusAnimalScoring(MOUNTAIN, 1),
                req(
                        pos(0,0), stack(MOUNTAIN),
                        pos(-1,1), stack(MOUNTAIN),
                        0
                )
        ));

        // 24. EAGLE – sommet
        cards.add(card(AnimalType.EAGLE, 4, 1,
                new TerrainBonusAnimalScoring(MOUNTAIN, 2),
                req(
                        pos(0,0), stack(MOUNTAIN, MOUNTAIN, MOUNTAIN),
                        0
                )
        ));

        // 25. GOAT – crête
        cards.add(card(AnimalType.GOAT, 2, 2,
                new TerrainBonusAnimalScoring(MOUNTAIN, 1),
                req(
                        pos(0,0), stack(MOUNTAIN),
                        pos(1,-1), stack(MOUNTAIN),
                        0
                )
        ));

        // 26. SHEEPDOG – ferme étendue
        cards.add(card(AnimalType.SHEEPDOG, 3, 2,
                new FixedPointsAnimalScoring(),
                req(
                        pos(0,0), stack(FIELD),
                        pos(1,0), stack(FIELD),
                        pos(-1,0), stack(BUILDING),
                        0
                )
        ));

        // 27. OTTER – rivière
        cards.add(card(AnimalType.OTTER, 2, 2,
                new TerrainBonusAnimalScoring(WATER, 1),
                req(
                        pos(0,0), stack(WATER),
                        pos(1,-1), stack(WATER),
                        pos(2,-2), stack(WATER),
                        1
                )
        ));

        // 28. CRANE – marais
        cards.add(card(AnimalType.CRANE, 3, 1,
                new TerrainBonusAnimalScoring(WATER, 2),
                req(
                        pos(0,0), stack(WATER),
                        pos(-1,1), stack(WATER),
                        pos(1,1), stack(WATER),
                        0
                )
        ));

        // 29. HAWK – montagne haute
        cards.add(card(AnimalType.HAWK, 4, 1,
                new TerrainBonusAnimalScoring(MOUNTAIN, 2),
                req(
                        pos(0,0), stack(MOUNTAIN, MOUNTAIN),
                        pos(1,0), stack(MOUNTAIN),
                        0
                )
        ));

        // 30. PIG – champs
        cards.add(card(AnimalType.PIG, 2, 2,
                new TerrainBonusAnimalScoring(FIELD, 1),
                req(
                        pos(0,0), stack(FIELD),
                        pos(1,-1), stack(FIELD),
                        0
                )
        ));

        // 31. TURKEY – ferme
        cards.add(card(AnimalType.TURKEY, 2, 2,
                new TerrainBonusAnimalScoring(BUILDING, 1),
                req(
                        pos(0,0), stack(BUILDING),
                        pos(1,-1), stack(FIELD),
                        0
                )
        ));

        // 32. RACCOON – bâtiment + eau
        cards.add(card(AnimalType.RACCOON, 3, 1,
                new FixedPointsAnimalScoring(),
                req(
                        pos(0,0), stack(BUILDING),
                        pos(1,-1), stack(WATER),
                        pos(0,-2), stack(WATER),
                        pos(-1,-1), stack(WATER),
                        0
                )
        ));

        return cards;
    }

    // ---------------- helpers ----------------

    private static AnimalCard card(AnimalType type, int points, int cubes,
                                   Object scoring,
                                   AnimalRequirement req) {
        return new AnimalCard(
                UUID.randomUUID(),
                type,
                points,
                cubes,
                scoring instanceof FixedPointsAnimalScoring
                        ? (FixedPointsAnimalScoring) scoring
                        : (TerrainBonusAnimalScoring) scoring,
                List.of(req)
        );
    }

    private static AnimalRequirement req(Object... data) {
        List<Position> pos = new ArrayList<>();
        List<List<TerrainType>> stacks = new ArrayList<>();
        int cubeAt = -1;

        for (int i = 0; i < data.length; i += 2) {
            if (data[i] instanceof Integer) {
                cubeAt = (int) data[i];
                break;
            }
            pos.add((Position) data[i]);
            if (data[i + 1] instanceof List<?> list &&
                    list.stream().allMatch(t -> t instanceof TerrainType)) {

                @SuppressWarnings("unchecked")
                List<TerrainType> stack = (List<TerrainType>) list;
                stacks.add(stack);
            }
        }
        return new AnimalRequirement(pos, stacks, cubeAt);
    }

    private static Position pos(int x, int y) {
        return new Position(x, y);
    }

    private static List<TerrainType> stack(TerrainType... t) {
        return List.of(t);
    }
}
