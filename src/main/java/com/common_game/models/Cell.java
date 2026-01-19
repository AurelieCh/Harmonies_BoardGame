package com.common_game.models;

import com.common_game.decks.Position;
import com.enums.AnimalType;
import com.enums.ErrorCode;
import com.enums.TerrainType;
import com.exceptions.GameException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Stack;

@Getter
@Setter
@AllArgsConstructor
public class Cell {
    private final Position position;
    private final Stack<TerrainTile> stack = new Stack<>(); // Tiles pile (max 3)
    private AnimalType animalCube; // Animal cube (null if empty)

    public Cell(Position position) {
        this.position = position;
    }

    public void addTile(TerrainTile tile) {
        if (stack.size() >= 3) {
            throw new GameException(ErrorCode.CELL_MAX_HEIGHT_REACHED, "Max 3 tiles per cell");
        }
        stack.push(tile);
    }

    public TerrainTile getTopTile() {
        return stack.isEmpty() ? null : stack.peek();
    }

    public int getHeight() {
        return stack.size();
    }

    // Returns a copy to discard any problem modifying the real objects
    public Stack<TerrainTile> getStack() {
        Stack<TerrainTile> copy = new Stack<>();
        copy.addAll(stack);
        return copy;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean hasAnimalCube() {
        return animalCube != null;
    }

    public List<TerrainType> getStackSignature() {
        return stack.stream()
                .map(TerrainTile::getType)
                .toList();
    }

}
