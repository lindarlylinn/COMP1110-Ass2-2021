package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;


import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)

public class applyDraftingMoveTest {
    @Test
    public void testapplyDraftingMove() {
        int count = 0;
        for (String move : Moves) {
            for (String[] gameState : gameStates) {
                GameBoard gameboard = GameBoard.parseFromGameState(gameState);
                gameboard.applyDraftingMove(move);
                String[] out = gameboard.toGameState();
                assertEquals(Arrays.toString(out),Arrays.toString(resultSate[count]));
                count = count+1;
            }


        }
    }
    public ArrayList<String> Moves = new ArrayList<>(Arrays.asList(
            "ACe0", "ACb3", "BCe2", "BCb3", "B1a4", "B4c1", "A1a2", "A4c1"));

    public String[][] gameStates = {
            new String[]{"AF1acde4bbceCabdeeeB1413161414D0000000000","A0Ma00c02e12SFbB0Me04d32SFf"},
            new String[]{"BF1abce4acdeCabbbeB1813161417D0000000000", "A0MS1c13b14c2FbfB0MS0c11c12e23b14a2Fdd"},

    };
    public String[][] resultSate = {
            new String[]{"AF1acde4bbceCabdB1413161414D0000000000, A0Ma00c02e12S0e1FbeeB0Me04d32SFf"},
            new String[]{"BF1abce4acdeCabbbB1813161417D0000000000, A0MS0e11c13b14c2FbfB0MS0c11c12e23b14a2Fdd"},
            new String[]{"AF1acde4bbceCadeeeB1413161414D0000000000, A0Ma00c02e12S3b1FbB0Me04d32SFf"},
            new String[]{"BF1abce4acdeCaeB1813161417D0000000000, A0MS1c13b44c2FbfB0MS0c11c12e23b14a2Fdd"},
            new String[]{"AF1acde4bbceCabdB1413161414D0000000000, A0Ma00c02e12SFbB0Me04d32S2e3Ff"},
            new String[]{"BF1abce4acdeCabbbB1813161417D0000000000, A0MS1c13b14c2FbfB0MS0c11c12e33b14a2Fdd"},
            new String[]{"AF1acde4bbceCadeeeB1413161414D0000000000","A0Ma00c02e12SFbB0Me04d32S3b1Ff"},
            new String[]{"BF1abce4acdeCaeB1813161417D0000000000", "A0MS1c13b14c2FbfB0MS0c11c12e23b44a2Fdd"},
            new String[]{"AF4bbceCabcddeeeeB1413161414D0000000000","A0Ma00c02e12SFbB0Me04d32S4a1Ff"},
            new String[]{"BF4acdeCabbbbceeB1813161417D0000000000", "A0MS1c13b14c2FbfB0MS0c11c12e23b14a3Fdd"},
            new String[]{"AF1acdeCabbbdeeeeB1413161414D0000000000","A0Ma00c02e12SFbB0Me04d32S1c1Ff"},
            new String[]{"BF1abceCaabbbdeeB1813161417D0000000000", "A0MS1c13b14c2FbfB0MS0c11c22e23b14a2Fdd"},
            new String[]{"AF4bbceCabcddeeeeB1413161414D0000000000","A0Ma00c02e12S2a1FbB0Me04d32SFf"},
            new String[]{"BF4acdeCabbbbceeB1813161417D0000000000", "A0MS1c12a13b14c2FbfB0MS0c11c12e23b14a2Fdd"},
            new String[]{"AF1acdeCabbbdeeeeB1413161414D0000000000","A0Ma00c02e12S1c1FbB0Me04d32SFf"},
            new String[]{"BF1abceCaabbbdeeB1813161417D0000000000", "A0MS1c23b14c2FbfB0MS0c11c12e23b14a2Fdd"},
    };



    @Test
    public void testDraftingMoveForCenterOrFactory() {
        for (int i = 1; i < gameStates1.length; i++) {
            GameBoard gameboard = GameBoard.parseFromGameState(gameStates1[i]);
            try{
                gameboard.applyDraftingMove(Moves1.get(i));
                // Verify if an exception is thrown
            }catch(Exception ex){
                assertTrue(ex.getMessage().contains("No such tile in centre.")||
                        ex.getMessage().contains("No such tile in factory."),
                        "No such tile in centre/factory." );
            }
        }
    }
    public ArrayList<String> Moves1 = new ArrayList<>(Arrays.asList(
            "BCe0","BCd3","BFe1","AFa0"));
    public String[][] gameStates1 = {

            new String[]{"AFbddCabccB1514161419D0000000000","A0MS1b22c13a34a1FbB0MS0c11b12e13d4Ff"},
            new String[]{"AFCbbbB1913161418D0000000000", "A0MS0b11b23d14c2FbfB0MS0c11a12d33c14e2Fdd"}
    };


    @Test
    public void testDraftingMoveNotValid() {
        for (String move : Moves2) {
            GameBoard gameboard = GameBoard.parseFromGameState(gameStates2);
            try {
                gameboard.applyDraftingMove(move);
                // Verify if an exception is thrown
            } catch (Exception ex) {
                Tile tile = Tile.getTileFromLabel(move.charAt(2));
                int rowIndex = Character.getNumericValue(move.charAt(3));

                assertTrue(ex.getMessage().contains(
                        tile.toString().toUpperCase()
                                + " tile cannot be placed into row "
                                + rowIndex
                                + " in storage because "
                                + tile.toString().toUpperCase()
                                + " is in "), "tile cannot be placed into a specific row in storage because is in");
            }
        }
    }
    public ArrayList<String> Moves2 = new ArrayList<>(Arrays.asList(
            "ACe0","BCc3","B0d1","A0b2"));
    public String[] gameStates2 = {"AF0bddCabcceeB1514161416D0000000000","A1Me04S1b22c13a34a1FbB0MS0c11b12e13d4Ff"};


}
