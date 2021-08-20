package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)


public class FillFactoriesWithTilesTest {
    @Test
    public void testfillFactoriesWithTiles() {
        List[] testFactory = ExampleFactories.testFactory;
        Tile[][][] resultTile = ExampleFactories.resultTile;
        for (int i = 0; i < testFactory.length; i++) {
            ArrayList<Tile> TileInBag = new ArrayList<>();
            // add all the element in list into ArrayList
            TileInBag.addAll(testFactory[i]);
            // initialize 2 players GameBoard
            GameBoard gameboard = GameBoard.initBasicGameBoards(2);

            gameboard.fillFactoriesWithTiles(TileInBag);
            Tile[][] out = gameboard.getFactories();
            // change address to actual value
            String[] outs = new String[out.length];
            for(int j =0;j<out.length;j++){ outs[j] = Arrays.toString(out[j]);}
            // change address to actual value
            String[] resultTiles = new String[resultTile[i].length];
            for(int r =0;r<resultTile[i].length;r++){ resultTiles[r] = Arrays.toString(resultTile[i][r]);}
            // check if expect equal actual
            assertEquals(Arrays.toString(resultTiles), Arrays.toString(outs), "The actual factory doesn't match the expected");
        }
    }
}
