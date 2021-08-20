package comp1110.ass2;

import java.util.ArrayList;
import java.util.Arrays;

import static comp1110.ass2.Tile.*;

/*******************************************************************************
 *                           Class: Mosaic                                     *
 *                 This class is contributed by Qm9nb25n                       *
 ******************************************************************************/
public class Mosaic {
    Tile[][] mosaicGrids;
    private Tile[][] gridColour;


    private Mosaic(boolean isBasic) {
        // Initialize mosaic grids
        mosaicGrids = new Tile[5][5];
        // Initialize corresponding place for each colour in basic mosaic
        if (isBasic) {
            gridColour = new Tile[][]{{Blue, Green, Orange, Purple, Red},
                                      {Red, Blue, Green, Orange, Purple},
                                      {Purple, Red, Blue, Green, Orange},
                                      {Orange, Purple, Red, Blue, Green},
                                      {Green, Orange, Purple, Red, Blue}};
        } else {
            // Initialize empty spaces for variant mosaic
            gridColour = new Tile[5][5];
        }
    }


    /**
     * This constructor is just for testing. Do NOT use for construct Mosaic.
     *
     * @param isBasic Initialize a basic mosaic?
     * @param mosaicGrids Tiles in mosaic
     */
    Mosaic(boolean isBasic, Tile[][] mosaicGrids) {
        if (isBasic) {
            gridColour = new Tile[][]{{Blue, Green, Orange, Purple, Red},
                    {Red, Blue, Green, Orange, Purple},
                    {Purple, Red, Blue, Green, Orange},
                    {Orange, Purple, Red, Blue, Green},
                    {Green, Orange, Purple, Red, Blue}};
        } else {
            // Initialize empty spaces for variant mosaic
            gridColour = new Tile[5][5];
        }

        this.mosaicGrids = mosaicGrids;
    }


    /**
     * Initialize a basic mosaic
     *
     * @return A Basic mosaic
     */
    static Mosaic initBasicMosaic() {
        return new Mosaic(true);
    }


    /**
     * Initialize a variant mosaic with no pattern.
     *
     * @return A Variant Mosaic
     */
    static Mosaic initVariantMosaic() {
        return new Mosaic(false);
    }


    /**
     * Check if a mosaic is basic mosaic.
     *
     * @return True if current mosaic is basic mosaic
     */
    boolean isBasicMosaic() {
        return !Arrays.deepEquals(gridColour, new Tile[5][5]);
    }


    /**
     * Convert a basic mosaic to variant mosaic.
     */
    void convertToVariantMosaic() {
        gridColour = new Tile[5][5];
    }


    /**
     * Get a column by a given index in Mosaic.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param colIndex Index of a column (from 0 - 4)
     * @return A list of columns in a given column
     */
    Tile[] getColumnByIndex(int colIndex) {
        Tile[] column = new Tile[5];

        for (int i = 0; i < 5; i++) {
            column[i] = mosaicGrids[i][colIndex];
        }

        return column;
    }


    /**
     * Obtain a tile by its row index and column index
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param rowIndex Row index of a tile (from 0 - 4)
     * @param colIndex Column index of a tile (from 0 - 4)
     * @return The tile in row index and column index
     */
    public Tile getTileByRowAndCol(int rowIndex, int colIndex) {
        return mosaicGrids[rowIndex][colIndex];
    }


    /**
     * Get the occupied tiles in a row.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @return An arrayList of occupied tiles
     */
    ArrayList<Tile> getOccupiedTileByRow(int rowIndex) {
        ArrayList<Tile> occupiedTiles = new ArrayList<>();
        Tile[] row = mosaicGrids[rowIndex];

        for (int i = 0; i < 5; i++) {
            Tile currTile = row[i];
            // Check if the current place is empty;
            if (currTile != null) {
                occupiedTiles.add(currTile);
            }
        }

        return occupiedTiles;
    }


