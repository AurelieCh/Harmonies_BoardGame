package com.api.dtos.response;

import com.enums.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrawnTileResponse {
    private TerrainType tileType;
}
