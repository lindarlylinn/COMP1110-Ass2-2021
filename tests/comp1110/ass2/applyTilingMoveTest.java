package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class applyTilingMoveTest {
    @Test
    public void testapplyTilingMove() {
        int count = 0;
        for (String[] gameState : gameStates) {
            GameBoard gameboard = GameBoard.parseFromGameState(gameState);
            for (String move : Moves) {
                gameboard.applyTilingMove(move);
                String[] out = gameboard.toGameState();
                assertEquals(Arrays.toString(out),Arrays.toString(resultSate[count]));
                count = count+1;
            }
        }
    }


    public ArrayList<String> Moves = new ArrayList<String>(Arrays.asList(
            "A04","A13","B02","B34"));

    public String[][] gameStates = {
            new String[]{"AFCB1616181614D0000000000","A0Ma03S0e11b22c13a34a1FbeeeeB0Me04S0c11b12e13d4Ff"},
            new String[]{"AFCbbbB1913161418D0000000000", "A0Me02S0b11b23d14c2FbfB0MS0c11a12d33c44e2Fdd"}
    };
    public String[][] resultSate = {
            new String[]{"AFCB1616181614D0000000000, A2Ma03e04S1b22c13a34a1FbeeeeB0Me04S0c11b12e13d4Ff"},
            new String[]{"AFCB1616181614D0001000000, A4Ma03e04b13S2c13a34a1FbeeeeB0Me04S0c11b12e13d4Ff"},
            new String[]{"AFCB1616181614D0001000000, A4Ma03e04b13S2c13a34a1FbeeeeB1Mc02e04S1b12e13d4Ff"},
            new String[]{"AFCB1616181614D0001000300, A4Ma03e04b13S2c13a34a1FbeeeeB2Mc02e04d34S1b12e1Ff"},
            new String[]{"AFCbbbB1913161418D0000000000, A1Me02b04S1b23d14c2FbfB0MS0c11a12d33c44e2Fdd"},
            new String[]{"AFCbbbB1913161418D0001000000, A2Me02b04b13S3d14c2FbfB0MS0c11a12d33c44e2Fdd"},
            new String[]{"AFCbbbB1913161418D0001000000, A2Me02b04b13S3d14c2FbfB1Mc02S1a12d33c44e2Fdd"},
            new String[]{"AFCbbbB1913161418D0001030000, A2Me02b04b13S3d14c2FbfB2Mc02c34S1a12d34e2Fdd"}

    };


    @Test
    public void testStorgeNotFull() {
        GameBoard gameboard = GameBoard.parseFromGameState(gameStates1);
        for(String move : Moves1){
            try{
                gameboard.applyTilingMove(move);
                // Verify if an exception is thrown
            }catch(Exception ex){
                assertTrue(ex.getMessage().contains("The row is not full. Cannot tile at current row!"),
                        "Row you try to move is not full" );
            }
        }
    }

    public ArrayList<String> Moves1 = new ArrayList<String>(Arrays.asList(
            "A24","A42","B20","B32"));

    public String[] gameStates1 = {"AFCB1616181614D0000000000","A0MS0e11b22c13a34a1FbeeeeB0MS0c11b12e13d3Ff"};
}
