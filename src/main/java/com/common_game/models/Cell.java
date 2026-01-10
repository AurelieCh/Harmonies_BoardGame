package com.common_game.models;

import com.common_game.decks.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Deque;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    private Position position;
    private Deque<TerrainTile> stack;

    public Cell(Position position) {
    }
}
