package com.api.dtos.response;

import com.common_game.models.Player;
import com.enums.GameStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GameStateResponse {
    private UUID gameId;
    private GameStatus status;
    private List<Player> players;
    private UUID currentPlayerUUID;
    private Integer nbOfAnimalCardsLeft;
    private Integer nbOfTileLeft;
}