    /**
     * Get the occupied tiles in a column.
     * <p>
     * Usage scope: Variant Mosaic
     *
     * @param colIndex Index of a column (from 0 - 4)
     * @return An arrayList of occupied tiles
     */
    ArrayList<Tile> getOccupiedTileByCol(int colIndex) {
        ArrayList<Tile> occupiedTiles = new ArrayList<>();
        Tile[] col = getColumnByIndex(colIndex);

        for (int i = 0; i < 5; i++) {
            Tile currTile = col[i];
            // Check if the current place is empty;
            if (currTile != null) {
                occupiedTiles.add(currTile);
            }
        }

        return occupiedTiles;
    }


    /**
     * Get the number of tiles in the given row.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @return Number of tiles in the given row
     */
    int getNumOfTilesByRow(int rowIndex) {
        int counter = 0;
        // Get the row by index of a row
        Tile[] row = mosaicGrids[rowIndex];

        // Start counting
        for (Tile tile : row) {
            if (tile != null) {
                counter++;
            }
        }

        return counter;
    }


    /**
     * Get the number of tiles in the given column.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param colIndex Index of a index (from 0 - 4)
     * @return Number of tiles in the given column
     */
    int getNumOfTilesByCol(int colIndex) {
        int counter = 0;
        // Get the row by index of a row
        Tile[] column = getColumnByIndex(colIndex);

        // Start counting
        for (Tile tile : column) {
            if (tile != null) {
                counter++;
            }
        }

        return counter;
    }


