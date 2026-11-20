package at.edu.c02.puzzleroom;

import at.edu.c02.puzzleroom.commands.CommandFastmove;
import at.edu.c02.puzzleroom.commands.CommandLoad;
import at.edu.c02.puzzleroom.commands.CommandMove;
import at.edu.c02.puzzleroom.exceptions.PuzzleRoomInvalidArgumentsException;
import at.edu.c02.puzzleroom.exceptions.PuzzleRoomInvalidMoveException;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CommandTest {
    @Test
    public void movePositiveTest() throws Exception {
        GameBoard gameBoard = new GameBoardImpl();
        new CommandLoad(new String[]{"src/test/resources/simple.maze"}).execute(gameBoard);
        Player player = gameBoard.getPlayer();

        new CommandMove(new String[]{"right"}).execute(gameBoard);

        // Player should now be at 1 step and at column 2 (because they moved right once)
        assertEquals(1, player.getStepCount());
        assertEquals(1, player.getRow());
        assertEquals(2, player.getCol());
    }

    @Test(expected = PuzzleRoomInvalidMoveException.class)
    public void movePositiveTestNegative() throws Exception {
        GameBoard gameBoard = new GameBoardImpl();
        new CommandLoad(new String[]{"src/test/resources/simple.maze"}).execute(gameBoard);

        // Moving left should throw an InvalidMoveException
        new CommandMove(new String[]{"left"}).execute(gameBoard);
    }

    @Test(expected = PuzzleRoomInvalidArgumentsException.class)
    public void movePositiveTestNegative2() throws Exception {
        GameBoard gameBoard = new GameBoardImpl();
        new CommandLoad(new String[]{"src/test/resources/simple.maze"}).execute(gameBoard);

        // This should throw a PuzzleRoomInvalidArgumentsException
        new CommandMove(new String[]{"invalid"}).execute(gameBoard);
    }

    // C · fastmove: `fastmove d r r u` reaches the finish and shows the room once
    @Test
    public void fastmoveReachesFinish() throws Exception {
        GameBoard gameBoard = new GameBoardImpl();
        new CommandLoad(new String[]{"test.maze"}).execute(gameBoard);
        Player player = gameBoard.getPlayer();

        // Capture the console output to count how often the room is shown
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        try {
            new CommandFastmove(new String[]{"d", "r", "r", "u"}).execute(gameBoard);
        } finally {
            System.setOut(originalOut);
        }

        assertTrue(gameBoard.isFinished());
        assertEquals(4, player.getStepCount());
        // Every `show` prints exactly one `*` (the player) -> the room was shown once
        assertEquals(1, output.toString().chars().filter(c -> c == '*').count());
    }

    // C · fastmove: `fastmove d d r` stops at the wall, the first move stays, `r` is skipped
    @Test
    public void fastmoveStopsAtBlockedMove() throws Exception {
        GameBoard gameBoard = new GameBoardImpl();
        new CommandLoad(new String[]{"test.maze"}).execute(gameBoard);
        Player player = gameBoard.getPlayer();

        try {
            new CommandFastmove(new String[]{"d", "d", "r"}).execute(gameBoard);
            fail("Expected PuzzleRoomInvalidMoveException");
        } catch (PuzzleRoomInvalidMoveException expected) {
            // the second `d` hits the wall
        }

        // Only the first move was done: player is directly below the start field
        assertEquals(1, player.getStepCount());
        assertEquals(2, player.getRow());
        assertEquals(1, player.getCol());
        assertFalse(gameBoard.isFinished());
    }
}
