package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)


public class ValidTilingMovesTest {
    @Test
    public void testValidTilingMoves() {
       for (int i=0; i<gameStates.length;i++) {
           ArrayList<String> out = Azul.validMovesTiling(gameStates[i]);
           assertEquals(out,validMoves.get(i));
       }
    }

    public ArrayList<ArrayList<String>> validMoves = new ArrayList<>(Arrays.asList(
            new ArrayList<>(Arrays.asList("A00", "A01", "A02", "A03", "A04", "A10", "A11", "A12", "A13", "A14", "A20", "A21", "A22", "A23", "A24")),
            new ArrayList<>(Arrays.asList("A10", "A11", "A12", "A13", "A20", "A21", "A22", "A23")),
            new ArrayList<>(Arrays.asList("A20", "A21", "A23")),
            new ArrayList<>(Arrays.asList("B00", "B01", "B02", "B03", "B04", "B20", "B21", "B22", "B23", "B24")),
            new ArrayList<>(Arrays.asList("B20", "B21", "B22", "B23", "B24")),
            new ArrayList<>(Arrays.asList("B01", "B03", "B04")),
            new ArrayList<>(Arrays.asList("A00", "A01", "A02", "A03", "A10", "A11", "A13", "A14", "A20", "A22", "A23", "A24")),
            new ArrayList<>(Arrays.asList("A10", "A11", "A13", "A14", "A20", "A22", "A23", "A24")),
            new ArrayList<>(Arrays.asList("A20", "A22", "A23", "A24"))));

    public String[][] gameStates = {
            new String[]{"AFCB1913161418D0000000000", "A0MS0b11b22b33d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"AFCB1913161418D0000000000", "A1Mb04S1b22b33d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"AFCB1914161418D0000000000", "A2Mb04b12S2b33d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"BFCB1916161418D0000000000", "A3Mb04b12b21S3d14c2FbfB0MS0c11a12d33c14e2Fdd"},
            new String[]{"BFCB1916161418D0000000000", "A3Mb04b12b21S3d14c2FbfB1Mc02S1a12d33c14e2Fdd"},
            new String[]{"BFCB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d34c4FfB0Mc02d20S0d11a12e23c34e3Fbbbb"},
            new String[]{"AFCB1612111313D0001000200", "A1Mb04b12b21S0c11e22a33d34c4FfB2Md01c02d20S1a12e23c34e3Fbbbb"},
            new String[]{"AFCB1612111313D0001000200", "A3Mc03b04b12b21S1e22a33d34c4FfB2Md01c02d20S1a12e23c34e3Fbbbb"},
            new String[]{"AFCB1612111314D0001000200", "A7Mc03b04b12e13b21S2a33d34c4FfB2Md01c02d20S1a12e23c34e3Fbbbb"}
    };
}