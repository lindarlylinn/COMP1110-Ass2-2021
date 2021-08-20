package comp1110.ass2;

import java.util.*;

public abstract class AI {
    /** Note: Format of drafting moves and tiling moves are the same. **/
    GameBoard gameBoard;
    Player player;

    /**
     * Get all the legal drafting moves.
     *
     * @return All legal drafting moves
     *
     * @author Qm9nb25nIFdhbmc=
     */
    ArrayList<String> getLegalDraftingMoves() {
        // Create a set to avoid duplicate move.
        HashSet<String> draftingMoves = new HashSet<>();

        // Get player board based on player.
        PlayerBoard playerBoard = gameBoard.getPlayerBoardByPlayer(player);
        // Get the player id. (FORMAT: Capital letter in String. e.g. "A", "B"...)
        String currPlayerString = player.getId();

        // Get all coloured tiles.
        Tile[] allTiles = Tile.getAllColouredTiles();
        // Create a map records the legal rows. (e.g. {Red: [0,1,4], Blue: [0]...})
        HashMap<Tile, ArrayList<Integer>> legalRowsForTiles = new HashMap<>();
        for (Tile tile : allTiles) {
            ArrayList<Integer> legalRows = playerBoard.getPlaceableRowsByTileInStorage(tile);
            // Since all the tiles can be placed on floor. Add floor as legal row as well.
            // Set the row number as "-1".
            legalRows.add(-1);
            legalRowsForTiles.put(tile, legalRows);
        }

        int numOfFactories = gameBoard.getFactories().length;
        // Iterate through every factories:
        for (int factoryIndex = 0; factoryIndex < numOfFactories; factoryIndex++) {
            // Get factory based on factory index.
            Tile[] currFactory = gameBoard.getFactories()[factoryIndex];
            // Iterate tiles through factory.
            for (Tile tile : currFactory) {
                // Get legal tiles.
                ArrayList<Integer> placeableRows = legalRowsForTiles.get(tile);
                // If there is no placeable tiles: jump to next tile.
                if (placeableRows == null) {
                    break;
                }
                // If there are placeable rows:
                for (int rowValue : placeableRows) {
                    // Convert the move to string.
                    String moveString = currPlayerString +
                            factoryIndex +
                            tile.getLabel();
                    if (rowValue == -1) {
                        moveString += "F";
                    } else {
                        moveString += rowValue;
                    }
                    // Add to array drafting moves.
                    draftingMoves.add(moveString);
                }
            }
        }

        // Iterate in centre:
        for (Tile tile : gameBoard.getCentre()) {
            // Get all the placeable rows of the tile.
            ArrayList<Integer> placeableRows = legalRowsForTiles.get(tile);
            // If there is no placeable tiles: jump to next tile.
            if (placeableRows != null) {
                // If there are placeable rows:
                for (int rowValue : placeableRows) {
                    // Convert the move to string.
                    String moveString = currPlayerString +
                            "C" +
                            tile.getLabel();
                    if (rowValue == -1) {
                        moveString += "F";
                    } else {
                        moveString += rowValue;
                    }
                    // Add to array drafting moves.
                    draftingMoves.add(moveString);
                }
            }
        }

        // Sort the moves.
        ArrayList<String> legalDraftingMoves = new ArrayList<>(draftingMoves);
        Collections.sort(legalDraftingMoves);
        return legalDraftingMoves;
    }


    /**
     * Get all the legal tiling moves for a player board.
     *
     * @return All legal drafting moves
     *
     * @author Qm9nb25nIFdhbmc=
     */
    ArrayList<String> getLegalTilingMoves() {
        ArrayList<String> legalTilingMoves = new ArrayList<>();

        // Get the player board based on player.
        PlayerBoard playerBoard = gameBoard.getPlayerBoardByPlayer(player);
        // Get all untiled rows.
        ArrayList<Integer> untiledRows = playerBoard.getUntiledRows();

        // If there is no untiled rows, return a empty array.
        if (untiledRows.size() == 0) {
            return legalTilingMoves;
        }

        // Iterate through every rows:
        for (int rowIndex : untiledRows) {
            // Get all placeable columns.
            ArrayList<Integer> placeableColumns = playerBoard.getPlaceableColsInMosaicByRow(rowIndex);
            if (placeableColumns.size() == 0) {
                String moveString = player.getId() + rowIndex + "F";
                legalTilingMoves.add(moveString);
            } else {
                // Iterate through every number in placeable columns:
                for (int colIndex : placeableColumns) {
                    // Convert to move:
                    String moveString = player.getId() + rowIndex + colIndex;
                    // Add to array legalTilingMoves.
                    legalTilingMoves.add(moveString);
                }
            }
        }

        return legalTilingMoves;
    }


