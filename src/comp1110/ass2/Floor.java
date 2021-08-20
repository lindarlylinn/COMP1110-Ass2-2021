package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;

import static comp1110.ass2.Tile.*;

/*******************************************************************************
 *                           Class: Floor                                      *
 *                  This class is contributed by Qm9nb25n                      *
 ******************************************************************************/
class Floor {
    Tile[] floor;

    private Floor() {
        this.floor = new Tile[7];
    }


    /**
     * This method is just for testing. Do NOT use for constructing Floor.
     *
     * @param floor Tiles on floor
     */
    Floor(Tile[] floor) {
        this.floor = floor;
    }


    /**
     * Initialize a floor.
     *
     * @return Initialized floor
     */
    static Floor initFloor() {
        return new Floor();
    }


    /**
     * Get tiles on floor.
     *
     * @return An array of array of tiles.
     * [first 7 tiles on floor, the rest of tiles on floor]
     * Format: first element in array: first 7 tiles on floor
     *         second element in array: the rest of tiles on floor
     */
    public Tile[] getFloor() {
        return floor;
    }

    /**
     * Get number of tiles on the floor.
     *
     * @return number of tiles on the floor
     */
    int getNumOfTilesOnFloor() {
        int counter = 0;

        for (int i = 0; i < 7; i++) {
            Tile cullTile = floor[i];
            if (cullTile != null) {
                counter ++;
            }
        }

        return counter;
    }


    /**
     * Check if the floor is filled with tiles
     *
     * @return True if the floor is filled with tiles, otherwise return false
     */
    boolean isFloorFull() {
        return getNumOfTilesOnFloor() == 7;
    }


    /**
     * Move a single tile to floor.
     *
     * @param tile Tile to be moved
     */
    private Tile addOneTileToFloor(Tile tile) {
        // If floor is full, return current floor.
        if (isFloorFull()) {
            return tile;
        // Else: if floor is not full:
        } else {
            // Get number of tiles on floor:
            int numOfTilesOnFloor = getNumOfTilesOnFloor();
            // Place to last position
            floor[numOfTilesOnFloor] = tile;
        }

        return null;
    }


    /**
     * Add first player token to floor. If the floor is full. Replace the last
     * tile with first player token.
     *
     * @return last tile if the floor is full.
     */
    private Tile addFirstPlayerTokenToFloor() {
        if (isFloorFull()) {
            Tile lastTile = floor[6];
            floor[6] = FirstPlayerToken;
            return lastTile;
        }
        else {
            addOneTileToFloor(FirstPlayerToken);
            return null;
        }
    }


    /**
     * Given a list of tiles, add them to floor. Return the redundant tiles.
     *
     * @param tiles A list of tiles to be placed into floor
     */
    ArrayList<Tile> addMultipleTilesToFloor(ArrayList<Tile> tiles) {
        ArrayList<Tile> redundantTiles = new ArrayList<>();

        for (Tile tile : tiles) {
            if (tile == FirstPlayerToken) {
                Tile redundantTile = addFirstPlayerTokenToFloor();
                if (redundantTile != null) {
                    redundantTiles.add(redundantTile);
                }
                tiles.remove(FirstPlayerToken);
                break;
            }
        }

        for (Tile tile : tiles) {
            Tile redundantTile = addOneTileToFloor(tile);
            if (redundantTile != null) {
                redundantTiles.add(redundantTile);
            }
        }

        return redundantTiles;
    }


    /**
     * Check if current floor contains first player token.
     *
     * @return If current floor contains first player token
     */
    boolean hasFirstPlayerTokenOnFloor() {
        for (Tile tile : floor) {
            if (tile == FirstPlayerToken) {
                return true;
            }
        }
        return false;
    }


    /**
     * Empty the floor.
     *
     * @return Tiles on floor
     */
    ArrayList<Tile> emptyFloor() {
        ArrayList<Tile> removedTiles = new ArrayList<>(Arrays.asList(floor).subList(0, getNumOfTilesOnFloor()));

        floor = new Tile[7];

        return removedTiles;
    }


    /**
     * Set the floor by string representation of floor.
     *
     * @param floorString String representation of mosaic
     * @return A list of tiles
     */
    static Floor parseFloorFromString(String floorString) {
        Floor floor = initFloor();

        if (floorString.length() > 7) {
            throw new IllegalArgumentException("Number of tiles on floor cannot greater than 7");
        }

        for (int i = 0; i < floorString.length(); i++) {
            Tile currTile = getTileFromLabel(floorString.charAt(i));
            floor.addOneTileToFloor(currTile);
        }

        return floor;
    }


    /**
     * Convert a floor to floor string.
     *
     * @return String representation of floor (ordered)
     */
    String toFloorString() {
        StringBuilder unorderedFloorSb = new StringBuilder();
        for (Tile tile : floor) {
            if (tile != null) {
                unorderedFloorSb.append(tile.getLabel());
            }
        }
        String unorderedFloorString = unorderedFloorSb.toString();

        // Sort the floor string alphabetically.
        char[] orderedFloorString = unorderedFloorString.toCharArray();
        Arrays.sort(orderedFloorString);

        return new String(orderedFloorString);
    }


    @Override
    public String toString() {
        return Arrays.toString(floor);
    }
}