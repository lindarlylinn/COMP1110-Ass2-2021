package comp1110.ass2;

import java.util.*;

import static comp1110.ass2.Tile.FirstPlayerToken;

public class Azul {
    /**
     * Given a shared state string, determine if it is well-formed.
     * Note: you don't need to consider validity for this task.
     * A sharedState is well-formed if it satisfies the following conditions.
     * <p>
     * [turn][factories][centre][bag][discard]
     * where [turn][factories], [centre], [bag] and [discard] are replaced by the
     * corresponding small string as described below.
     * <p>
     * 0. [turn] The Turn substring is one character 'A'-'D' representing a
     * player, which indicates that it is this player's turn to make the next
     * drafting move. (In a two-player game, the turn substring can only take
     * the values 'A' or 'B').
     * <p>
     * 1. [factories] The factories substring begins with an 'F'
     * and is followed by a collection of *up to* 5 5-character factory strings
     * representing each factory.
     * Each factory string is defined in the following way:
     * 1st character is a sequential digit '0' to '4' - representing the
     * factory number.
     * 2nd - 5th characters are 'a' to 'e', alphabetically - representing
     * the tiles.
     * A factory may have between 0 and 4 tiles. If a factory has 0 tiles,
     * it does not appear in the factories string.
     * Factory strings are ordered by factory number.
     * For example: given the string "F1aabc2abbb4ddee": Factory 1 has tiles
     * 'aabc', Factory 2 has tiles 'abbb', Factory 4 has tiles 'ddee', and
     * Factories 0 and 4 are empty.
     * <p>
     * 2. [centre] The centre substring starts with a 'C'
     * This is followed by *up to* 15 characters.
     * Each character is 'a' to 'e', alphabetically - representing a tile
     * in the centre.
     * The centre string is sorted alphabetically.
     * For example: "Caaabcdde" The Centre contains three 'a' tiles, one 'b'
     * tile, one 'c' tile, two 'd' tile and one 'e' tile.
     * <p>
     * 3. [bag] The bag substring starts with a 'B'
     * and is followed by 5 2-character substrings
     * 1st substring represents the number of 'a' tiles, from 0 - 20.
     * 2nd substring represents the number of 'b' tiles, from 0 - 20.
     * 3rd substring represents the number of 'c' tiles, from 0 - 20.
     * 4th substring represents the number of 'd' tiles, from 0 - 20.
     * 5th substring represents the number of 'e' tiles, from 0 - 20.
     * <p>
     * For example: "B0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles and twenty 'e' tiles.
     * 4. [discard] The discard substring starts with a 'D'
     * and is followed by 5 2-character substrings defined the same as the
     * bag substring.
     * For example: "D0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles, and twenty 'e' tiles.
     *
     * @param sharedState the shared state - factories, bag and discard.
     * @return true if sharedState is well-formed, otherwise return false
     * TASK 2
     *
     * @author WmFuZSBIYWlkZXI=
     */
    public static boolean isSharedStateWellFormed(String sharedState) {
        // FIXED Task 2
        String sharedStateWithoutPlayer = sharedState.substring(1);
        // checks if Factory, Center, Bag and Discard are all present
        String[] sharedStateSplit = sharedStateWithoutPlayer.split("(?=\\sF|C|B|D)");
        if (sharedStateSplit.length != 4) {
            return false;
        }
        // check if Factory, Center, Bag and Discard are in order in string
        if (sharedStateSplit[0].charAt(0) != 'F' || sharedStateSplit[1].charAt(0) != 'C' ||
                sharedStateSplit[2].charAt(0) != 'B' || sharedStateSplit[3].charAt(0) != 'D') {
            return false;
        }
        // checks if first character in string is a player character
        if (sharedState.charAt(0) != 'A' && sharedState.charAt(0) != 'B' &&
                sharedState.charAt(0) != 'C' && sharedState.charAt(0) != 'D') {
            return false;
        }
        if (!sharedStateSplit[0].equals("F")) {
            String factory = sharedStateSplit[0].substring(1);
            String[] factoryArray = factory.split("(?=\\s0|1|2|3|4|5|6|7|8)");
            for (String s : factoryArray) {
                // checks if factories start with number and if factories exceed four
                if ((!Character.isDigit(s.charAt(0))) || (Character.getNumericValue(s.charAt(0)) > 4)) {
                    return false;
                }
                // checks if factory has four tiles
                if (s.length() != 5) {
                    return false;
                }
                //checks that tiles are alphabetically ordered
                if (tilesNotOrderedAlphabetical(s)) {
                    return false;
                }
                // checks that tiles are a,b,c,d or e
                if (notCorrectTiles(s)) {
                    return false;
                }
            }
            // checks that factories are correctly ordered from lowest to highest
            for (int i = 0; i < factoryArray.length - 1; i++) {
                if (factoryArray[i].charAt(0) >= factoryArray[i + 1].charAt(0)) {
                    return false;
                }
            }
        }
        if (!sharedStateSplit[1].equals("C")) {
            String centerTiles = sharedStateSplit[1].substring(1);
            //checks of center has more than 15 tiles in it
            if (centerTiles.length() > 16) {
                return false;
            }
            if (tilesNotOrderedAlphabetical(centerTiles)) {
                return false;
            }

            if (notCorrectTiles(centerTiles)) {
                return false;
            }
        }
        if (sharedStateSplit[2].length() != 11 || sharedStateSplit[3].length() != 11) {
            return false;
        }
        String bag = sharedStateSplit[2].substring(1);
        String discard = sharedStateSplit[3].substring(1);
        // checks of bag and discard just have numbers
        for (int x = 0; x < bag.length(); x++) {
            if (!Character.isDigit(bag.charAt(x)) || !Character.isDigit(discard.charAt(x))){
                return false;
            }
        }
        // checks that numbers in discard are not above 20
        return areTilesBelowTwenty(bag) && (areTilesBelowTwenty(discard));
    }

