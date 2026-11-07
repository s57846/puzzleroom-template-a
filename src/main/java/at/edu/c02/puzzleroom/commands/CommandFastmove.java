package at.edu.c02.puzzleroom.commands;

import at.edu.c02.puzzleroom.GameBoard;
import at.edu.c02.puzzleroom.Player;
import at.edu.c02.puzzleroom.exceptions.PuzzleRoomException;
import at.edu.c02.puzzleroom.exceptions.PuzzleRoomInvalidArgumentsException;
import at.edu.c02.puzzleroom.exceptions.PuzzleRoomInvalidMoveException;

/**
 * This command executes several moves one after another.
 * `u` = up, `d` = down, `l` = left, `r` = right
 * Example usage: `fastmove d r r u`
 */
public class CommandFastmove implements Command {
    private final String[] directions;

    public CommandFastmove(String[] arguments) throws PuzzleRoomException {
        if (arguments.length == 0) {
            throw new PuzzleRoomInvalidArgumentsException();
        }
        // Check all arguments first, so that no move happens with a typo in the input
        for (String direction : arguments) {
            if (!direction.equals("u") && !direction.equals("d")
                    && !direction.equals("l") && !direction.equals("r")) {
                throw new PuzzleRoomInvalidArgumentsException();
            }
        }
        directions = arguments;
    }

    public void execute(GameBoard gameBoard) throws PuzzleRoomException {
        Player player = gameBoard.getPlayer();
        if (player == null) {
            throw new PuzzleRoomInvalidMoveException();
        }

        for (String direction : directions) {
            boolean success = switch (direction) {
                case "u" -> player.moveUp();
                case "d" -> player.moveDown();
                case "l" -> player.moveLeft();
                case "r" -> player.moveRight();
                default -> throw new PuzzleRoomInvalidArgumentsException();
            };
            // Stop at the first blocked move. Earlier moves stay done.
            if (!success) {
                throw new PuzzleRoomInvalidMoveException();
            }
        }

        // Show the room exactly once after the last move
        CommandShow showCommand = new CommandShow();
        showCommand.execute(gameBoard);
    }
}
