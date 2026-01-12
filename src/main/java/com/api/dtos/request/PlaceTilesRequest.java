package com.api.dtos.request;

import com.common_game.decks.Position;
import com.enums.TerrainType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaceTilesRequest {
    private List<TerrainType> tiles;
    private List<Position> positions;
}

