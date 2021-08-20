package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static comp1110.ass2.Tile.*;

/*******************************************************************************
 *                           Class: Storage                                    *
 *                 This class is contributed by Qm9nb25n                       *
 ******************************************************************************/
public class Storage {
    Tile[][] storageRows;

    private Storage() {
        this.storageRows = new Tile[][] {
                new Tile[1],   // First row
                new Tile[2],   // Second row
                new Tile[3],   // Third row
                new Tile[4],   // Fourth row
                new Tile[5]};  // Fifth row
    }

    /**
     * This constructor is just for testing. Do NOT use for constructing Storage.
     *
     * @param storageRows Tiles in rows in storage.
     */
    Storage(Tile[][] storageRows) {
        this.storageRows = storageRows;
    }


    /**
     * Initialize a storage.
     *
     * @return an empty Storage
     */
    static Storage initStorage() {
        return new Storage();
    }

    public Tile[][] getStorageRows() {
        return storageRows;
    }

    /**
     * Get a row by it's row index.
     *
     * @param rowIndex Index of a row (e.g. 0, 1, 2 ...)
     * @return A row in storage
     */
    public Tile[] getRowByRowIndex(int rowIndex) {
        return storageRows[rowIndex].clone();
    }


    /**
     * Get a row in reversed form in storageRows.
     * (mainly for arranging tiles at right)
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @return A list of tiles in a given row
     */
    Tile[] getReversedRowByIndex(int rowIndex) {
        // Create a copy of a row so that we don't change the original
        // StorageRows.
        Tile[] row = storageRows[rowIndex].clone();
        // Return a reversed row.
        Collections.reverse(Arrays.asList(row));
        return row;
    }


    /**
     * Get the occupied colour of a row.
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @return Occupied tile in the given row
     */
    Tile getOccupiedTileByRow(int rowIndex) {
        Tile[] row = storageRows[rowIndex];

        // Iterate through every tile in the row:
        for (Tile tile : row) {
            if (tile != null) {
                // Return the first not null tile as occupied tile.
                return tile;
            }
        }

        // If there is no tile occupied, return null.
        return null;
    }


    /**
     * Get the number of tiles in the given row.
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @return Number of same tiles in the given row
     */
    public int getNumOfTilesByRow(int rowIndex) {
        int counter = 0;
        // Get the row by index of a row
        Tile[] row = storageRows[rowIndex];

        // Iterate through the row:
        for (Tile tile : row) {
            // If the tile is not null:
            if (tile != null) {
                // Count it.
                counter ++;
            }
        }

        return counter;
    }


    /**
     * Check whether a tile can be placed in a row in Storage.
     *
     * @param tile Tile to be placed (e.g. Blue, Green ...)
     * @param rowIndex Index of a row (e.g. 0 - 4)
     * @return The row is placeable for a given tile
     */
    boolean isPlaceableRow(Tile tile, int rowIndex) {
        Tile occupiedTile = getOccupiedTileByRow(rowIndex);
        // If there is no element in the given row:
        if (occupiedTile == null) {
            // return true.
            return true;
        }

        int numOfTiles = getNumOfTilesByRow(rowIndex);
        /* If the current row is not full and the given tile matches with
         * occupied tile(s) in this row, return true, otherwise return false */
        return numOfTiles < rowIndex + 1 && occupiedTile.equals(tile);
    }


    /**
     * Get rows index that is complete.
     *
     * @return Rows are full of tiles.
     */
    ArrayList<Integer> getCompleteRows() {
        ArrayList<Integer> completeRows = new ArrayList<>();

        // Iterate the storage rows:
        for (int i = 0; i < 5; i++) {
            // If the a row is complete:
            if (getNumOfTilesByRow(i) == i + 1) {
                // Add it's index to placeableRows.
                completeRows.add(i);
            }
        }

        return completeRows;
    }


    /**
     * Add one tile to a given row in storage.
     *
     * @param tile Tile to be added (e.g. Blue, Green ...)
     * @param rowIndex Index of a row (from 0 - 4)
     * @return Redundant Tile if the given row is full
     */
    private Tile addOneTileToStorage(int rowIndex, Tile tile) {
        int numOfTilesInRow = getNumOfTilesByRow(rowIndex);

        // If there is a space for tile, place it.
        if (numOfTilesInRow < rowIndex + 1) {
            storageRows[rowIndex][numOfTilesInRow] = tile;
            return null;
        }

        // Else return the tile as redundant tile.
        return tile;
    }


    /**
     * Add multiple tiles to a given row in storage.
     *
     * @param rowIndex Index of row (from 0 - 4)
     * @param tile Tile to be added (e.g. Blue, Green ...)
     * @param noOfTiles Number of Tiles to be added
     */
    void addMultiTilesToStorage(int rowIndex, Tile tile, int noOfTiles) {

        int i = 0;
        // Add tiles to Storage.
        while (i < noOfTiles) {
            addOneTileToStorage(rowIndex, tile);
            i++;
        }
    }


