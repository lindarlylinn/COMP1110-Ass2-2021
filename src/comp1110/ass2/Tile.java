package comp1110.ass2;

import java.util.*;

/*******************************************************************************
 *                           Enum: Tile                                        *
 *             This class is contributed by Qm9nb25n and WmFuZQ==              *
 ******************************************************************************/
public enum Tile {
    Blue("blue", "a"),
    Green("green", "b"),
    Orange("orange", "c"),
    Purple("purple", "d"),
    Red("red", "e"),

    FirstPlayerToken("First Player Token", "f");

    final private String colour;
    final private String label;

    /**
     * Tile constructor.
     *
     * @param colour Colour of the tile
     * @param label Label of the tile
     */
    Tile(String colour, String label) {
        this.colour = colour;
        this.label = label;
    }


    /**
     * Get the colour of a tile.
     *
     * @return String representation of tile's colour
     */
    public String getColour() {
        return colour;
    }


    /**
     * Get the label (String representation) of a tile.
     *
     * @return String representation of a tile's label
     */
    public String getLabel() {
        return label;
    }


    /**
     * Get the label (char representation) of a tile
     *
     * @return String representation of a tile's label
     */
    public char getCharLabel() {
        return label.charAt(0);
    }


    /**
     * Obtain a list of tiles: Blue, Green, Orange, Purple and Red.
     *
     * @return A list of coloured tiles. (Blue, Green, Orange, Purple and Red)
     */
    public static Tile[] getAllColouredTiles() {
        return new Tile[] {Blue, Green, Orange, Purple, Red};
    }

    /**
     * Obtain all kinds of tiles.
     *
     * @return A list of tiles. (Blue, Green, Orange, Purple, Red, First Player Token)
     */
    public static Tile[] getAllTiles() {
        return new Tile[] {Blue, Green, Orange, Purple, Red, FirstPlayerToken};
    }


    /**
     * Get a tile by it's label.
     *
     * @param label The string representation of label (Not case sensitive)
     * @return The tile corresponding to the label
     */
    public static Tile getTileFromLabel(String label) {
        return switch (label) {
            case "a" -> Blue;
            case "b" -> Green;
            case "c" -> Orange;
            case "d" -> Purple;
            case "e" -> Red;
            case "f" -> FirstPlayerToken;
            default -> null;
        };
    }

    /**
     * Get a tile by it's label (char).
     *
     * @param label The string representation of label (Not case sensitive)
     * @return The tile corresponding to the label
     */
    public static Tile getTileFromLabel(char label) {
        return switch (label) {
            case 'a' -> Blue;
            case 'b' -> Green;
            case 'c' -> Orange;
            case 'd' -> Purple;
            case 'e' -> Red;
            case 'f' -> FirstPlayerToken;
            default -> null;
        };
    }


    /**
     * Giving a string represents tiles, convert it to a list of tiles.
     *
     * @param tilesStr String representation of tiles
     * @return An arraylist of tiles
     */
    static ArrayList<Tile> parseTilesFromString(String tilesStr) {
        ArrayList<Tile> tiles = new ArrayList<>();

        for(int i = 0; i < tilesStr.length(); i++) {
            char currTileChar = tilesStr.charAt(i);
            tiles.add(getTileFromLabel(currTileChar));
        }

        return tiles;
    }


    /**
     * Parse a string into a list of tiles.
     *
     * String format:
     * 5 2-character substrings.
     * 1st substring represents the number of 'a' tiles, from 0 - 20.
     * 2nd substring represents the number of 'b' tiles, from 0 - 20.
     * 3rd substring represents the number of 'c' tiles, from 0 - 20.
     * 4th substring represents the number of 'd' tiles, from 0 - 20.
     * 5th substring represents the number of 'e' tiles, from 0 - 20.
     * <p>
     * For example: "B0005201020" The bag contains zero 'a' tiles, five 'b'
     * tiles, twenty 'c' tiles, ten 'd' tiles and twenty 'e' tiles.
     *
     * @param numString Number representation of tiles
     * @return An arraylist of tiles
     */
    static ArrayList<Tile> parseTilesFromNumString(String numString) {
        if (numString.length() != 10) {
            throw new IllegalArgumentException("The given length of string " +
                    "represents number of each tiles should be 10, but got" +
                    numString.length());
        }

        String[] splitNumString = numString.split("(?<=\\G..)");
        ArrayList<Tile> tiles = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            int numOfTiles = Integer.parseInt(splitNumString[i]);
            Tile currTile = getAllColouredTiles()[i];
            for (int j = 0; j < numOfTiles; j++) {
                tiles.add(currTile);
            }
        }

        return tiles;
    }


    /**
     * Convert tiles into number representation of string.
     *
     * 1st and 2rd characters represent the number of a tiles in the string.
     * 3th and 4th characters represent the number of b tiles in the string.
     * 5th and 6th characters represent the number of c tiles in the string.
     * 7th and 8th characters represent the number of d tiles in the string.
     * 9th and 10th characters represent the number of e tiles in the string.
     *
     * @param tiles A list of tiles
     * @return Number representation of a list of tiles
     */
    static String tilesToNumString(ArrayList<Tile> tiles) {
        int[] counterArray = getTilesStatistic(tiles);

        StringBuilder sb = new StringBuilder();
        for (int num : counterArray) {
            if (num / 10 == 0) {
                sb.append("0");
            }
            sb.append(num);
        }

        return sb.toString();
    }

    /**
     * Count the occurrence of tiles.
     *
     * @param tiles A list of tiles.
     * @return An array represents the occurrence of each tiles in a list of
     * arrays ([occurrence of blue tiles, occurrence of green tiles, ...])
     */
    public static int[] getTilesStatistic(ArrayList<Tile> tiles) {
        int[] counterArray = new int[5];
        for (Tile tile : tiles) {
            switch (tile.getLabel()) {
                case "a" -> counterArray[0] += 1;
                case "b" -> counterArray[1] += 1;
                case "c" -> counterArray[2] += 1;
                case "d" -> counterArray[3] += 1;
                case "e" -> counterArray[4] += 1;
            }
        }

        return counterArray;
    }

    /**
     * Convert from two dimensional array to Array List
     *
     * @param twoDTiles A two dimensional array of tiles.
     * @return An ArrayList
     */
    public static ArrayList<Tile> convert2DArrayToArrayList(Tile[][] twoDTiles){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile[] tilesArray : twoDTiles) {
            tiles.addAll(Arrays.asList(tilesArray));
        }
        return tiles;
    }
    @Override
    public String toString() {
        return colour;
    }
}
