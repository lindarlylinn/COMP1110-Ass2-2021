package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class IsValidStorageAndMosaicTest {
    @Test
    public void testIsValidEmptyStorageAndEmptyMosaic() {
        GameBoard gameBoard = GameBoard.parseFromGameState(ExampleGames.VALID_STATES[0]);
        assertTrue(gameBoard.playerBoards[0].isValidStorageAndMosaic());
    }
    @Test
    public void testIsValidStorageAndEmptyMosaic() {
        GameBoard gameBoard = GameBoard.parseFromGameState(ExampleGames.VALID_STATES[1]);
        assertTrue(gameBoard.playerBoards[0].isValidStorageAndMosaic());
    }
    @Test
    public void testIsValidStorageAndMosaic() {
        GameBoard gameBoard = GameBoard.parseFromGameState(ExampleGames.VALID_STATES[2]);
        assertTrue(gameBoard.playerBoards[0].isValidStorageAndMosaic());
    }
    @Test
    public void testIsInvalidStorageAndMosaic() {
        GameBoard gameBoard = GameBoard.parseFromGameState(ExampleGames.VALID_STATES[1]);
        gameBoard.playerBoards[0].getMosaic().tileToVariantMosaic(0,0,Tile.Purple);
        assertFalse(gameBoard.playerBoards[0].isValidStorageAndMosaic());
        gameBoard.playerBoards[0].getMosaic().mosaicGrids[0][0] = null;
        assertTrue(gameBoard.playerBoards[0].isValidStorageAndMosaic());
        gameBoard.playerBoards[0].getMosaic().tileToVariantMosaic(0,0,Tile.Green);
        assertTrue(gameBoard.playerBoards[0].isValidStorageAndMosaic());
    }
}
