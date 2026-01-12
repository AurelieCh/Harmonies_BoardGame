package com.api.dtos.request;

import com.common_game.decks.Position;
import com.enums.AnimalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceAnimalCubeRequest {
    private AnimalType animalType;
    private Position position;
}
