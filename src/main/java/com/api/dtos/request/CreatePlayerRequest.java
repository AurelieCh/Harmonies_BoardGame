package com.api.dtos.request;

import com.enums.WaterFaceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlayerRequest {
    private String name;
    private WaterFaceType waterFaceType;  // (RIVER ou ISLANDS)
}

