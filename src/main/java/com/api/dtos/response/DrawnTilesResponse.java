package com.api.dtos.response;

import com.enums.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrawnTilesResponse {
    private List<TerrainType> tileTypes;
}
