package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class ParseFromFloorStringTest {
    /**
     * Check if the if two list of tiles is a permutation of each other.
     *
     * @param tiles0 First list of tiles
     * @param tiles1 Second list of tiles
     * @return True if they are permutations of each other
     * e.g. {Red, Green, Blue} vs {Green, Red, Blue}  -> true
     *      {Red, Green, Blue} vs {Red, Red, Blue}    -> false
     */
    private boolean checkPermutations(Tile[] tiles0, Tile[] tiles1) {
        if (tiles0.length != tiles1.length) {
            return false;
        }

        HashMap<Tile, Integer> tileCount = new HashMap<>();

        for (Tile tileInTiles0 : tiles0) {
            tileCount.merge(tileInTiles0, 1, Integer::sum);
        }

        for (Tile tileInTiles1 : tiles1) {
            if (tileCount.get(tileInTiles1) == null || tileCount.get(tileInTiles1) == 0) {
                return false;
            } else {
                tileCount.put(tileInTiles1, tileCount.get(tileInTiles1) - 1);
            }
        }

        return true;
    }

    @Test
    public void testParseFromFloorString() {
        Floor[] testFloors = ExampleFloors.testFloors;
        String[] testFloorStrings = ExampleFloors.testStrings;
        for (int i = 0 ; i < testFloors.length; i++) {
            String testFloorString = testFloorStrings[i];
            Floor parsedFloor = Floor.parseFloorFromString(testFloorString);
            Floor testFloor = testFloors[i];
            assertTrue(checkPermutations(parsedFloor.getFloor(), testFloor.getFloor()), parsedFloor + "\n" + testFloor);
        }
    }
}
