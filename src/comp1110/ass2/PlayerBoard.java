package comp1110.ass2;

import java.util.*;

import static comp1110.ass2.Tile.*;


/*******************************************************************************
 *                           Class: playerBoard                                *
 *                                                                             *
 *                   Integrate storage, mosaic and floor                       *
 *            This class is contributed by WmFuZQ==, WXV4aW4= and Qm9nb25n     *
 *                 Author of some methods are not specified                    *
 ******************************************************************************/
public class PlayerBoard {
    private final Player player;
    private Storage storage;
    private Mosaic mosaic;
    private Floor floor;
    private int score;


    /**
     * Player board constructor, initialize an empty player board with a player.
     *
     * @param player Set the player for player board.
     */
    PlayerBoard(Player player) {
        this.player = player;
        this.storage = Storage.initStorage();
        this.floor = Floor.initFloor();
        this.score = 0;
    }


    /**
     * Initialize a basic player board by a given player.
     *
     * @param player The player of a given board
     * @return A basic player board
     */
    static PlayerBoard initBasicPlayerBoard(Player player) {
        PlayerBoard playerBoard = new PlayerBoard(player);
        playerBoard.mosaic = Mosaic.initBasicMosaic();
        return playerBoard;
    }


    /**
     * Initialize a variant player board by a given player.
     *
     * @param player The player of a given board
     * @return A variant player board
     */
    static PlayerBoard initVariantPlayerBoard(Player player) {
        PlayerBoard playerBoard = new PlayerBoard(player);
        playerBoard.mosaic = Mosaic.initVariantMosaic();
        return playerBoard;
    }



    /**
     * Get player of current player board.
     *
     * @return player of current player board
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Get score of current player's board.
     *
     * @return score of current player board
     */
    public int getScore() {
        return score;
    }


    /**
     * Get mosaic of current player's board.
     *
     * @return mosaic of current player board
     */
    public Mosaic getMosaic() {
        return mosaic;
    }


    /**
     * Get storage of current player's board.
     *
     * @return storage of current player board
     */
    public Storage getStorage() {
        return storage;
    }


    /**
     * Get tiles on floor.
     *
     * @return An array of array of tiles.
     * [first 7 tiles on floor, the rest of tiles on floor]
     * Format: first element in array: first 7 tiles on floor
     *         second element in array: the rest of tiles on floor
     */
    public Tile[] getTilesOnFloor() {
        return floor.getFloor();
    }


    /**
     * Get number of tiles on floor.
     *
     * @return Number of tiles on floor.
     */
    public int getNumOfTilesOnFloor() {
        return floor.getNumOfTilesOnFloor();
    }


    /**
     * Check if the player of current player board is the first player in next
     * round.
     *
     * @return True if we have first player token on floor
     */
    boolean isFirstPlayer() {
        return floor.hasFirstPlayerTokenOnFloor();
    }


    /**
     * Check if current game board is a basic player board or variant player
     * board.
     *
     * @return True if the game board is a basic mosaic board
     */
    boolean isBasicMosaic() {
        return mosaic.isBasicMosaic();
    }


    /**
     * Determine if a tile can be placed into a row in storage.
     *
     * @param tile Tile to be placed into storage
     * @param rowIndex Row to be placed
     * @return If a tile can be placed into a row in storage
     */
    public boolean isPlaceableRowInStorage(Tile tile, int rowIndex) {
        boolean isStoragePlaceable = storage.isPlaceableRow(tile, rowIndex);
        boolean isMosaicPlaceable = mosaic.isPlaceableRow(tile, rowIndex);

        return isStoragePlaceable && isMosaicPlaceable;
    }


    /**
     * Get all placeable row in storage by a tile.
     *
     * @param tile Tile to be placed into storage
     * @return All placeable rows
     */
    ArrayList<Integer> getPlaceableRowsByTileInStorage(Tile tile) {
        ArrayList<Integer> placeableRows = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (isPlaceableRowInStorage(tile, i)) {
                placeableRows.add(i);
            }
        }