    static boolean areTilesBelowTwenty(String bagOrDiscard){
        String[] array = new String[5];
        for (int i = 0; i<5;i++) {
            array[i] = bagOrDiscard.charAt(i * 2) + String.valueOf(bagOrDiscard.charAt(i * 2 + 1));
        }
        for (String s : array) {
            if (Integer.parseInt(s) > 20) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a tile's string is NOT correct.
     *
     * @param s string of the tile
     * @return true if the string is not correct
     *
     * @author WmFuZSBIYWlkZXI=
     */
    static boolean notCorrectTiles(String s) {
        for (int z = 0; z < s.substring(1).length(); z++) {
            if (s.substring(1).charAt(z) < 97 || s.substring(1).charAt(z) > 102) {
                return true;
            }
        }
        return false;
    }


    /**
     * Check if tiles are NOT ordered in alphabetical.
     *
     * @param s String represents tiles
     * @return true if tiles are NOT ordered in alphabetical
     *
     * @author WmFuZSBIYWlkZXI=
     */
    static boolean tilesNotOrderedAlphabetical(String s) {
        for (int a = 0; a < s.substring(1).length() - 1; a++) {
            if (s.substring(1).charAt(a) > s.substring(1).charAt(a + 1)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Given a playerState, determine if it is well-formed.
     * Note: you don't have to consider validity for this task.
     * A playerState is composed of individual playerStrings.
     * A playerState is well-formed if it satisfies the following conditions.
     * <p>
     * A playerString follows this pattern: [player][score][mosaic][storage][floor]
     * where [player], [score], [mosaic], [storage] and [floor] are replaced by
     * a corresponding substring as described below.
     * Each playerString is sorted by Player i.e. Player A appears before Player B.
     * <p>
     * 1. [player] The player substring is one character 'A' to 'D' -
     * representing the Player
     * <p>
     * 2. [score] The score substring is one or more digits between '0' and '9' -
     * representing the score
     * <p>
     * 3. [mosaic] The Mosaic substring begins with a 'M'
     * Which is followed by *up to* 25 3-character strings.
     * Each 3-character string is defined as follows:
     * 1st character is 'a' to 'e' - representing the tile colour.
     * 2nd character is '0' to '4' - representing the row.
     * 3rd character is '0' to '4' - representing the column.
     * The Mosaic substring is ordered first by row, then by column.
     * That is, "a01" comes before "a10".
     * <p>
     * 4. [storage] The Storage substring begins with an 'S'
     * and is followed by *up to* 5 3-character strings.
     * Each 3-character string is defined as follows:
     * 1st character is '0' to '4' - representing the row - each row number must only appear once.
     * 2nd character is 'a' to 'e' - representing the tile colour.
     * 3rd character is '0' to '5' - representing the number of tiles stored in that row.
     * Each 3-character string is ordered by row number.
     * <p>
     * 5. [floor] The Floor substring begins with an 'F'
     * and is followed by *up to* 7 characters in alphabetical order.
     * Each character is 'a' to 'f' - where 'f' represents the first player token.
     * There is only one first player token.
     * <p>
     * An entire playerState for 2 players might look like this:
     * "A20Ma02a13b00e42S2a13e44a1FaabbeB30Mc01b11d21S0e12b2F"
     * If we split player A's string into its substrings, we get:
     * [A][20][Ma02a13b00e42][S2a13e44a1][Faabbe].
     *
     * @param playerState the player state string
     * @return True if the playerState is well-formed,
     * false if the playerState is not well-formed
     * TASK 3
     *
     * @author Qm9nb25nIFdhbmc=
     */
    public static boolean isPlayerStateWellFormed(String playerState) {
        // FIXED Task 3
        String[] splitStatesByPlayer = playerState.split("(?=\\sA|B|C|D)");
        String[][] splitPlayerState = new String[splitStatesByPlayer.length][];

        for (int i = 0; i < splitPlayerState.length; i++) {
            String currPlayerState = splitStatesByPlayer[i];
            if (!isSinglePlayerStateWellFormed(currPlayerState)) {
                return false;
            }
        }

        return true;
    }


    /**
     * Check if a single player state is well formed.
     * (Rules can be checked above)
     *
     * @param playerState A string representation of player state
     * @return True if the state is well formed, otherwise return false
     *
     * @author Qm9nb25nIFdhbmc=
     */
    public static boolean isSinglePlayerStateWellFormed(String playerState) {
        // Split string by components in player's board.
        String[] splitPlayerState = playerState.split("(?=\\s.|M|S|F)");

        // Check if we have player, score, mosaic, storage and factory
        if (splitPlayerState.length != 4) {
            return false;
        }

        // Retrieve player and string from splitPlayerState
        String playerAndScore = splitPlayerState[0];
        // Minimal length of player and score should be 2
        if (playerAndScore.length() < 2) {
            return false;
        }

        // Check if the player is legal.
        char player = playerAndScore.charAt(0);
        if (player < 65 || player > 68) {
            return false;
        }

        // Check if score is valid
        for (int i = 1; i < playerAndScore.length(); i++) {
            if (!Character.isDigit(playerAndScore.charAt(i))) {
                return false;
            }
        }

        // Retrieve mosaic string from splitPlayerState.
        String mosaic = splitPlayerState[1].substring(1);
        /* If the length of mosaic string is not multiple of 3 or its length is
           more than 25 * 3, return false */
        if (mosaic.length() % 3 != 0 || mosaic.length() > 3 * 25) {
            return false;
        }

        // Check if mosaic is well formed.
        for (int i = 0; i < mosaic.length(); i += 3) {
            // Check if tile is legal
            char tile = mosaic.charAt(i);
            if (tile < 97 || tile > 101) {
                return false;
            }

            // Check if the value of row is valid
            char row = mosaic.charAt(i + 1);
            if (row < 48 || row > 52) {
                return false;
            }

            // Check if the value of column is valid
            char column = mosaic.charAt(i + 2);
            if (column < 48 || column > 52) {
                return false;
            }
        }

        // Retrieve storage string from splitPlayerState.
        String storage = splitPlayerState[2].substring(1);
        /* If the length of mosaic string is not multiple of 3 or its length is
           more than 25 * 3, return false */
        if (storage.length() % 3 != 0 || storage.length() > 3 * 5) {
            return false;
        }

        ArrayList<Character> rowOccurrence = new ArrayList<>();
        for (int i = 0; i < storage.length(); i += 3) {
            // Check if value of row is valid
            char row = storage.charAt(i);
            if (row < 48 || row > 52) {
                return false;
            }

            // Check if we have duplicate rows
            for (char r : rowOccurrence) {
                if (r == row) {
                    return false;
                }
            }
            rowOccurrence.add(row);

            // Check if tile is legal
            char tile = storage.charAt(i + 1);
            if (tile < 97 || tile > 101) {
                return false;
            }

            // Check if number of tiles is valid
            char numOfTiles = storage.charAt(i + 2);
            if (numOfTiles < 48 || numOfTiles > 53) {
                return false;
            }
        }

        // Retrieve floor string from splitPlayerState.
        String floor = splitPlayerState[3].substring(1);
        if (floor.length() > 7) {
            return false;
        }

        if (floor.length() == 0) {
            return true;
        }

        int firstPlayerTokenCounter = 0;
        for (int i = 1; i < floor.length(); i++) {
            char currTile = floor.charAt(i);
            char prevTile = floor.charAt(i - 1);

            // Check if current tile is valid
            if (currTile < 97 || currTile > 102) {
                return false;
            }

            // Check if tiles are arranged in alphabetical order
            if (currTile < prevTile) {
                return false;
            }

            // If current tile is first player token
            if (currTile == 102) {
                firstPlayerTokenCounter += 1;
            }
        }

        // Check if the number of first player token is less than 2.
        return firstPlayerTokenCounter < 2;
    }

    /**
     * Given the gameState, draw a *random* tile from the bag.
     * If the bag is empty, refill the the bag with the discard pile and then draw a tile.
     * If the discard pile is also empty, return 'Z'.
     *
     * @param gameState the current game state
     * @return the tile drawn from the bag, or 'Z' if the bag and discard pile are empty.
     * TASK 5
     *
     * @author WXV4aW4gU3Vu
     */
    public static char drawTileFromBag(String[] gameState) {
        // FIXED Task 5
        GameBoard gameBoard = GameBoard.parseFromGameState(gameState);

        /* Try to draw a random tile from the bag and return it's char label.
         * catch, return 'Z'.(when error occurs in try means discard pile is also empty) */
        try {
            return gameBoard.takeOneTileFromBag().getCharLabel();
        } catch (Exception e) {
            return 'Z';
        }
    }

    /**
     * Given a state, refill the factories with tiles.
     * If the factories are not all empty, return the given state.
     *
     * @param gameState the state of the game.
     * @return the updated state after the factories have been filled or
     * the given state if not all factories are empty.
     * TASK 6
     *
     * @author WXV4aW4gU3Vu
     */
    public static String[] refillFactories(String[] gameState) {
        // FIXED Task 6
        GameBoard gameBoard = GameBoard.parseFromGameState(gameState);
        // Check if factories are empty.
        if (gameBoard.areFactoriesEmpty()) {
            // Check if centre also empty.
            if (gameBoard.isCentreEmpty()) {
                // Refill all factories.
                gameBoard.refillAllFactories();
            }
        }
        return gameBoard.toGameState();
    }

    /**
     * Given a gameState for a completed game,
     * return bonus points for rows, columns, and sets.
     *
     * @param gameState a completed game state
     * @param player    the player for whom the score is to be returned
     * @return the number of bonus points awarded to this player for rows,
     * columns, and sets
     * TASK 7
     * @author WmFuZSBIYWlkZXI=
     */
    public static int getBonusPoints(String[] gameState, char player) {
        // FIXED Task 7
        GameBoard gameBoard = GameBoard.parseFromGameState(gameState);
        PlayerBoard playerNow = gameBoard.playerBoards[player-65];
        return playerNow.getMosaic().completeColumnBonusScore() +
                playerNow.getMosaic().completeRowBonusScore() +
                playerNow.getMosaic().fiveSameTilesBonusScore();
    }

    /**
     * Given a valid gameState prepare for the next round.
     * 1. Empty the floor area for each player and adjust their score accordingly (see the README).
     * 2. Refill the factories from the bag.
     * * If the bag is empty, refill the bag from the discard pile and then
     * (continue to) refill the factories.
     * * If the bag and discard pile do not contain enough tiles to fill all
     * the factories, fill as many as possible.
     * * If the factories and centre contain tiles other than the first player
     * token, return the current state.
     *
     * @param gameState the game state
     * @return the state for the next round.
     * TASK 8
     *
     * @author Qm9nb25nIFdhbmc=
     */
    public static String[] nextRound(String[] gameState) {
        // FIXED Task 8
        GameBoard gameBoard = GameBoard.parseFromGameState(gameState);
        // Check if factories are empty.
        if (!gameBoard.areFactoriesEmpty()) {
            return gameState;
        }

        // Check if centre are empty.
        if (!gameBoard.isCentreEmpty()) {
            return gameState;
        }

        // Check if we have untiled rows.
        for (PlayerBoard playerBoard : gameBoard.playerBoards) {
            if (playerBoard.getUntiledRows().size() != 0) {
                return gameState;
            }
        }

        // Get the player next round before cleaning the floor.
        Player playerOfNextTurn = gameBoard.getPlayerInNextRound();

        // Clear the floor, deduct scores, add bonus scores.
        for (PlayerBoard playerBoard : gameBoard.playerBoards) {
            playerBoard.minusLostScoreOnFloor();
            gameBoard.placeTilesToDiscard(playerBoard.emptyFloor());
        }

        // Check if it's end of the game:
        if (!gameBoard.isGameComplete()) {
            /* If the game is not complete: refill all factories, set the player
             * of next turn. */
            gameBoard.refillAllFactories();
            gameBoard.turn = playerOfNextTurn;
        } else {
            // Add bonus score to each player board at end of the game.
            for (PlayerBoard playerBoard : gameBoard.playerBoards) {
                playerBoard.addBonusScoreAtTheEndOfTheGame();
            }
        }

        // Return String representation of game board.
        return gameBoard.toGameState();
    }

    /**
     * Given an entire game State, determine whether the state is valid.
     * A game state is valid if it satisfies the following conditions.
     * <p>
     * [General]
     * 1. The game state is well-formed.
     * 2. There are no more than 20 of each colour of tile across all player
     * areas, factories, bag and discard
     * 3. Exactly one first player token 'f' must be present across all player
     * boards and the centre.
     * <p>
     * [Mosaic]
     * 1. No two tiles occupy the same location on a single player's mosaic.
     * 2. Each row contains only 1 of each colour of tile.
     * 3. Each column contains only 1 of each colour of tile.
     * [Storage]
     * 1. The maximum number of tiles stored in a row must not exceed (row_number + 1).
     * 2. The colour of tile stored in a row must not be the same as a colour
     * already found in the corresponding row of the mosaic.
     * <p>
     * [Floor]
     * 1. There are no more than 7 tiles on a single player's floor.
     * [Centre]
     * 1. The number of tiles in the centre is no greater than 3 * the number of empty factories.
     * [Factories]
     * 1. At most one factory has less than 4, but greater than 0 tiles.
     * Any factories with factory number greater than this factory must contain 0 tiles.
     *
     * @param gameState array of strings representing the game state.
     *                  state[0] = sharedState
     *                  state[1] = playerStates
     * @return true if the state is valid, false if it is invalid.
     * TASK 9
     *
     * @author WmFuZSBIYWlkZXI=
     */
    public static boolean isStateValid(String[] gameState) {
        if (!isSharedStateWellFormed(gameState[0]) || !isPlayerStateWellFormed(gameState[1])) {
            return false;
        }
        try {
            GameBoard gameboard = GameBoard.parseFromGameState(gameState);
            // checks if there is  one first player token
            if (gameboard.numberOfFirstPlayerToken() != 1) {
                return false;
            }
            // checks that there is not more than 20 of each colour
            for (int i = 0; i < 5; i++) {
                if (Tile.getTilesStatistic(gameboard.getAllTilesInGameBoard())[i] != 20) {
                    return false;
                }
            }
            if(gameboard.getCentre().contains(FirstPlayerToken)){
             if (gameboard.getCentre().size() > (gameboard.numberOfEmptyFactories()*3)+1) {
             return false;
             }
             }
            else {
                if (gameboard.getCentre().size() > (gameboard.numberOfEmptyFactories()*3)) {
                    return false;
                }
            }
            for (int i=0; i< gameboard.playerBoards.length;i++)
            if (!gameboard.playerBoards[i].isValidStorageAndMosaic()){
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Given a valid gameState and a move, determine whether the move is valid.
     * A Drafting move is a 4-character String.
     * A Drafting move is valid if it satisfies the following conditions:
     * <p>
     * 1. The specified factory/centre contains at least one tile of the specified colour.
     * 2. The storage row the tile is being placed in does not already contain a different colour.
     * 3. The corresponding mosaic row does not already contain a tile of that colour.
     * Note that the tile may be placed on the floor.
     * </p>
     * <p>
     * A Tiling move is a 3-character String.
     * A Tiling move is valid if it satisfies the following conditions:
     * 1. The specified row in the Storage area is full.
     * 2. The specified column does not already contain a tile of the same colour.
     * 3. The specified location in the mosaic is empty.
     * 4. If the specified column is 'F', no valid move exists from the
     * specified row into the mosaic.
     * </p>
     *
     * @param gameState the game state.
     * @param move      A string representing a move.
     * @return true if the move is valid, false if it is invalid.
     * TASK 10
     *
     *  @author WXV4aW4gU3Vu
     */
    public static boolean isMoveValid(String[] gameState, String move) {
        // FIXED Task 10
        // Check if gameState is well-formed or not
        if (!isSharedStateWellFormed(gameState[0]) || !isPlayerStateWellFormed(gameState[1])) {
            return false;
        }
        char[] moves = move.toCharArray();
        GameBoard gameboard = GameBoard.parseFromGameState(gameState);
        // Check if the player making this move is his turn or not
        if(gameboard.getTurn()!=Player.getPlayerById(moves[0])){return false;}
        PlayerBoard currPlayerBoard = gameboard.getPlayerBoardByPlayer(Player.getPlayerById(moves[0]));
        // Check if Drafting Move
        if (move.length() == 4) {
            Tile tile = Tile.getTileFromLabel(moves[2]);
            // Check if specified factory contains at least one tile of the specified colour
            if (moves[1] != 'C') {
                if (!isHave(gameboard.getFactories()[Character.getNumericValue(moves[1])], tile)) {
                    return false;
                }
            // Check if centre contains at least one tile of the specified colour
            } else {
                if (!gameboard.getCentre().contains(tile)) {
                    return false;
                }
            }
            // If tiles are to be placed in specific row
            if (moves[3] != 'F') {
                // then check if that row in storage already contain a different colour and corresponding mosaic
                // row already contain a tile of that colour or not
                return currPlayerBoard.isPlaceableRowInStorage(tile, Character.getNumericValue(moves[3]));
            }
        // Check if Tiling Move
        }else{
            Storage currentStorage = currPlayerBoard.getStorage();
            Mosaic currentMosaic = currPlayerBoard.getMosaic();
            int row = Character.getNumericValue(moves[1]);
            int col = Character.getNumericValue(moves[2]);
            Tile tile = currentStorage.getOccupiedTileByRow(row);
            // Check if row in the Storage area is full
            if(currentStorage.getNumOfTilesByRow(row) != row+1){
                return false;
            }
            // If tiles are to be placed in specified column
            if (moves[2] != 'F'){
                // Check that column already contain a tile of the same colour
                if (!currentMosaic.isPlaceableCol(tile,col)) {
                    return false;
                }
                // Check the location in the mosaic is empty
                return currPlayerBoard.getMosaic().getTileByRowAndCol(row, col) == null;
            // If tiles are to be placed in floor
            }else{
                // Check if it still have a valid move exists
                return !isPlaceble(currPlayerBoard.getMosaic(), row, tile);
            }
        }
        return true;
    }
    /**
     * Given a Mosaic, check if a tile can be placed in a corresponding mosaic row.
     * If do not have any place to put in, return false.
     *
     * @param mosaic currentplayer mosaic.
     * @param row specified row of mosaic.
     * @param tile tile that need to be placed in.
     * @return ture or false.
     *
     * @author WXV4aW4gU3Vu
     */
    public static boolean isPlaceble(Mosaic mosaic ,int row, Tile tile){
        return !mosaic.getPlaceableCols(tile, row).isEmpty();
    }
    /**
     * Given a list of tiles , check if a specific tile in that list.
     * If the list contains at least one of the specific tile, return false.
     *
     * @param tiles a list of tiles.
     * @param til tile that need to be search.
     * @return ture or false.
     *
     * @author WXV4aW4gU3Vu
     */
    public static boolean isHave(Tile[] tiles,Tile til){
        for (Tile tile : tiles) {
            if (tile == til) {
                return true;
            }
        }
        return false;
    }



        /**
         * Given a gameState and a move, apply the move to the gameState.
         * If the move is a Tiling move, you must also update the player's score.
         * If the move is a Tiling move, you must also empty the remaining tiles
         * into the discard.
         * If the move is a Drafting move, you must also move any remaining tiles
         * from the specified factory into the centre.
         * If the move is a Drafting move and you must put tiles onto the floor,
         * any tiles that cannot fit on the floor are placed in the discard with
         * the following exception:
         * If the first player tile would be placed into the discard, it is instead
         * swapped with the last tile in the floor, when the floor is sorted
         * alphabetically.
         *
         * @param gameState the game state.
         * @param move      A string representing a move.
         * @return the updated gameState after the move has been applied.
         * TASK 11
         *
         *  @author WXV4aW4gU3Vu
         */
    public static String[] applyMove(String[] gameState, String move) {
        // FIXED Task 11
        GameBoard gameboard = GameBoard.parseFromGameState(gameState);
        PlayerBoard currPlayerBoard = gameboard.getPlayerBoardByPlayer(Player.getPlayerById(move.charAt(0)));
        Storage currentStorage = currPlayerBoard.getStorage();
        // If it is Tiling move
        if (move.length() == 3) {
            // Try to do apply tiling move
            try {
                gameboard.applyTilingMove(move);
            // Error then current mosaic board is not a Basic mosaic board
            }catch (Exception e){
                // Switch to variant board
                gameboard.castToVariantBoards();
                // Then apply tiling move again
                gameboard.applyTilingMove(move);
            }
            // After this move check storage, if no complete row then change player
            if(currentStorage.getCompleteRows().isEmpty()) {
                gameboard.changeToNextPlayer();
            }
        }else{
            // Apply drafting move
            gameboard.applyDraftingMove(move);
            // Check tiles in centre, not empty then change player for next turn
            if(!gameboard.isCentreEmpty()){
                gameboard.changeToNextPlayer();
            }

        }
        return gameboard.toGameState();
    }

    /**
     * Given a valid game state, return a valid move.
     *
     * @param gameState the game state
     * @return a move for the current game state.
     * TASK 13
     *
     * @author WmFuZSBIYWlkZXI=
     */

    public static String generateAction(String[] gameState) {
        Random rand = new Random();
        ArrayList<String> validDraftingMoves = validMovesDrafting(gameState);
        ArrayList<String> validTilingMoves = validMovesTiling(gameState);

        if (validDraftingMoves.size()>0) {
            return validDraftingMoves.get(rand.nextInt(validDraftingMoves.size()));
        }
        if (validTilingMoves.size()>0) {
            int countFirstStorageRow = 1;
            for (int i =0; i<validTilingMoves.size()-1;i++){
                if (validTilingMoves.get(i).charAt(1) == validTilingMoves.get(i+1).charAt(1)){
                    countFirstStorageRow++;
                }
                else {
                    break;
                }
            }
            return validTilingMoves.get(rand.nextInt(countFirstStorageRow));
        }

        // This is implemented in GreedyMoveAI.
        return null;
    }


    /**
     * Generate a action using greedy move AI
     *
     * @param gameState Current game state
     * @return A drafting move or tiling move
     */
    public static String generateActionAdvanced(String[] gameState) {
        // FIXED Task 15 Implement a "smart" generateAction()
        // Get the current game board by game state
        GameBoard gameBoard = GameBoard.parseFromGameState(gameState);
        // Get current turn by current game state
        Player currTurn = gameBoard.getTurn();

        // Initialize a greedy move AI
        GreedyMoveAI greedyMoveAI = GreedyMoveAI.initGreedyMoveAI(gameBoard, currTurn);

        // Check if the drafting move is finished:
        if (gameBoard.isDraftingMoveFinished()) {
            // If so: generate tiling move
            return greedyMoveAI.generateTilingMoves().get(0);
        } else {
            // Else: generate drafting move
            return greedyMoveAI.generateDraftingMove();
        }
    }


    /**
     * Given a valid gameBoard (including specific components such as the specified player etc.), add valid drafting moves to ArrayList
     *
     * @param gameBoard the game board, player, playerboard, center or factory string, and an ArrayList which will be modified
     *
     * @author WmFuZSBIYWlkZXI=
     */
    public static void validMovesDraftingHelper(ArrayList<String> validDraftingMoves, GameBoard gameBoard, Player player, PlayerBoard playerboard, String centreOrFactoryNumber) {
        List<Tile> getCentreOrFactory;
        if ("C".equals(centreOrFactoryNumber)) {
            getCentreOrFactory = gameBoard.getCentre();
        } else {
            getCentreOrFactory = Arrays.asList(gameBoard.getFactories()[Integer.parseInt(centreOrFactoryNumber)]);
        }
        for (int tileChar = 97; tileChar < 102; tileChar++) {
            Tile tile = Tile.getTileFromLabel(Character.toString(tileChar));
            if (getCentreOrFactory.contains(tile)) {
                validDraftingMoves.add(player.id + centreOrFactoryNumber + Character.toString(tileChar) + "F");
                for (int s = 0; s < 5; s++) {
                    if (playerboard.getStorage().isPlaceableRow(tile, s)) {
                        if (!Arrays.asList(playerboard.getMosaic().mosaicGrids[s])
                                .contains(tile)) {
                            validDraftingMoves.add((player.id) + centreOrFactoryNumber + Character.toString(tileChar) + s + "");
                        }
                    }
                }
            }
        }
    }

    /**
     * Given a valid game state, return all possible drafting valid moves.
     *
     * @param gameState the game state
     * @return  possible valid moves for the current game state.
     *
     * @author WmFuZSBIYWlkZXI=
     */
    public static ArrayList<String> validMovesDrafting(String[] gameState) {
        GameBoard gameBoard = GameBoard.parseFromGameState(gameState);
        Player player = gameBoard.getTurn();
        PlayerBoard playerboard = gameBoard.getPlayerBoardByPlayer(player);
        ArrayList<String> validDraftingMoves = new ArrayList<>();
        if (!gameBoard.areFactoriesEmpty()) {
            for (int x = 0; x < gameBoard.getFactories().length; x++) {
                validMovesDraftingHelper(validDraftingMoves, gameBoard, player, playerboard, String.valueOf(x));
            }
        }
            validMovesDraftingHelper(validDraftingMoves, gameBoard, player, playerboard, "C");

        return validDraftingMoves;
    }
    /**
     * Given a valid game state, all possible valid tiling moves
     *
     * @param gameState the game state
     * @return all possible tiling moves for the current game state.
     *
     * @author WmFuZSBIYWlkZXI=
     */
    public static ArrayList<String> validMovesTiling(String[] gameState) {
        GameBoard gameBoard = GameBoard.parseFromGameState(gameState);
        ArrayList<String> validTilingMoves = new ArrayList<>();
        Player player = gameBoard.getTurn();
        PlayerBoard playerboard = gameBoard.getPlayerBoardByPlayer(player);
            for (int a = 0; a < 5; a++) {
                if (playerboard.getStorage().getNumOfTilesByRow(a) == a+1) {
                    for (int tileChar = 97; tileChar < 102; tileChar++) {
                        Tile tile = Tile.getTileFromLabel(Character.toString(tileChar));
                        if(playerboard.getStorage().getOccupiedTileByRow(a)==tile) {
                            for (int j = 0; j < 5; j++) {
                                if (!Arrays.asList(playerboard.getMosaic().getColumnByIndex(j)).contains(tile)) {
                                    if (playerboard.getMosaic().mosaicGrids[a][j] == null) {
                                        validTilingMoves.add(player.id + a + j + "");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return validTilingMoves;
    }
}
