package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class FillFactoryWithTilesTest {
    @Test
    public void testfillFactoryWithTiles() {
        // Call and rename the example in ExampleTileList
        List[] testFactory = ExampleTileList.testFactory;
        Tile[][] resultTile = ExampleTileList.resultTile;

        for (int i = 0; i < testFactory.length; i++) {
            ArrayList<Tile> TileInBag = new ArrayList<>();
            // add all the element in list into ArrayList
            TileInBag.addAll(testFactory[i]);
            // initialize 2 players GameBoard
            GameBoard gameboard = GameBoard.initBasicGameBoards(2);
            // put number of tiles equal or less than 4 into factory
            if (i<testFactory.length-1) {
                // fill the chosen tile into the first Factory (factory that in index 0)
                gameboard.fillFactoryWithTiles(TileInBag, 0);
                // out - Current status of the first factory
                Tile[] out = gameboard.getFactories()[0];
                // out value reach the expected value then pass otherwise message is released
                assertEquals(Arrays.toString(resultTile[i]), Arrays.toString(out), "The actual factory doesn't match the expected");
            }else{
                // when put number of tiles large than 4 into factory
                try{
                    // try to fill the chosen tile into the first Factory
                    gameboard.fillFactoryWithTiles(TileInBag, 0);
                // Verify if an exception is thrown
                }catch(Exception ex){
                    // if thrown then reach the expected value otherwise message is released
                    assertTrue(ex.getMessage().contains("The maximum number of tiles in a factory is 4, but got "
                            + TileInBag.size()), "The maximum number of tiles in a factory is 4, but got "
                            + TileInBag.size());
                }
            }
        }
    }
}