        return placeableRows;
    }


    /**
     * Get all the placeable columns in mosaic by row index.
     *
     * @param rowIndex Index of placeable
     * @return All the placeable columns
     */
    public ArrayList<Integer> getPlaceableColsInMosaicByRow(int rowIndex) {
        if (rowNotFullInStorage(rowIndex)) {
            throw new IllegalArgumentException("Row is not full");
        }
        Tile occupiedTileInStorage = storage.getOccupiedTileByRow(rowIndex);
        return mosaic.getPlaceableCols(occupiedTileInStorage, rowIndex);
    }


    /**
     * Take a tiles from factory or centre. Place it into storage.
     *
     * @param tiles A list of tiles to be placed into storage
     *              **(Must NOT contain first player token)
     * @param rowIndex Row to be placed
     * @return Redundant tiles that cannot be placed into storage
     */
    public ArrayList<Tile> moveTilesToStorage(ArrayList<Tile> tiles, int rowIndex) {
        boolean hasFirstPlayerTokenInTiles = false;

        // Remove first player token temporarily.
        for (int i = 0; i < tiles.size(); i++) {
            Tile currTile = tiles.get(i);
            if (currTile == FirstPlayerToken) {
                tiles.remove(i);
                hasFirstPlayerTokenInTiles = true;
                break;
            }
        }

        if (!isPlaceableRowInStorage(tiles.get(0), rowIndex)) {
            throw new IllegalArgumentException(
                    tiles.get(0).toString().toUpperCase()
                            + " tile cannot be placed into row "
                            + rowIndex
                            + " in storage because "
                            + tiles.get(0).toString().toUpperCase()
                            + " is in " );
        } else {
            // Return redundant tiles that cannot be placed into storage
            ArrayList<Tile> redundantTiles = storage.addMultiTilesToStorage(rowIndex, tiles);

            if (hasFirstPlayerTokenInTiles) {
                redundantTiles.add(0, FirstPlayerToken);
            }
            return redundantTiles;
        }
    }


    /**
     * Take a tiles from factory or centre. Place it into storage.
     *
     * @param tiles A list of tiles to be placed into floor
     */
    public ArrayList<Tile> moveTilesToFloor(ArrayList<Tile> tiles) {
        // Return redundant tiles
        return floor.addMultipleTilesToFloor(tiles);
    }


    /**
     * Get all tiles in a player board.
     *
     * @return All tiles in a player board.
     *
     * @author WmFuZSBIYWlkZXI=
     */
    public ArrayList<Tile> getAllTilesInSinglePlayerBoard(){
        ArrayList<Tile> tiles = new ArrayList<>();
        List<Tile> listFloor = Arrays.asList(floor.floor);
        tiles.addAll(listFloor);
        tiles.addAll(convert2DArrayToArrayList(mosaic.mosaicGrids));
        tiles.addAll(convert2DArrayToArrayList(storage.storageRows));

        return tiles;
    }


    /**
     * Tile tiles from storage to mosaic.
     *
     * @param rowIndex Index of the row to place tile in mosaic
     * @param colIndex Index of the column to place tile in mosaic
     */
    public void tileToBasicMosaic(int rowIndex, int colIndex) {
        Tile currTile = storage.getOccupiedTileByRow(rowIndex);
        mosaic.tileToBasicMosaic(rowIndex, colIndex, currTile);
    }


    /**
     * Tile tiles from storage to mosaic.
     *
     * @param rowIndex Index of the row to place tile in mosaic
     * @param colIndex Index of the column to place tile in mosaic
     */
    public void tileToVariantMosaic(int rowIndex, int colIndex) {
        Tile currTile = storage.getOccupiedTileByRow(rowIndex);
        mosaic.tileToVariantMosaic(rowIndex, colIndex, currTile);
    }


    /**
     * Calculate bonus score for linked tiles after tiling to mosaic
     *
     * @param rowIndex Index of the row which has placed tile in mosaic
     * @param colIndex Index of the column which has placed tile in mosaic
     */
     public int addLinkedBonusScoreAfterTiling(int rowIndex, int colIndex) {
        int bonus = mosaic.getLinkedTilesBonusScore(rowIndex, colIndex);
        score += bonus;
        return score;
    }


    /**
     * Remove tiles from a row in storage after tiling.
     *
     * @param rowIndex The row to be cleared in storage
     * @return Tiles which has been removed from storage
     *         **(To be placed into discard)
     */
    public ArrayList<Tile> removeTilesFromStorageByRow(int rowIndex) {
        if (storage.getNumOfTilesByRow(rowIndex) == rowIndex + 1) {
            ArrayList<Tile> tiles = storage.removeTilesByRow(rowIndex);
            tiles.remove(0);
            return tiles;
        } else {
            throw new IllegalArgumentException("Row "
                                                + rowIndex
                                                + " is not complete.");
        }
    }


    /**
     * Remove tiles from a row in storage after tiling return all tiles in the
     * row.
     *
     * @param rowIndex The row to be cleared in storage
     * @return Tiles which has been removed from storage
     *         **(To be placed into discard)
     */
    public ArrayList<Tile> removeAllTilesFromStorageByRow(int rowIndex) {
        if (storage.getNumOfTilesByRow(rowIndex) == rowIndex + 1) {
            return storage.removeTilesByRow(rowIndex);
        } else {
            throw new IllegalArgumentException("Row "
                    + rowIndex
                    + " is not complete.");
        }
    }


    /**
     * Get rows that need to be tiled.
     *
     * @return A list of rows that has completed (untiled rows)
     */
    public ArrayList<Integer> getUntiledRows() {
        return storage.getCompleteRows();
    }


    /**
     * Calculate points lost for tiles on floor.
     */
    public int minusLostScoreOnFloor() {
        int numOfTiles = floor.getNumOfTilesOnFloor();
        score += switch (numOfTiles) {
            case 0 -> 0;
            case 1 -> -1;
            case 2 -> -2;
            case 3 -> -4;
            case 4 -> -6;
            case 5 -> -8;
            case 6 -> -11;
            // 7+ tiles on floor
            default -> -14;
        };

        // Make score always greater or equal to 0.
        score = Math.max(0, score);

        return score;
    }


    /**
     * Empty floor after tiling.
     *
     * @return Tiles to be placed into DISCARD
     */
    public ArrayList<Tile> emptyFloor() {
        return floor.emptyFloor();
    }


    /**
     * Determine if we have complete rows in the current game board.
     *
     * @return If there exists a complete row in current game board
     */
    boolean hasCompleteRow() {
        for (int i = 0; i < 5; i++) {
            if (mosaic.getNumOfTilesByRow(i) == 5) {
                return true;
            }
        }

        return false;
    }


    /**
     * Add bonus score at the end of the game.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    public void addBonusScoreAtTheEndOfTheGame() {
        score += mosaic.completeRowBonusScore();
        score += mosaic.completeColumnBonusScore();
        score += mosaic.fiveSameTilesBonusScore();
    }


    public ArrayList<Integer> getCompleteRowsInMosaic() {
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (mosaic.getNumOfTilesByRow(i) == 5) {
                rows.add(i);
            }
        }
        return rows;
    }


    public ArrayList<Integer> getCompleteColumnsInMosaic() {
        ArrayList<Integer> columns = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            if (mosaic.getNumOfTilesByCol(i) == 5) {
                columns.add(i);
            }
        }

        return columns;
    }

    public ArrayList<Tile> getCompletePattern() {
        LinkedHashMap<Tile, Integer> tileCount = new LinkedHashMap<>();
        for (int rowIndex = 0; rowIndex < 5; rowIndex++) {
            for (int colIndex = 0; colIndex < 5; colIndex++) {
                Tile tile = mosaic.getTileByRowAndCol(rowIndex, colIndex);
                if (tile != null) {
                    tileCount.merge(tile, 1, Integer::sum);
                }
            }
        }

        ArrayList<Tile> completeTiles = new ArrayList<>();
        for (Map.Entry<Tile, Integer> entry : tileCount.entrySet()) {
            if (entry.getValue().equals(5)) {
                completeTiles.add(entry.getKey());
            }
        }

        return completeTiles;
    }


    /**
     * Convert player board to its string representation.
     *
     * @return String representation of player board.
     */
    String toPlayerState() {
        return player.getId() + score +
                'M' + mosaic.toMosaicString() +
                'S' + storage.toStorageString() +
                'F' + floor.toFloorString();
    }


    /**
     * Convert a player state(String) into player board
     *
     * @param singlePlayerState String representation of game state.
     */
    static PlayerBoard parsePlayerBoardFromSinglePlayerState(String singlePlayerState) {
        // Split string by components in player's board.
        String[] splitPlayerState = singlePlayerState.split("(?=\\s.|M|S|F)");

        String playerAndScore = splitPlayerState[0];
        Player player = Player.getPlayerById(playerAndScore.charAt(0));

        PlayerBoard playerBoard = initVariantPlayerBoard(player);
        playerBoard.score = Integer.parseInt(playerAndScore.substring(1));

        /* If a mosaicStr can be processed into basic mosaic OR variant mosaic,
         * convert to into basic mosaic.
         * If a mosaic can ONLY be processed into variant mosaic, convert it into
         * variant mosaic. */
        String mosaicString = splitPlayerState[1].substring(1);
        playerBoard.mosaic = Mosaic.parseFromMosaicString(mosaicString);

        String storageString = splitPlayerState[2].substring(1);
        playerBoard.storage = Storage.parseFromStorageString(storageString);

        String floorString = splitPlayerState[3].substring(1);
        playerBoard.floor = Floor.parseFloorFromString(floorString);

        return playerBoard;
    }


    /**
     * Convert a full player state(String) into a list of player board.
     *
     * @param playerStates A consecutive player state
     * @return A list of player states
     */
    static PlayerBoard[] parsePlayerBoardFromPlayerState(String playerStates) {
        // Split by players.
        String[] splitPlayerStatesByPlayers
                = playerStates.split("(?=\\sA|B|C|D)");

        PlayerBoard[] playerBoards = new PlayerBoard[splitPlayerStatesByPlayers.length];

        // Get number of players.
        int numOfPlayers = splitPlayerStatesByPlayers.length;

        // Set player boards.
        for (int i = 0; i < numOfPlayers; i++) {
            String currPlayerStateStr = splitPlayerStatesByPlayers[i];
            playerBoards[i] = PlayerBoard.
                    parsePlayerBoardFromSinglePlayerState(currPlayerStateStr);
        }

        /* Check if the player boards mixed basic player boards with variant
         * player boards. */
        boolean hasVariantBoard = false;
        for (PlayerBoard playerBoard : playerBoards) {
            if (!playerBoard.isBasicMosaic()) {
                hasVariantBoard = true;
                break;
            }
        }
        if (hasVariantBoard) {
            for (PlayerBoard playerBoard : playerBoards) {
                playerBoard.convertToVariantMosaic();
            }
        }

        return playerBoards;
    }


    /**
     * Check if Storage is working harmony with Mosaic.
     * <p>
     * @return True if satisfies the following condition:
     *         The tile(s) occupied in a Storage row should not exists on Mosaic
     */
     boolean isValidStorageAndMosaic() {
        for (int i =0; i<mosaic.mosaicGrids.length; i++){
            if (storage.getOccupiedTileByRow(i)!= null) {
            if (Arrays.asList(mosaic.mosaicGrids[i]).contains(storage.getOccupiedTileByRow(i))){
                return false;
            }
            }
        }
        return true;
    }


    /**
     * Check if a row is full in storage.
     *
     * @param rowIndex Index of a row
     * @return True if a row is full
     */
    boolean rowNotFullInStorage(int rowIndex) {
        return storage.getNumOfTilesByRow(rowIndex) != rowIndex + 1;
    }


    /**
     * Convert a basic player board to variant player board.
     */
    void convertToVariantMosaic() {
        mosaic.convertToVariantMosaic();
    }


    @Override
    public String toString() {
        String[] storageRowsString = storage.toRowsString();
        String[] mosaicRowsString = mosaic.toRowsString();

        StringBuilder sb = new StringBuilder();

        sb.append("\nCurrent Player: ");
        sb.append(player);
        sb.append("\nCurrent Score: ");
        sb.append(score);

        sb.append("\nStorage:");
        String gap = "        ";
        sb.append(" ".repeat(storageRowsString[0].length()));
        sb.append("Mosaic:\n");
        for (int i = 0; i < 5; i++) {
            sb.append(storageRowsString[i]);
            sb.append(gap);
            sb.append(mosaicRowsString[i]);
            sb.append("\n");
        }

        sb.append("\nFloor: ");
        sb.append(floor);
        return sb.toString();
    }

}