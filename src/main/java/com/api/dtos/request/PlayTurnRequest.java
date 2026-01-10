package com.api.dtos.request;

import com.common_game.decks.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PlayTurnRequest {
    private UUID playerId;
    private Position position;
}
