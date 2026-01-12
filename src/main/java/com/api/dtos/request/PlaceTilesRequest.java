package com.api.dtos.request;

import com.common_game.decks.Position;
import com.enums.TerrainType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PlaceTilesRequest {
    private Map<Position, TerrainType> tilePlacements; // {"x":0,"y":0": "FOREST"}
    private int slotNumber; // Number of the slot to refill
}

