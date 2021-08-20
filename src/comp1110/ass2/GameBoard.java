package comp1110.ass2;

import java.util.*;

import static comp1110.ass2.Tile.*;

/*******************************************************************************
 *                           Class: GameBoard                                  *
 *                                                                             *
 *          Integrate factories, centre, bag, discard and playerBoards         *
 *            This class is contributed by WmFuZQ==, WXV4aW4= and Qm9nb25n     *
 *                 Author of some methods are not specified                    *
 ******************************************************************************/
public class GameBoard {
    // Shared board.
    Player turn;
    Tile[][] factories;
    private ArrayList<Tile> centre;
    private ArrayList<Tile> bag;
    private ArrayList<Tile> discard;

    // Player boards
    public PlayerBoard[] playerBoards;


    private GameBoard(int numOfPlayers, boolean initBasicPlayerBoard) {
        turn = Player.PLAYER1;
        centre = initCentre();
        bag = initBag();
        discard = new ArrayList<>();
        factories = initFactories(numOfPlayers);

        if (initBasicPlayerBoard) {
            playerBoards = initBasicPlayerBoard(numOfPlayers);
        } else {
            playerBoards = initVariantPlayerBoard(numOfPlayers);
        }
    }



    /**
     * Initialize a basic player board with given number of players.
     *
     * @param numOfPlayers Number of players in the game
     * @return A basic player board
     */
    public static GameBoard initBasicGameBoards(int numOfPlayers) {
        return new GameBoard(numOfPlayers, true);
    }


    /**
     * Initialize a variant player board with given number of players.
     *
     * @param numOfPlayers Number of players in the game
     * @return A basic player board
     */
    public static GameBoard initVariantGameBoards(int numOfPlayers) {
        return new GameBoard(numOfPlayers, false);
    }


    /**
     * Convert basic player boards to variant player boards.
     */
    void castToVariantBoards() {
        for (PlayerBoard playerBoard : playerBoards) {
            playerBoard.convertToVariantMosaic();
        }
    }


    /**
     * Initialize a game and assign the first player by given index.
     *
     * Initialize a game needs the following steps:
     * 1. Set first player
     * 2. Fill all the factories
     *
     * @param firstPlayerIndex First player index
     */
    public void initGame(int firstPlayerIndex) {
        // Set first player of a game.
        setFirstPlayer(firstPlayerIndex);
        // Fill all the factories.
        refillAllFactories();
    }


    /**
     * Initialize a centre. (Place first player token into centre.)
     *
     * @return Place first player token into centre
     */
    static ArrayList<Tile> initCentre() {
        ArrayList<Tile> centre = new ArrayList<>();
        // Add first player token when initialize a centre.
        centre.add(FirstPlayerToken);
        return centre;
    }


    /**
     * Initialize a bag with 100 tiles.
     * (20 of each tiles, not including first player token)
     *
     * @return A bag with 100 tiles, randomly order them
     */
    static ArrayList<Tile> initBag() {
        ArrayList<Tile> bag = new ArrayList<>();

        // Add 20 of each tiles into bag.
        Tile[] tiles = getAllColouredTiles();
        for (Tile tile : tiles) {
            int counter = 0;
            while (counter < 20) {
                bag.add(tile);
                counter++;
            }
        }

        // Shuffle the tiles.
        Collections.shuffle(bag);
        return bag;
    }


    /**
     * Initialize factories by giving number of players.
     *
     * @param numOfPlayers Number of players in the game
     * @return Specific number of factories
     */
    static Tile[][] initFactories(int numOfPlayers) {
        return switch (numOfPlayers) {
            case 2 -> new Tile[5][4];
            case 3 -> new Tile[7][4];
            case 4 -> new Tile[9][4];
            default -> throw new IllegalStateException("Unexpected number of players: " + numOfPlayers + ".");
        };
    }


