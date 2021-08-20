package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)


public class ValidDraftingMovesTest {
    @Test
    public void testValidDraftingMoves() {
       for (int i=0; i<gameStates.length;i++) {
           ArrayList<String> out = Azul.validMovesDrafting(gameStates[i]);
           assertEquals(out.size(),validMoves.get(i).size());
           Set<String> outSet = new HashSet<>(out);
           Set<String> outSetActual = new HashSet<>(validMoves.get(i));
           assertEquals(outSet,outSetActual);
       }
    }

    public ArrayList<ArrayList<String>> validMoves = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList("A1b0", "A2d1", "A0d3", "A4d0", "A1b1", "A2d2", "A0d4", "A0d1", "A2d0", "A0d2", "A0d0", "A4cF", "A2cF", "A3eF", "A1eF", "A4c4", "A4c2", "A2c4", "A4c3", "A4c0", "A2c2", "A3e3", "A4c1", "A2c3", "A3e4", "A0a0", "A1c1", "A3c0", "A0a1", "A1c2", "A1c0", "A3bF", "A1bF", "A4dF", "A2dF", "A0dF", "A3b4", "A3b2", "A4d3", "A1b4", "A3b3", "A4d4", "A3b0", "A4d1", "A1b2", "A2d3", "A3b1", "A4d2", "A1b3", "A2d4", "A3d0", "A0b1", "A2b0", "A3d1", "A0b2", "A0b0", "A0aF", "A3cF", "A1cF", "A3c3", "A0a4", "A3c4", "A3c1", "A0a2", "A1c3", "A3c2", "A0a3", "A1c4", "A2c0", "A3e1", "A1e3", "A2c1", "A3e2", "A1e4", "A1e1", "A3e0", "A1e2", "A1e0", "A4bF", "A3dF", "A2bF", "A0bF", "A4b3", "A4b4", "A4b1", "A2b3", "A3d4", "A4b2", "A2b4", "A2b1", "A3d2", "A0b3", "A4b0", "A2b2", "A3d3", "A0b4")),
            new ArrayList<>(Arrays.asList("B3c0", "B1c2", "B3c1", "B1c3", "B1c0", "B1c1", "B4dF", "B3bF", "B2dF", "B1bF", "BCaF", "B3b3", "B4d4", "B3b4", "B3b1", "B4d2", "B1b3", "B2d4", "B3b2", "B4d3", "B1b4", "B2b0", "B3d1", "B2b1", "B3d2", "B3d0", "B3cF", "B1cF", "B3c4", "B3c2", "B1c4", "B3c3", "B2c1", "B3e2", "B1e4", "B4c0", "B2c2", "B3e3", "B3e0", "B1e2", "B2c0", "B3e1", "B1e3", "B1e0", "B1e1", "B4bF", "B2bF", "B3dF", "BCd4", "BCd3", "BCd2", "BCd1", "BCd0", "B4b4", "B4b2", "B2b4", "B4b3", "B4b0", "B2b2", "B3d3", "B4b1", "B2b3", "B3d4", "B4d0", "B1b1", "B2d2", "B3b0", "B4d1", "B1b2", "B2d3", "B2d0", "B1b0", "B2d1", "BCa4", "B4cF", "BCa3", "B3eF", "BCa2", "B2cF", "BCa1", "B1eF", "BCa0", "B4c3", "BCdF", "B4c4", "B4c1", "B2c3", "B3e4", "B4c2", "B2c4")),
            new ArrayList<>(Arrays.asList("A1b0", "A1b1", "A2d2", "A2d0", "ACa3", "A2cF", "ACa4", "ACa2", "A3eF", "ACa0", "A1eF", "ACdF", "A2c4", "A2c2", "A3e3", "A2c3", "A3e4", "A3c0", "A1c2", "A1c0", "A3bF", "ACb4", "A1bF", "ACb2", "ACb3", "A2dF", "ACb0", "ACb1", "ACaF", "A3b4", "A3b2", "A1b4", "A3b3", "A3b0", "A1b2", "A2d3", "A3b1", "A1b3", "A2d4", "A3d0", "A2b0", "A3cF", "A1cF", "ACbF", "A3c3", "A3c4", "A1c3", "A3c2", "A1c4", "A2c0", "A1e3", "A3e2", "A1e4", "A3e0", "A1e2", "A1e0", "A3dF", "A2bF", "ACd4", "ACd2", "ACd3", "ACd0", "A2b3", "A3d4", "A2b4", "A2b1", "A3d2", "A2b2", "A3d3")),
            new ArrayList<>(Arrays.asList("B1c2", "B3c1", "B1c3", "B1c1", "B3bF", "B2dF", "B1bF", "BCaF", "B3b3", "B3b4", "B3b1", "B1b3", "B2d4", "B3b2", "B1b4", "B3d1", "B2b1", "B3d2", "B3cF", "B1cF", "B3c4", "B3c2", "B1c4", "B3c3", "B2c1", "B3e2", "B1e4", "B2c2", "B3e3", "B1e2", "B3e1", "B1e3", "B1e1", "B2bF", "B3dF", "BCd4", "BCd3", "BCd2", "BCd1", "B2b4", "B2b2", "B3d3", "B2b3", "B3d4", "B1b1", "B2d2", "B1b2", "B2d3", "B2d1", "BCa4", "BCa3", "B3eF", "BCa2", "B2cF", "BCa1", "B1eF", "BCdF", "B2c3", "B3e4", "B2c4")),
            new ArrayList<>(Arrays.asList("A1b0", "A2d2", "A2d0", "A2cF", "A3eF", "A1eF", "ACdF", "A2c4", "A2c2", "A3e3", "A2c3", "A3e4", "A3c0", "A1c2", "A1c0", "A3bF", "A1bF", "A2dF", "A3b4", "A3b2", "A1b4", "A3b3", "A3b0", "A1b2", "A2d3", "A1b3", "A2d4", "A3d0", "A2b0", "A3cF", "A1cF", "A3c3", "A3c4", "A1c3", "A3c2", "A1c4", "A2c0", "A1e3", "A3e2", "A1e4", "A3e0", "A1e2", "A1e0", "A3dF", "A2bF", "ACd4", "ACd2", "ACd3", "ACd0", "A2b3", "A3d4", "A2b4", "A3d2", "A2b2", "A3d3")),
            new ArrayList<>(Arrays.asList("B1c2", "B3e2", "B1e4", "B1c3", "B3e3", "B1e2", "B1e3", "B3bF", "BCb4", "BCb3", "B1bF", "B3dF", "BCb2", "BCd4", "BCd3", "BCd2", "BCcF", "B3b3", "B3b4", "B1b3", "B3d3", "B3b2", "B1b4", "B3d4", "B1b2", "B3d2", "B3cF", "B1cF", "B3eF", "BCc4", "BCc3", "B1eF", "BCc2", "BCbF", "B3c4", "BCdF", "B3c2", "B1c4", "B3e4", "B3c3")),
            new ArrayList<>(Arrays.asList("A1b0", "ACc4", "A1cF", "ACc2", "ACe4", "A1eF", "ACc0", "ACe2", "ACe0", "ACbF", "ACdF", "A1c4", "A1c2", "A1e4", "A1c0", "A1e2", "A1e0", "ACb4", "A1bF", "ACb2", "ACd4", "ACb0", "ACd2", "ACd3", "ACd0", "ACcF", "A1b4", "ACeF", "A1b2")),
            new ArrayList<>(Arrays.asList("BCb4", "BCc4", "BCb2", "BCc3", "BCd4", "BCc2", "BCe4", "BCd2", "BCe2", "BCbF", "BCcF", "BCdF", "BCeF")),
            new ArrayList<>(Arrays.asList("ACe2", "ACbF", "ACb4", "ACcF", "ACb2", "ACc4", "ACeF", "ACc2", "ACe4")),
            new ArrayList<>(Arrays.asList("BCbF", "BCb4", "BCeF", "BCe4")),
            new ArrayList<>(Arrays.asList("ACbF", "ACb2"))));

    public String[][] gameStates = {
            new String[]{"AF0abdd1bbce2bbcd3bcde4bcddCfB1913161418D0000000000", "A0MSFB0MSF"},
            new String[]{"BF1bbce2bbcd3bcde4bcddCaddfB1913161418D0000000000", "A0MS1b1FB0MSF"},
            new String[]{"AF1bbce2bbcd3bcdeCabddddfB1913161418D0000000000", "A0MS1b1FB0MS0c1F"},
            new String[]{"BF1bbce2bbcd3bcdeCaddddB1913161418D0000000000", "A0MS1b2FfB0MS0c1F"},
            new String[]{"AF1bbce2bbcd3bcdeCddddB1913161418D0000000000", "A0MS1b2FfB0MS0c11a1F"},
            new String[]{"BF1bbce3bcdeCbbcddddB1913161418D0000000000", "A0MS1b23d1FfB0MS0c11a1F"},
            new String[]{"AF1bbceCbbbcdddddeB1913161418D0000000000", "A0MS1b23d1FfB0MS0c11a13c1F"},
            new String[]{"BFCbbbccdddddeeB1913161418D0000000000", "A0MS0b11b23d1FbfB0MS0c11a13c1F"},
            new String[]{"AFCbbbcceeB1913161418D0000000000", "A0MS0b11b23d1FbfB0MS0c11a12d33c1Fdd"},
            new String[]{"BFCbbbeeB1913161418D0000000000", "A0MS0b11b23d14c2FbfB0MS0c11a12d33c1Fdd"},
            new String[]{"AFCbbbB1913161418D0000000000", "A0MS0b11b23d14c2FbfB0MS0c11a12d33c14e2Fdd"},
    };
}