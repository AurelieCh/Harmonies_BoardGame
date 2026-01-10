package com.api.dtos.request;

import com.common_game.decks.Position;
import com.enums.TerrainType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlaceTileRequest {
    private UUID playerId;
    private Position position;
    private TerrainType tileType;
}