    /**
     * Add multiple tiles to a given row in storage.
     *
     * @param rowIndex Index of row (from 0 - 4)
     * @param tiles Tiles to be added
     * @return Redundant Tiles if the given row is full
     */
    ArrayList<Tile> addMultiTilesToStorage(int rowIndex, ArrayList<Tile> tiles) {
        ArrayList<Tile> redundantTiles = new ArrayList<>();

        // Iterate through tiles:
        for (Tile tile : tiles) {
            // Add one tile to storage.
            Tile redundantTile = addOneTileToStorage(rowIndex, tile);
            // If current tile is redundant tile:
            if (redundantTile != null) {
                // Add it to redundantTiles.
                redundantTiles.add(redundantTile);
            }
        }

        return redundantTiles;
    }


    /**
     * Remove all tiles in a given row.
     *
     * @param rowIndex Index of the row which needs to remove tiles
     * @return Removed tiles on the row.
     */
    ArrayList<Tile> removeTilesByRow(int rowIndex) {
        // Get all the tiles in the row.
        ArrayList<Tile> removedTiles = new ArrayList<>(Arrays.asList(storageRows[rowIndex]));

        // Clear the row by row index.
        storageRows[rowIndex] = new Tile[rowIndex + 1];

        return removedTiles;
    }


    /**
     * From current storageRows to its string representation
     *
     * @return *Up to* 5 3-character strings represents the current Storage.
     * Each 3-character string is defined as follows:
     * 1st character is '0' to '4' - representing the row - each row number must only appear once.
     * 2nd character is 'a' to 'e' - representing the tile colour.
     * 3rd character is '0' to '5' - representing the number of tiles stored in that row.
     * Each 3-character string is ordered by row number.
     */
    String toStorageString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            // Get number of tiles in one row, if it's not empty:
            int numOfTile = getNumOfTilesByRow(i);
            if (numOfTile != 0) {
                Tile tile = getOccupiedTileByRow(i);
                // Add row index to string
                sb.append(i);
                // Label of Tile to string
                sb.append(tile.getLabel());
                // Number of tiles to string
                sb.append(numOfTile);
            }
        }

        return sb.toString();
    }


    /**
     * Given a string representation of storage, covert it into Storage
     *
     * @param storageStr String representation of storage
     * @return A storage interpreted form String
     */
    static Storage parseFromStorageString(String storageStr) {
        Storage storage = initStorage();

        for (int i = 0; i < storageStr.length(); i += 3) {
            // Get index of a row from string
            int rowIndex = Character.getNumericValue(storageStr.charAt(i));
            // Get tile from string
            Tile tile = getTileFromLabel(storageStr.charAt(i+1));
            // Get number of tiles from string
            int noOfTile = Character.getNumericValue(storageStr.charAt(i+2));

            if (noOfTile > rowIndex + 1) {
                throw new IllegalArgumentException("Number of tiles to be placed is row "
                        + rowIndex
                        + " exceeds it's capacity");
            }

            if (storage.getOccupiedTileByRow(rowIndex) != null) {
                throw new IllegalArgumentException("The row is placed.");
            }

            // Add tiles to storage
            storage.addMultiTilesToStorage(rowIndex, tile, noOfTile);
        }

        return storage;
    }


    /**
     * Return an array of strings which represent each rows in Mosaic.
     *
     * @return An array of strings which represent each rows
     */
    String[] toRowsString() {
        String[] rows = new String[5];

        int maxRowStrLength = 0;
        // Get longest string representation of a row for alignment
        for (int i = 0; i < 5; i++) {
            Tile[] row = this.getReversedRowByIndex(i);
            maxRowStrLength = Math.max(maxRowStrLength, Arrays.toString(row).length());
        }

        for (int i = 0; i < 5; i++) {
            StringBuilder sb = new StringBuilder();
            Tile[] row = this.getReversedRowByIndex(i);
            // Make the output align to right
            int numOfSpaces = maxRowStrLength - Arrays.toString(row).length();
            // Add spaces
            sb.append(" ".repeat(Math.max(0, numOfSpaces)));
            sb.append(Arrays.toString(row));
            rows[i] = sb.toString();
        }

        return rows;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int maxRowStrLength = 0;
        // Get longest string representation of a row for alignment
        for (int i = 0; i < 5; i++) {
            Tile[] row = this.getReversedRowByIndex(i);
            maxRowStrLength = Math.max(maxRowStrLength, Arrays.toString(row).length());
        }


        for (int i = 0; i < 5; i++) {
            Tile[] row = this.getReversedRowByIndex(i);
            // Make the output align to right
            int numOfSpaces = maxRowStrLength - Arrays.toString(row).length();
            // Add spaces
            sb.append(" ".repeat(Math.max(0, numOfSpaces)));
            sb.append(Arrays.toString(row));
            sb.append("\n");
        }

        return sb.toString();
    }
}