    /**
     * Give all legal moves, filter the impossible moves.
     *
     * @param legalMoves All separate legal moves
     * @return All reasonable moves.
     */
    ArrayList<ArrayList<String>> getAllReasonableTilingMovesForVariantGame(ArrayList<String> legalMoves) {
        // Get row move pair of the moves.
        LinkedHashMap<Integer, ArrayList<String>> rowMovePair = new LinkedHashMap<>();
        // Iterate through all legal moves:
        for (String move : legalMoves) {
            int rowValue = Character.getNumericValue(move.charAt(1));
            // Match move with row index.
            try {
                ArrayList<String> moves = rowMovePair.get(rowValue);
                moves.add(move);
                rowMovePair.put(rowValue, moves);
            } catch (Exception e) {
                ArrayList<String> moves = new ArrayList<>();
                moves.add(move);
                rowMovePair.put(rowValue, moves);
            }
        }

        // Iterate through all moves:
        for (Map.Entry<Integer, ArrayList<String>> entry : rowMovePair.entrySet()) {
            // Add "to floor" moves to row move pair.
            ArrayList<String> moves = entry.getValue();
            boolean hasToFloor = false;
            for (String move : moves) {
                if (move.charAt(2) == 'F') {
                    hasToFloor = true;
                    break;
                }
            }

            if (!hasToFloor) {
                String player = this.player.id;
                int rowVal = entry.getKey();
                String move = player + rowVal + "F";
                moves.add(moves.size(), move);
                rowMovePair.put(rowVal, moves);
            }
        }

        // Flatten the map "rowMovePair", get all values of the map.
        ArrayList<ArrayList<String>> rowMovePairArr = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<String>> entry : rowMovePair.entrySet()) {
            rowMovePairArr.add(entry.getValue());
        }

        // Get all moves combination.
        return getAllMovesCombination(rowMovePairArr);
    }


    /**
     * Get the combinations of all moves.
     *
     * @param moves possible moves in different rows
     * @return A combination of all rows.
     *
     * @author Qm9nb25nIFdhbmc=
     * Inspired by:
     * https://www.geeksforgeeks.org/combinations-from-n-arrays-picking-one-element-from-each-array/
     */
    static ArrayList<ArrayList<String>> getAllMovesCombination(ArrayList<ArrayList<String>> moves) {

        // Number of moves
        int n = moves.size();

        // To keep track of next element in each of the n moves
        int[] indices = new int[n];

        // Initialize with first element's index
        for(int i = 0; i < n; i++) {
            indices[i] = 0;
        }

        // Initialize an arraylist of arraylists to record results.
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        while (true) {
            // Print current combination
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                temp.add(moves.get(i).get(indices[i]));
            }
            result.add(temp);

            // Find the rightmost array that has more
            // elements left after the current element
            // in that array
            int next = n - 1;
            while (next >= 0 && (indices[next] + 1 >= moves.get(next).size())) {
                next--;
            }
            // No such array is found so no more
            // combinations left
            if (next < 0) {
                return result;
            } else {
                // If found move to next element in that
                // array
                indices[next]++;

                // For all arrays to the right of this
                // array current index again points to
                // first element
                for (int i = next + 1; i < n; i++) {
                    indices[i] = 0;
                }
            }
        }
    }


    /**
     * Evaluate a player board.
     * <p>
     * Mainly used in drafting moves.
     * </p>
     *
     * @param playerBoard Player board to evaluate.
     * @return Evaluation score.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    double draftingMoveEvaluation(PlayerBoard playerBoard) {
        double score = 0;
        Storage storageArea = playerBoard.getStorage();
        ArrayList<Integer> completeRows = storageArea.getCompleteRows();
        for (Integer rowIndex : completeRows) {
            score += 3 + (rowIndex + 1) * 0.8;
        }

        score -= playerBoard.getNumOfTilesOnFloor() * 2;
        return score;
    }


    /**
     * Generate next drafting move according to current game board.
     *
     * @return The drafting move decided by AI
     */
    public abstract String generateDraftingMove();


    /**
     * Generate next tiling move according to current game board.
     *
     * @return The tiling move decided by AI
     */
    public abstract ArrayList<String> generateTilingMoves();
}