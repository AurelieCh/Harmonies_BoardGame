package com.api.dtos.request;

import com.common_game.decks.Position;
import com.enums.TerrainType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TilesPlacementRequest {
    private Position position;
    private TerrainType terrainType;
}