    /**
     * Initialize basic player boards by given number of players.
     *
     * @param numOfPlayers Number of players
     * @return A list of basic player board
     */
    static PlayerBoard[] initBasicPlayerBoard(int numOfPlayers) {
        return switch (numOfPlayers) {
            case 2 -> new PlayerBoard[] {
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER1),
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER2)
            };
            case 3 -> new PlayerBoard[] {
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER1),
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER2),
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER3)
            };
            case 4 -> new PlayerBoard[] {
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER1),
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER2),
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER3),
                    PlayerBoard.initBasicPlayerBoard(Player.PLAYER4)

            };
            default -> throw new IllegalArgumentException("Can only start a game with 2-4 players.");
        };
    }


    /**
     * Initialize variant player boards by given number of players.
     *
     * @param numOfPlayers Number of players
     * @return A list of basic player board
     */
    static PlayerBoard[] initVariantPlayerBoard(int numOfPlayers) {
        return switch (numOfPlayers) {
            case 2 -> new PlayerBoard[] {
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER1),
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER2)
            };
            case 3 -> new PlayerBoard[] {
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER1),
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER2),
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER3)
            };
            case 4 -> new PlayerBoard[] {
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER1),
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER2),
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER3),
                    PlayerBoard.initVariantPlayerBoard(Player.PLAYER4)

            };
            default -> throw new IllegalArgumentException("Can only start a game with 2-4 players.");
        };
    }


    /**
     * Get turn of current GameBoard.
     *
     * @return Turn of game board
     */
    public Player getTurn() {
        return turn;
    }


    /**
     * Get tiles in bag of current GameBoard.
     *
     * @return Bag (Tiles in bag)
     */
    public ArrayList<Tile> getBag() {
        return bag;
    }


    /**
     * Get tiles in centre of current GameBoard.
     *
     * @return Centre (Tiles in centre)
     */
    public ArrayList<Tile> getCentre() {
        return centre;
    }


    /**
     * Get a map that counts the number of each kind of tiles.
     *
     * @return tile map that counts the number of each tiles
     */
    public LinkedHashMap<Tile, Integer> getSortedCentreTilesMap() {
        LinkedHashMap<Tile, Integer> tileMap = new LinkedHashMap<>() {{
            put(Blue, 0);
            put(Green, 0);
            put(Orange, 0);
            put(Purple, 0);
            put(Red, 0);
            put(FirstPlayerToken, 0);
        }};

        for (Tile tile : centre) {
            tileMap.merge(tile, 1, Integer::sum);
        }

        return tileMap;
    }


    /**
     * Get tiles in discard of current GameBoard.
     *
     * @return Discard (Tiles in discard)
     */
    public ArrayList<Tile> getDiscard() {
        return discard;
    }


    /**
     * Get tiles in factories of current GameBoard.
     *
     * @return Discard (Tiles in discard)
     */
    public Tile[][] getFactories() {
        return factories;
    }


    /**
     * Get a series of player boards.
     *
     * @return An array of playBoards in GameBoard.
     */
    public PlayerBoard[] getPlayerBoards() {
        return playerBoards;
    }


    /**
     * Get number of players in current game board.
     *
     * @return Number of players in game board
     */
    public int getNumOfPlayers() {
        return playerBoards.length;
    }


    /**
     * Check the type of the game in current game board.
     *
     * @return True if the game is a basic game.
     */
    public boolean isBasicGame() {
        return playerBoards[0].isBasicMosaic();
    }


    /**
     * Get a player board by a player.
     *
     * @param player Player to be found in player board
     * @return Player board that matches with player
     */
    public PlayerBoard getPlayerBoardByPlayer(Player player) {
        for (PlayerBoard playerBoard : playerBoards) {
            if (playerBoard.getPlayer().equals(player)) {
                return playerBoard;
            }
        }

        throw new ArrayIndexOutOfBoundsException("No such player in current game board.");
    }


    /**
     * Set the first player by given index.
     *
     * @param firstPlayerIndex Index of first player
     *                         (e.g. 1 stands for PLAYER1 ...)
     */
    void setFirstPlayer(int firstPlayerIndex) {
        // Assign first player.
        turn = Player.getPlayerByIndex(firstPlayerIndex);
    }


    /**
     * Change player board to next turn.
     */
    public void changeToNextPlayer() {
        turn = getNextPlayer(turn);
    }


    /**
     * Get the player of next turn.
     * (Based on the first player token)
     *
     * @return Player of next round
     */
    public Player getPlayerInNextRound() {
        // Iterate through player boards:
        for (PlayerBoard playerBoard : playerBoards) {
            /* If there is a player's board has first player token, assign it as
             * player of next turn. */
            if (playerBoard.isFirstPlayer()) {
                return playerBoard.getPlayer();
            }
        }

        return getNextPlayer(turn);
    }


    public Player getNextPlayerHasUntiledRows() {
        Player nextPlayer = null;
        for (PlayerBoard playerBoard : playerBoards) {
            if (playerBoard.hasCompleteRow()) {
                nextPlayer = playerBoard.getPlayer();
                break;
            }
        }
        return nextPlayer;
    }

    public void setTurn(Player player) {
        turn = player;
    }


    /**
     * Get the next player of current player.
     *
     * @return Next player of current player
     */
    public Player getNextPlayer(Player player) {
        return Player.nextPlayer(player, playerBoards.length);
    }


    /**
     * Place redundant tiles to discard.
     *
     * @param tiles Tiles to be placed into discard
     */
    public void placeTilesToDiscard(ArrayList<Tile> tiles) {
        if (tiles.size() != 0) {
            for (Tile tile : tiles) {
                // If the tile is first player token, place it to centre.
                if (tile.equals(FirstPlayerToken)) {
                    centre.add(tile);
                    // Otherwise place to discard.
                } else {
                    discard.add(tile);
                }
            }
        }
    }

    /**
     * Place tiles into centre.
     *
     * @param tiles tiles to be placed into centre.
     */
    public void placeTilesToCentre(ArrayList<Tile> tiles) {
        centre.addAll(tiles);
    }


    /**
     * Draw and delete one tile from bag.
     * (empty bag refill from discard)
     *
     * @return Tile that has been drawn and deleted from bag
     *
     * @author WXV4aW4gU3Vu
     */
    Tile takeOneTileFromBag(){
        // Check if bag is empty.
        if (isBagEmpty()){
            // Refill the the bag with the discard pile.
            refillBagWithDiscard();
        }
        // Draw and delete the first tile from the bag.
        Random rand = new Random();
        return bag.remove(rand.nextInt(bag.size()));
    }


    /**
     * Draw tiles from bag or discard.
     *
     * @param numOfTiles Number of tiles to be drawn.
     * @return Tiles that has been drawn from bag
     */
    ArrayList<Tile> takeTilesFromBag(int numOfTiles) {
        /* Take tiles from the place,
         * if the place have enough tiles to draw, take the given number
         * if not, take all the tiles from the place. */
        int numOfTilesTakenFromBag = Math.min(bag.size(), numOfTiles);
        // Take tiles first n (numOfTilesTakenFromBag) tiles from bag.
        ArrayList<Tile> drawTiles = new ArrayList<>(bag.subList(0, numOfTilesTakenFromBag));
        // Removing those tiles that has been taken.
        bag = new ArrayList<>(bag.subList(numOfTilesTakenFromBag, bag.size()));

        // Return draw tiles.
        return drawTiles;
    }


    /**
     * Refill bag with tiles in discard.
     */
    void refillBagWithDiscard() {
        // Add all tiles from discard into bag.
        bag.addAll(discard);
        // Empty the discard.
        discard = new ArrayList<>();
    }


    /**
     * Give a factory index, calculate number of tiles in factory.
     *
     * @param factoryIndex Index of factory (e.g. 0, 1, 2, 3 ...)
     * @return Number of tiles in factory
     */
    public int getNumOfTilesInFactory(int factoryIndex) {
        Tile[] currFactory = factories[factoryIndex];

        int counter = 0;

        // Count the number of tiles in factory.
        for (Tile tile: currFactory) {
            if (tile != null) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Check if all factories are empty.
     *
     * @return True if factories is empty
     */
    public boolean areFactoriesEmpty() {
        // If the number of tiles in a factory is not 0, return false.
        for (int i = 0; i < factories.length; i++) {
            if (getNumOfTilesInFactory(i) != 0) {
                return false;
            }
        }

        /* If we've iterated through all factories, and there is no tiles in it,
         * return true. */
        return true;
    }

    /**
     * Check if a bag is empty.
     *
     * @return True if bag is empty.
     *
     * @author WXV4aW4gU3Vu
     */
    public boolean isBagEmpty() {
        return bag.size() == 0;
    }


    /**
     * Check if the centre is empty.
     *
     * @return If the centre is empty
     */
    public boolean isCentreEmpty() {
        // If we have only one element, check if it's first player token.
        if (centre.size() == 1) {
            return centre.get(0) == FirstPlayerToken;
        }
        // Otherwise, check if it's empty.
        return centre.size() == 0;
    }


    /**
     * Get all tiles in player board.
     *
     * @return All tiles in player board
     */
    public ArrayList<Tile> getAllTilesInGameBoard() {
        ArrayList<Tile> tiles = new ArrayList<>();
        tiles.addAll(centre);
        tiles.addAll(bag);
        tiles.addAll(discard);
        tiles.addAll(convert2DArrayToArrayList(factories));
        for (int x = 0; x<getNumOfPlayers(); x++) {
            tiles.addAll(playerBoards[x].getAllTilesInSinglePlayerBoard());
        }
        tiles.removeAll(Collections.singleton(null));

        return tiles;
    }


    /**
     * Get number of first player token in game board.
     *
     * @return Number of first player token
     */
    public int numberOfFirstPlayerToken() {
        int firstPlayerToken = 0;
        for (Tile tile : getAllTilesInGameBoard()) {
            if (tile == FirstPlayerToken) {
                firstPlayerToken++;
            }
        }
        return firstPlayerToken;
    }


    /**
     * Give a factory index and an array list of tiles, fill the corresponding
     * factory with tiles.
     *
     * @param tiles A list of tiles (Size is smaller than 4)
     * @param factoryIndex Index of factory
     */
    void fillFactoryWithTiles(ArrayList<Tile> tiles, int factoryIndex) {
        if (tiles.size() > 4) {
            throw new IllegalArgumentException("The maximum number of tiles in a factory is 4, but got "
                    + tiles.size());
        }

        Tile[] currFactory = factories[factoryIndex];

        for (int i = 0; i < tiles.size(); i++) {
            Tile currTile = tiles.get(i);
            currFactory[i] = currTile;
        }
    }


    /**
     * Give a list of tiles place them into factories.
     *
     * @param tiles A list of tiles to be placed into factories
     */
    void fillFactoriesWithTiles(ArrayList<Tile> tiles) {
        //
        int tileIndex = 0;
        // Iterate though factories:
        for (int i = 0; i < factories.length; i++) {
            /* Start index and endIndex are aiming for splitting tiles from a
             * given list of tiles.
             *
             * * startIndex : place where we start taking tiles.
             * * endIndex: place where we finish taking tiles.
             * * For endIndex: EndIndex is normally 4 + startIndex because we
             *   need to take four tiles to factory.
             *   If endIndex is greater than the length of tiles, in other words
             *   we don't have 4 tiles in the end, ONLY take tiles
             *   from startIndex to the end of tiles arrayList (the rest of the
             *   tiles)).
             */
            int startIndex = tileIndex;
            int endIndex = Math.min(tileIndex + 4, tiles.size());
            /* If there is no tiles in arrayList tile, but the factories are not
               fully filled, start filling factories. */
            if (startIndex > endIndex) {
                break;
            // Otherwise:
            } else {
                // Fill factories with tiles.
                ArrayList<Tile> splitTiles = new ArrayList<>(tiles.subList(startIndex, endIndex));
                fillFactoryWithTiles(splitTiles, i);
                tileIndex += 4;
            }
        }
    }


    /**
     * Get number of empty factories.
     *
     * @return number of empty factories
     */
    public int numberOfEmptyFactories(){
        int counter = 0;
        for (int i=0; i<factories.length;i++){
            if (getNumOfTilesInFactory(i)==0){
             counter++;
            }
        }
        return counter;
    }


    /**
     * Refill factories from bag.
     * (If tiles in bag is not enough to fill factories, add all tiles in
     * discard to bag and then fill factories.)
     */
    public void refillAllFactories() {
        // Get the number of tiles in bag.
        int numOfTilesInBag = bag.size();

        // Get the number of tiles we need to fill factories.
        int numOfTilesNeeded = factories.length * 4;

        /* If the number of tiles we need is greater than the tiles we have in
         * bag, refill bag with tiles in discard. */
        if (numOfTilesNeeded > numOfTilesInBag) {
            refillBagWithDiscard();
        }

        // Update the number of tiles we have.
        numOfTilesInBag = bag.size();

        /* If we have enough tiles after refilling, take number of tiles needed,
         * else, take all tiles form bag. */
        ArrayList<Tile> takenTiles =
                takeTilesFromBag(Math.min(numOfTilesNeeded, numOfTilesInBag));

        fillFactoriesWithTiles(takenTiles);
    }


    /**
     * Draw tiles from factories by giving a factory index and tile.
     *
     * @param factoryIndex Index of a factory
     * @param tile Tiles to draw
     * @return A list of arraylist
     * <p>
     *         ** The first element is drew tile
     * </p>
     *         ** The second element is tiles to be placed into centre
     */
    public ArrayList<ArrayList<Tile>> drawTilesFromFactory(int factoryIndex, Tile tile) {
        ArrayList<Tile> drewTiles = new ArrayList<>();
        ArrayList<Tile> tilesToBePlacedIntoCentre = new ArrayList<>();

        Tile[] factory = factories[factoryIndex];

        // Take tiles from factory
        for (int i = 0; i < 4; i++) {
            Tile currTile = factory[i];
            // If the colour of the tile is same as the given one:
            if (currTile == tile) {
                // Add it to drew tile
                drewTiles.add(currTile);
            // If the colour of the tile is not same as the given one:
            } else {
                if (currTile != null) {
                    // Place tiles into place into centre list
                    tilesToBePlacedIntoCentre.add(currTile);
                }
            }
        }

        /* If there is no tiles drawn from factory, it means the tile is not
         * exist in the factory, throw error. */
        if (drewTiles.size() == 0) {
            throw new IllegalStateException("No such tile in factory.");
        }

        // Clear the factory
        factories[factoryIndex] = new Tile[4];

        /* Save drew tiles and tiles to be placed into centre into an arraylist
         * of arraylist */
        ArrayList<ArrayList<Tile>> drewTilesAndCentreTiles = new ArrayList<>();
        drewTilesAndCentreTiles.add(drewTiles);
        drewTilesAndCentreTiles.add(tilesToBePlacedIntoCentre);

        return drewTilesAndCentreTiles;
    }


    /**
     * Draw tiles from center. If it has first player token, automatically add
     * the first player token into drew tiles.
     *
     * @param tile Selected tile
     * @return A list of selected tiles
     */
    public ArrayList<Tile> drawTileFromCentre(Tile tile) {
        ArrayList<Tile> drewTiles = new ArrayList<>();
        ArrayList<Tile> centreTiles = new ArrayList<>(centre);

        // Iterate centre:
        for (Tile currTile : centreTiles) {
            if (currTile.equals(FirstPlayerToken)) {
                drewTiles.add(FirstPlayerToken);
                centre.remove(FirstPlayerToken);
            }
            // If current tile is same as given tile:
            if (currTile.equals(tile)) {
                // Add it to drewTiles,
                drewTiles.add(currTile);
                // and remove from centre.
                centre.remove(currTile);
            }
        }

        /* If there is no tiles drawn from factory, it means the tile is not
         * exist in the factory, throw error. */
        if (drewTiles.equals(new ArrayList<>())) {
            throw new IllegalStateException("No such tile in centre.");
        }

        return drewTiles;
    }


    /**
     * Apply drafting move.
     * <p>
     * Main goal:
     * Draw tiles from factory or centre. Place them into storage or floor.
     * <p>
     * Note: this method will do the following things:
     * *1 draw tiles from centre or factories
     * *2 place tiles into floor or a row in storage
     * *3 place redundant tiles (when tiling to storage) into floor (if possible)
     * *4 place redundant tiles (when tiling to floor) into discard (if possible)
     * <p>
     * ** After drafting move, please remember to switch to next player. (not
     * implemented in this method)
     *
     * @param move A string representation of drafting move.
     */
    public void applyDraftingMove(String move) {
        // If the length of drafting move is not equal to 4: throw error.
        if (move.length() != 4) {
            throw new IllegalArgumentException("Illegal length of drafting move.");
        }

        // Get player board for applying drafting move.
        Player movePlayer = Player.getPlayerById(move.charAt(0));
        PlayerBoard currPlayerBoard = getPlayerBoardByPlayer(movePlayer);

        // Get the selected tile.
        Tile selectedTile = Tile.getTileFromLabel(move.charAt(2));

        /* Draw tiles from centre or factories */
        ArrayList<Tile> drewTiles;
        // If the place we drew tiles is centre:
        if (move.charAt(1) == 'C') {
            drewTiles = drawTileFromCentre(selectedTile);
        } else {
            int factoryIndex;
            // See if we could acquire index of factory.
            try {
                factoryIndex = Character.getNumericValue(move.charAt(1));
            // If we can't, throw an error.
            } catch (Exception e) {
                throw new IllegalArgumentException("Not a valid factory index");
            }

            // Draw tiles form factory.
            ArrayList<ArrayList<Tile>> drewTilesAndCentreTiles
                    = drawTilesFromFactory(factoryIndex, selectedTile);
            // Get drew tiles
            drewTiles = drewTilesAndCentreTiles.get(0);
            // Place the rest of the tiles in factory into centre.
            ArrayList<Tile> tilesToBePlacedIntoCentre = drewTilesAndCentreTiles.get(1);
            centre.addAll(tilesToBePlacedIntoCentre);
        }

        /* Place tiles to Floor or Storage*/
        // If we gonna place tiles into floor:
        if (move.charAt(3) == 'F') {
            // Place tiles into floor, and then
            ArrayList<Tile> redundantTiles = currPlayerBoard.moveTilesToFloor(drewTiles);
            // If we have some redundant tiles to be placed into discard, place them.
            if (!redundantTiles.equals(new ArrayList<>())) {
                discard.addAll(redundantTiles);
            }
        // If we gonna place tiles into storage:
        } else {
            int rowIndexOfStorage;
            // See if we can acquire row index of storage.
            try {
                rowIndexOfStorage = Character.getNumericValue(move.charAt(3));
            // If we can't, throw an error.
            } catch (Exception e) {
                throw new IllegalArgumentException("Not a valid storage row");
            }

            // Place tiles into storage and then collect tiles that cannot be placed into it's row.
            // Note: if the row is not placeable, it will throw error.
            ArrayList<Tile> redundantTiles = currPlayerBoard.moveTilesToStorage(drewTiles, rowIndexOfStorage);
            // Place the redundant tiles into floor, if floor is full, collect the redundant tiles.
            ArrayList<Tile> redundantTilesOnFloor = currPlayerBoard.moveTilesToFloor(redundantTiles);
            // Place the redundant tiles into discard.
            discard.addAll(redundantTilesOnFloor);
        }
    }


    /**
     * Apply tiling move (Basic mosaic board).
     * <p>
     * Main goal:
     * Move tiles on storage into mosaic or floor
     * <p>
     * *1 Select rows in storage
     * *2 Move to it's corresponding position
     * *3 Put the remaining tiles into discard
     * <p>
     * ** After applying tiling move, remember to choose to continue to tile
     * or clear the floor and switch to next player (not implemented in this
     * method)
     *
     * @param move String representation of tiling move
     */
    public void applyTilingMove(String move) {
        if (move.length() != 3) {
            throw new IllegalArgumentException("Illegal length of tiling move.");
        }

        Player movePlayer = Player.getPlayerById(move.charAt(0));
        PlayerBoard currPlayerBoard = getPlayerBoardByPlayer(movePlayer);

        int rowIndex = Character.getNumericValue(move.charAt(1));
        int colIndex = Character.getNumericValue(move.charAt(2));

        if (currPlayerBoard.rowNotFullInStorage(rowIndex)) {
            System.out.println(rowIndex);
            System.out.println(currPlayerBoard.getStorage().getNumOfTilesByRow(rowIndex));
            throw new IllegalArgumentException("The row is not full. Cannot tile at current row!");
        }

        if (colIndex == 15) {
            ArrayList<Tile> tilesOnRow = currPlayerBoard.removeTilesFromStorageByRow(rowIndex);
            ArrayList<Tile> redundantTiles = currPlayerBoard.moveTilesToFloor(tilesOnRow);
            GameBoard.this.placeTilesToDiscard(redundantTiles);
        } else {
            if (currPlayerBoard.isBasicMosaic()) {
                currPlayerBoard.tileToBasicMosaic(rowIndex, colIndex);
            } else {
                currPlayerBoard.tileToVariantMosaic(rowIndex, colIndex);
            }

            ArrayList<Tile> removedTiles =  currPlayerBoard.removeTilesFromStorageByRow(rowIndex);
            discard.addAll(removedTiles);

            currPlayerBoard.addLinkedBonusScoreAfterTiling(rowIndex, colIndex);
        }
    }


    /**
     * Apply AI's tiling moves.
     */
    public void applyMultipleTilingMoves(ArrayList<String> moves) {
        for (int i = 0; i < moves.size(); i++) {
            String move = moves.get(i);
            if (i == moves.size() - 1) {
                applyTilingMove(move);
                return;
            } else {
                applyTilingMove(move);
            }
        }
    }


    /**
     * Determine if a game is complete by checking if there is any complete rows
     * in each player board.
     *
     * @return If the game is complete
     */
    public boolean isGameComplete() {
        boolean hasCompleteRow = false;
        /* Check if a game is complete by checking if there is any complete rows
         * in mosaic and if there is any untiled rows. */
        for (PlayerBoard playerBoard : playerBoards) {
            if (playerBoard.getUntiledRows().size() != 0) {
                // If there is any row in storage that is not complete, return false.
                return false;
            }
            if (playerBoard.getNumOfTilesOnFloor() != 0) {
                return false;
            }
            if (playerBoard.hasCompleteRow()) {
                hasCompleteRow = true;
            }
        }
        return hasCompleteRow;
    }


    /**
     * Check if we've finished drafting moves.
     *
     * @return True if the drafting moves are finished
     */
    public boolean isDraftingMoveFinished() {
        return areFactoriesEmpty() && isCentreEmpty();
    }


    /**
     * Convert a string representation of game board to actual game board.
     *
     * Hidden logic of tacking basic player board or variant player board:
     * A gameState can be processed into basic player board OR variant player
     * board convert to into basic player board.
     * If a gameState can ONLY be processed into variant player board, convert
     * it into variant player board.
     *
     * @param gameState String representation of game board
     * @return A game board
     *
     * @author Qm9nb25nIFdhbmc=
     */
    public static GameBoard parseFromGameState(String[] gameState) {
        // Set player boards first.
        String playerStates = gameState[1];
        // Split by players.
        String[] splitPlayerStatesByPlayers
                = playerStates.split("(?=\\sA|B|C|D)");

        // Get number of players.
        int numOfPlayers = splitPlayerStatesByPlayers.length;

        GameBoard gameBoard = initVariantGameBoards(numOfPlayers);

        // Set player boards.
        gameBoard.playerBoards = PlayerBoard.parsePlayerBoardFromPlayerState(playerStates);

        // Set shared state.
        String sharedState = gameState[0];
        // Split shared state by Turn, Factory, Centre, Bag, Discard.
        String[] splitSharedState = sharedState.split("(?=\\s.|F|C|B|D)");

        // Set turn.
        String turnStr = splitSharedState[0];
        gameBoard.turn = Player.getPlayerById(turnStr);

        // Set factories.
        String factoriesStr = splitSharedState[1].substring(1);
        if (!factoriesStr.equals("")) {
            String[] splitFactoriesStr = factoriesStr.split("(?=\\s|[0-9])");
            for (String s : splitFactoriesStr) {
                ArrayList<Tile> tiles = Tile.parseTilesFromString(s.substring(1));
                int factoryIndex = Integer.parseInt(s.substring(0, 1));
                gameBoard.fillFactoryWithTiles(tiles, factoryIndex);
            }
        }

        // Set centre.
        String centreStr = splitSharedState[2].substring(1);
        gameBoard.centre = Tile.parseTilesFromString(centreStr);

        // Set bag.
        String bagStr = splitSharedState[3].substring(1);
        gameBoard.bag = Tile.parseTilesFromNumString(bagStr);
        Collections.shuffle(gameBoard.bag);

        // Set discard.
        String discardStr = splitSharedState[4].substring(1);
        gameBoard.discard = Tile.parseTilesFromNumString(discardStr);
        Collections.shuffle(gameBoard.discard);

        return gameBoard;
    }


    /**
     * Convert a game board to game state (String[]).
     *
     * @return GameState (String representation of current game board)
     *
     * @author Qm9nb25nIFdhbmc=
     */
    public String[] toGameState() {
        StringBuilder sharedStateSb = new StringBuilder();

        sharedStateSb.append(turn.getId());

        // Convert factories to string.
        sharedStateSb.append("F");
        StringBuilder factoriesSb = new StringBuilder();
        for (int i = 0; i < factories.length; i++) {
            Tile[] currFactory = factories[i];
            if (getNumOfTilesInFactory(i) != 0) {
                factoriesSb.append(i);
                StringBuilder factoryString = new StringBuilder();
                for (Tile tile : currFactory) {
                    factoryString.append(tile.getLabel());
                }
                char[] charArray = factoryString.toString().toCharArray();
                Arrays.sort(charArray);
                factoriesSb.append(new String(charArray));
            }
        }
        sharedStateSb.append(factoriesSb);


        // Convert centre to string.
        sharedStateSb.append("C");
        StringBuilder centreSb = new StringBuilder();
        for (Tile tile : centre) {
            centreSb.append(tile.getLabel());
        }
        char[] centreCharArray = centreSb.toString().toCharArray();
        Arrays.sort(centreCharArray);
        sharedStateSb.append(centreCharArray);

        // Convert bag to string.
        sharedStateSb.append("B");
        sharedStateSb.append(tilesToNumString(bag));

        // Convert discard to string.
        sharedStateSb.append("D");
        sharedStateSb.append(tilesToNumString(discard));

        StringBuilder playerStatesSb = new StringBuilder();
        for (PlayerBoard playerBoard : playerBoards) {
            playerStatesSb.append(playerBoard.toPlayerState());
        }

        return new String[] {sharedStateSb.toString(), playerStatesSb.toString()};
    }


    /**
     * Convert bag, centre, discard to a more readable string.
     *
     * @param place Bag or centre or discard.
     * @return A string representation of places
     */
    String toPlaceString(ArrayList<Tile> place) {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        for (int i = 1; i < place.size() + 1; i++) {
            sb.append(place.get(i - 1));
            if (i != place.size()) {
                sb.append(", ");
            }
            if (i % 5 == 0 && i != place.size()) {
                sb.append("\n");
            }
        }
        sb.append("]");

        sb.append("\n(").append(place.size()).append(" tiles in total.)");
        return sb.toString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Current turn: ");
        sb.append(turn);
        sb.append("\n*-=-*-=-*-=-*-=-*-=-*-=-*-=-*Shared Board*-=-*-=-*-=-*-=-*-=-*-=-*-=-*");

        sb.append("\nFactories: \n");
        for (Tile[] factory : factories) {
            sb.append(Arrays.toString(factory));
            sb.append("\n");
        }

        sb.append("\nCentre: \n");
        sb.append(toPlaceString(centre));
        sb.append("\n");

        sb.append("\nBag: \n");
        sb.append(toPlaceString(bag));
        sb.append("\n");

        sb.append("\nDiscard: \n");
        sb.append(toPlaceString(discard));
        sb.append("\n");


        sb.append("\n*-=-*-=-*-=-*-=-*-=-*-=-*-=-*Player Boards*-=-*-=-*-=-*-=-*-=-*-=-*-=-*");

        for (int i = 0; i < playerBoards.length; i++) {
            PlayerBoard currPlayerBoard = playerBoards[i];
            sb.append("\nPlayerBoard ");
            sb.append(i + 1);
            sb.append(": ");
            sb.append(currPlayerBoard.toString());
        }
        return sb.toString();
    }
}