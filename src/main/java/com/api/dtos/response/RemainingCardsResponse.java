package com.api.dtos.response;

import com.common_game.models.TerrainTile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemainingCardsResponse {
    private Integer nbRemaining;
    private List<TerrainTile> tiles;
}
