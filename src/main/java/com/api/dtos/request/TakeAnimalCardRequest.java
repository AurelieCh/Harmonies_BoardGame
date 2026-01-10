package com.api.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TakeAnimalCardRequest {
    private UUID playerId;
    private UUID cardId;
}
