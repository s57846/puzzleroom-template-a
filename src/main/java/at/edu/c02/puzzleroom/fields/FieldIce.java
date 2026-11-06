package at.edu.c02.puzzleroom.fields;

import at.edu.c02.puzzleroom.Direction;
import at.edu.c02.puzzleroom.GameBoard;

/**
 * An ice field (`@`).
 * When the player enters it, they slide further in the same direction.
 * They stop on the first walkable field after the ice.
 * If a wall comes first, they stay on the last ice field.
 */
public class FieldIce extends BaseField {
    public FieldIce(GameBoard gameBoard, char name, int row, int col) {
        super(gameBoard, name, row, col);
    }

    public void initialize() {
    }

    public boolean enterField(Direction direction) {
        // First, the player stands on this ice field
        gameBoard.getPlayer().walkStep();
        setPlayerPositionToField();

        // Then the player slides on to the next field in the same direction
        Field nextField = switch (direction) {
            case Up -> gameBoard.getField(row - 1, col);
            case Down -> gameBoard.getField(row + 1, col);
            case Left -> gameBoard.getField(row, col - 1);
            case Right -> gameBoard.getField(row, col + 1);
        };
        // If the next field can't be entered (e.g. a wall), the player simply
        // stays on this ice field. The move itself was still successful.
        nextField.enterField(direction);
        return true;
    }

    public boolean leaveField(Direction direction) {
        return true;
    }
}
