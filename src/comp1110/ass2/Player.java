package comp1110.ass2;

/*******************************************************************************
 *                           Enum: Player                                      *
 *                 This class is contributed by Qm9nb25n                       *
 ******************************************************************************/
public enum Player {
    PLAYER1("A"),
    PLAYER2("B"),
    PLAYER3("C"),
    PLAYER4("D");

    final String id;

    Player(String id) {
        this.id = id;
    }

    /**
     * Get the id of a player
     *
     * @return id of player
     */
    String getId() {
        return id;
    }


    /**
     * Giving an id, get a player
     *
     * @param id String representation of id of player
     * @return a player
     */
    static Player getPlayerById(String id) {
        return switch (id.toUpperCase()) {
            case "A" -> PLAYER1;
            case "B" -> PLAYER2;
            case "C" -> PLAYER3;
            case "D" -> PLAYER4;
            default -> throw new IllegalArgumentException("Invalid player id :" + id);
        };
    }


    /**
     * Giving an id, get a player
     *
     * @param id char representation of id of player
     * @return a player
     */
    static Player getPlayerById(char id) {
        return switch (Character.toUpperCase(id)) {
            case 'A' -> PLAYER1;
            case 'B' -> PLAYER2;
            case 'C' -> PLAYER3;
            case 'D' -> PLAYER4;
            default -> throw new IllegalArgumentException("Invalid player id :" + id);
        };
    }


    /**
     * Get a player by its index.
     *
     * @param index Index of a player (e.g. 1, 2 ... )
     * @return Player corresponds to it's index
     */
    public static Player getPlayerByIndex(int index) {
        return switch (index) {
            case 1 -> PLAYER1;
            case 2 -> PLAYER2;
            case 3 -> PLAYER3;
            case 4 -> PLAYER4;
            default -> throw new IllegalArgumentException("Invalid index for player");
        };
    }


    /**
     * Get index of a player.
     *
     * @param player A player
     * @return Player's index (e.g. PLAYER1 -> 1, PLAYER2 -> 2 ...)
     */
    public static String getIndexOfPlayerStr(Player player) {
        return switch (player) {
            case PLAYER1 -> "1";
            case PLAYER2 -> "2";
            case PLAYER3 -> "3";
            case PLAYER4 -> "4";
        };
    }

    public static int getIndexOfPlayer(Player player) {
        return switch (player) {
            case PLAYER1 -> 1;
            case PLAYER2 -> 2;
            case PLAYER3 -> 3;
            case PLAYER4 -> 4;
        };
    }


    /**
     * Given a player and number of players in the game, go to the next players.
     *
     * @param player       Current player
     * @param numOfPlayers Total number of players in the game
     * @return Next player
     */
    public static Player nextPlayer(Player player, int numOfPlayers) {
        String pid = player.id;
        switch (numOfPlayers) {
            case 2 -> {
                return switch (pid) {
                    case "A" -> PLAYER2;
                    case "B" -> PLAYER1;
                    default -> throw new IllegalArgumentException("Player "
                            + getIndexOfPlayerStr(player)
                            + "is not a valid player in current game board.");
                };
            }
            case 3 -> {
                return switch (pid) {
                    case "A" -> PLAYER2;
                    case "B" -> PLAYER3;
                    case "C" -> PLAYER1;
                    default -> throw new IllegalArgumentException("Player "
                            + getIndexOfPlayerStr(player)
                            + "is not a valid player in current game board.");
                };
            }
            case 4 -> {
                return switch (pid) {
                    case "A" -> PLAYER2;
                    case "B" -> PLAYER3;
                    case "C" -> PLAYER4;
                    case "D" -> PLAYER1;
                    default -> throw new IllegalArgumentException("Player "
                            + getIndexOfPlayerStr(player)
                            + "is not a valid player in current game board.");
                };
            }
            default -> throw new IllegalArgumentException("Invalid number of players: " + numOfPlayers);
        }
    }


    @Override
    public String toString() {
        return switch (id) {
            case "A" -> "Player 1";
            case "B" -> "Player 2";
            case "C" -> "Player 3";
            case "D" -> "Player 4";
            default -> throw new IllegalArgumentException("No such player.");
        };
    }
}