    /**
     * Check whether a tile can be placed in a row in Mosaic.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param tile     Tile to be placed (e.g. Blue, Green ...)
     * @param rowIndex Index of a row (e.g. 0 - 4)
     * @return The row is placeable for a given tile
     */
    boolean isPlaceableRow(Tile tile, int rowIndex) {
        ArrayList<Tile> occupiedTile = getOccupiedTileByRow(rowIndex);

        // Iterate the given row, check if we have duplicates:
        for (Tile tileInGrid : occupiedTile) {
            // If we do have duplicates, return false
            if (tileInGrid.equals(tile)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Check whether a tile can be placed in a column in Mosaic.
     * <p>
     * Usage scope: Basic Mosaic
     *
     * @param tile     Tile to be placed (e.g. Blue, Green ...)
     * @param colIndex Index of a column (e.g. 0 - 4)
     * @return The column is placeable for a given tile
     */
    boolean isPlaceableCol(Tile tile, int colIndex) {
        ArrayList<Tile> occupiedTile = getOccupiedTileByCol(colIndex);

        // Iterate the given row, check if we have duplicates:
        for (Tile tileInGrid : occupiedTile) {
            // If we do have duplicates, return false
            if (tileInGrid.equals(tile)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Check whether a tile can be placed in a column in Mosaic (strict).
     * Added pending for mosaic cell.
     * <p>
     * Usage scope: Variant Mosaic
     *
     * @param tile     Tile to be placed (e.g. Blue, Green ...)
     * @param colIndex Index of a column (e.g. 0 - 4)
     * @return The column is placeable for a given tile
     */
    boolean isPlaceableCol(Tile tile, int rowIndex, int colIndex) {
        ArrayList<Tile> occupiedTile = getOccupiedTileByCol(colIndex);

        if (mosaicGrids[rowIndex][colIndex] != null) {
            return false;
        }

        // Iterate the given row, check if we have duplicates:
        for (Tile tileInGrid : occupiedTile) {
            // If we do have duplicates, return false
            if (tileInGrid.equals(tile)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Give a tile, get all the placeable columns in Mosaic.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param tile Tile to be placed (e.g. Blue, Green ...)
     * @return A list of integer that represent the index of columns
     * (e.g. {0,1,2,3,4}, {2, 3}, {})
     */
    ArrayList<Integer> getPlaceableCols(Tile tile, int rowIndex) {
        ArrayList<Integer> placeableCols = new ArrayList<>();

        for (int colIndex = 0; colIndex < 5; colIndex++) {

            if (isBasicMosaic()) {
                if (gridColour[rowIndex][colIndex] == tile) {
                    placeableCols.add(colIndex);
                }
            } else {
                if (isPlaceableCol(tile, rowIndex, colIndex)) {
                    placeableCols.add(colIndex);
                }
            }
        }

        return placeableCols;
    }


    /**
     * Get the column index of a tile in a specific row in mosaic.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @param tile     Tile to be added
     * @return Column index of the corresponding coloured space
     */
    int getTileColByRow(int rowIndex, Tile tile) {
        Tile[] row = gridColour[rowIndex];

        for (int i = 0; i < 5; i++) {
            if (row[i].equals(tile)) {
                return i;
            }
        }

        // Return -1 if there is no such tile
        return -1;
    }


    /**
     * Add a tile to mosaic grid
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param row  row index of a tile (from 0 - 4)
     * @param col  column index of a tile (from 0 - 4)
     * @param tile tile to be added to mosaic
     */
    private void tileToMosaic(int row, int col, Tile tile) {
        mosaicGrids[row][col] = tile;
    }


    /**
     * Tile a tile to basic mosaic in a given row.
     * <p>
     * Usage scope: Basic Mosaic
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @param colIndex Index of a column (from 0 - 4)
     * @param tile     The tile to be tiled
     */
    void tileToBasicMosaic(int rowIndex, int colIndex, Tile tile) {
        if (isPlaceableRow(tile, rowIndex)) {
            // Get the column index of current tile.
            int tileColIndex = getTileColByRow(rowIndex, tile);
            /* If place of the colour space is not the place where the tile is
               placed, throw error. */
            if (tileColIndex != colIndex) {
                throw new IllegalArgumentException("Tile in col " + colIndex + " is not valid.");
            } else {
                // Tile the tile based on it's position.
                tileToMosaic(rowIndex, colIndex, tile);
            }
        } else {
            throw new IllegalArgumentException("Tile in row " + rowIndex + " is not valid.");
        }
    }


    /**
     * Tile a tile to variant mosaic on a given row, if the tile place is illegal,
     * return the tile.
     * <p>
     * Usage scope: Variant Mosaic
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @param colIndex Index of a column (from 0 - 4)
     */
    void tileToVariantMosaic(int rowIndex, int colIndex, Tile tile) {
        if (isPlaceableRow(tile, rowIndex) && isPlaceableCol(tile, colIndex)) {
            tileToMosaic(rowIndex, colIndex, tile);
        } else {
            throw new IllegalArgumentException("Tile in grid (" + rowIndex + "," + colIndex + ") is not valid.");
        }
    }


    /**
     * Calculate number of linked tiles in a list of tiles
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param indexAtList The index of the tile in a list of tiles
     * @param tileList    A list of tiles
     * @return Number of linked tiles in a list
     */
    static int getNumOfLinkedTiles(int indexAtList, Tile[] tileList) {
        // If there is no element at a given list:
        if (tileList[indexAtList] == null) {
            // Return 0.
            return 0;
        }

        // Initialize a counter for counting number of linked tiles.
        int counter = 1;
        // Count the number of linked tiles after a given position.
        for (int i = indexAtList; i < 4; i++) {
            // If there is no tile at the next position:
            if (tileList[i + 1] == null) {
                // Stop counting.
                break;
                // Otherwise:
            } else {
                // Increase the counter by 1
                counter++;
            }
        }

        // Count the number of linked tiles before a given position.
        for (int i = indexAtList; i > 0; i--) {
            // If there is no tile at the previous position:
            if (tileList[i - 1] == null) {
                // Stop counting
                break;
                // Otherwise:
            } else {
                // Increase the counter by 1
                counter++;
            }
        }

        return counter;
    }


    /**
     * Calculate number of linked tiles of a given position horizontally.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @param colIndex Index of a column (from 0 - 4)
     * @return Number of linked tiles horizontally
     */
    int getNumOfHorizontalLinkedTiles(int rowIndex, int colIndex) {
        Tile[] row = mosaicGrids[rowIndex];
        return getNumOfLinkedTiles(colIndex, row);
    }


    /**
     * Calculate number of linked tiles of a given position in vertically.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param rowIndex Index of a row (from 0 - 4)
     * @param colIndex Index of a column (from 0 - 4)
     * @return Number of linked tiles vertically
     */
    int getNumOfVerticalLinkedTiles(int rowIndex, int colIndex) {
        Tile[] column = getColumnByIndex(colIndex);
        return getNumOfLinkedTiles(rowIndex, column);
    }


    /**
     * Calculate bonus score for linked tiles.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param row Row index of a tile
     * @param col Column index of a tile
     * @return Bonus score for linking multiple tiles
     */
    int getLinkedTilesBonusScore(int row, int col) {
        int horizontalScore = getNumOfHorizontalLinkedTiles(row, col);
        int verticalScore = getNumOfVerticalLinkedTiles(row, col);
        // If there is no linked tiles at row or column:
        if (horizontalScore == 1 || verticalScore == 1) {
            /* Take the bigger one.
             *
             * For example:
             * * If there is no linked tiles vertical or horizontally:
             *   the score should be max(1, 1) = 1.
             *
             * * If there are 4 linked tiles horizontally, no linked
             *   tiles vertically, the bonus score should be max(4, 1) = 4.
             */
            return Math.max(horizontalScore, verticalScore);
        } else {
            return horizontalScore + verticalScore;
        }
    }


    /**
     * Calculate bonus score for complete row(s).
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @return Bonus score
     */
    int completeRowBonusScore() {
        int counter = 0;
        // Count how may rows have 5 tiles.
        for (int i = 0; i < 5; i++) {
            if (getNumOfTilesByRow(i) == 5) {
                counter += 1;
            }
        }

        // Return bonus score by rule (Number of complete row(s) * 2).
        return counter * 2;
    }


    /**
     * Calculate bonus score for complete column(s).
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @return Bonus score
     */
    int completeColumnBonusScore() {
        int counter = 0;
        // Count how may columns have 5 tiles.
        for (int i = 0; i < 5; i++) {
            if (getNumOfTilesByCol(i) == 5) {
                counter += 1;
            }
        }

        // Return bonus score by rule (Number of complete columns(s) * 7).
        return counter * 7;
    }


    /**
     * Gain 10 bonus points for each colour of tile for which have been placed
     * all 5 tiles on mosaic.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @return Bonus score
     */
    int fiveSameTilesBonusScore() {
        /* Initialize an array for counter occurrence of tiles.
         * It represents occurrence of [blue, green, orange, purple, red]. */
        int[] tilesOccurrenceCounter = new int[5];
        /* Index in tilesOccurrenceCounter.
         * 0 points to blue counter, 1 points to green counter and so on... */
        int index;

        // Iterate through every space in mosaic
        // Iterate through every row:
        for (int i = 0; i < 5; i++) {
            // Iterate through through every cell in the row:
            for (int j = 0; j < 5; j++) {
                // Get the tile by row and column index:
                Tile currTile = getTileByRowAndCol(i, j);
                // If current tile is not null:
                if (currTile != null) {
                    // Based on it's label, add 1 to its corresponding counter.
                    index = currTile.getCharLabel() - 97;
                    tilesOccurrenceCounter[index]++;
                }
            }
        }

        int bonus = 0;
        // Iterate through occurrence array:
        for (int occurrence : tilesOccurrenceCounter) {
            // Find how many tiles has occurred 5 times.
            if (occurrence == 5) {
                bonus += 10;
            }
        }

        return bonus;
    }


    /**
     * From current mosaicGrids to its string representation
     * <p>
     * Usage scope: Basic Mosaic
     *
     * @return String representation of mosaic
     * <p>
     * Rule: *up to* 25 3-character strings.
     * Each 3-character string is defined as follows:
     * 1st character is 'a' to 'e' - representing the tile colour.
     * 2nd character is '0' to '4' - representing the row.
     * 3rd character is '0' to '4' - representing the column.
     * The Mosaic substring is ordered first by row, then by column.
     * That is, "a01" comes before "a10".
     */
    String toMosaicString() {
        StringBuilder sb = new StringBuilder();

        // Iterate through all tiles in mosaic:
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                // Get tile by row index and column index.
                Tile currTile = getTileByRowAndCol(i, j);
                if (currTile != null) {
                    // Add tile to string
                    sb.append(currTile.getLabel());
                    // Add row to string
                    sb.append(i);
                    // Add column to string
                    sb.append(j);
                }
            }
        }

        return sb.toString();
    }


    /**
     * Given a string representation of mosaic, covert it into Mosaic.
     * <p>
     * Hidden logic of processing mosaicStr:
     * If a mosaicStr can be processed into basic mosaic OR variant mosaic,
     * convert to into basic mosaic.
     * If a mosaic can ONLY be processed into variant mosaic, convert it into
     * variant mosaic.
     * <p>
     * Usage scope: Basic Mosaic, Variant Mosaic
     *
     * @param mosaicStr String representation of Mosaic
     *                  (Assume mosaicStr is legal:
     *                   1. length of mosaic is multiple of 3
     *                   2. arrangement is tile-row-column)
     * @return A mosaic interpreted from String
     */
    static Mosaic parseFromMosaicString(String mosaicStr) {
        // Try to convert mosaicStr to basic mosaic:
        try {
            Mosaic mosaic = initBasicMosaic();
            // Iterate through mosaicStr every three characters:
            for (int i = 0; i < mosaicStr.length(); i += 3) {
                // Get the tile in mosaicStr at the first place.
                Tile tile = Tile.getTileFromLabel(mosaicStr.charAt(i));
                // Get the row index in mosaicStr at the second place.
                int row = Character.getNumericValue(mosaicStr.charAt(i + 1));
                // Get the column index in mosaicStr at the third place.
                int col = Character.getNumericValue(mosaicStr.charAt(i + 2));
                // Place the tile to basic mosaic.
                mosaic.tileToBasicMosaic(row, col, tile);
            }
            // Return tiled mosaic.
            return mosaic;
            /* If mosaicStr cannot be translated into basic mosaic, convert it into
             * variant mosaic. */
        } catch (Exception e) {
            Mosaic mosaic = initVariantMosaic();
            // Iterate through mosaicStr every three characters:
            for (int i = 0; i < mosaicStr.length(); i += 3) {
                // Get the tile in mosaicStr at the first place.
                Tile tile = Tile.getTileFromLabel(mosaicStr.charAt(i));
                // Get the row index in mosaicStr at the second place.
                int row = Character.getNumericValue(mosaicStr.charAt(i + 1));
                // Get the column index in mosaicStr at the third place.
                int col = Character.getNumericValue(mosaicStr.charAt(i + 2));
                // Place the tile to variant mosaic.
                mosaic.tileToVariantMosaic(row, col, tile);
            }
            // Return tiled mosaic.
            return mosaic;
        }
    }


    /**
     * Return an array of strings which represent each rows in Mosaic.
     *
     * @return An array of strings which represent each rows
     *         For example: [[null, blue, null, red, green], [null, null, null, null, null], ...] (array)
     */
    String[] toRowsString() {
        String[] rows = new String[5];

        // Iterate through every row:
        for (int i = 0; i < 5; i++) {
            Tile[] row = mosaicGrids[i];
            rows[i] = Arrays.toString(row);
        }

        return rows;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            Tile[] row = mosaicGrids[i];
            sb.append(Arrays.toString(row));
            sb.append("\n");
        }

        return sb.toString();
    }
}