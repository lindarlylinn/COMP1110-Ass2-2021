package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static comp1110.ass2.Tile.*;


import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class IsPlaceableRowInStorageTest {
    @Test
    public void testisPlaceableRowInStorage() {
        int count = 0;
        for (String[] gameState : gameStates) {
            GameBoard gameboard = GameBoard.parseFromGameState(gameState);
            PlayerBoard playerboard = gameboard.getPlayerBoardByPlayer(Player.getPlayerById('A'));
            for (int j = 0; j < tile.length; j++) {
                for (int z = 0; z < rowIndex[j].length; z++) {
                    boolean out = playerboard.isPlaceableRowInStorage(tile[j], rowIndex[j][z]);
                    System.out.println(out);
                    if (count < result.length) {
                        assertEquals(out, result[count]);
                    }
                    count = count + 1;
                }
            }
        }

    }
    public String[][] gameStates = {

            new String[]{"AFCB1612111314D0001000200", "A7Mc03b04b12e13b21S2a33d34c4FfB2Md01c02d20S1a12e23c34e3Fbbbb"},
            new String[]{"AFCB1913161418D0000000000", "A0MS0b11b23d14c2FbfB0MS0c11a12d33c14e2Fdd"},

    };

    public Tile[] tile = {Blue,Red,Orange,Purple,Green};
    public int[][] rowIndex = {{0,2},{2,3,4},{1,4},{0,2,3,4},{1}};

    public boolean[] result = {
            true,false,
            false,false,false,
            true,true,
            true,false,true,false,
            false,

            false, true,
            true, false, false,
            false, true,
            false, true, true, false,
            false

    };

}
