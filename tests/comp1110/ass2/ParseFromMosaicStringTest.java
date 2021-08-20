package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class ParseFromMosaicStringTest {

    private String getTypeErrorMessage(boolean expectedType) {
        if (expectedType) {
            return "The mosaic expected to be: basic, but got: variant";
        } else {
            return "The mosaic expected to be: variant, but got: basic";
        }
    }

    /**
     * Test if parsed mosaic girds matches with expected mosaic grids.
     */
    @Test
    public void testParseFromMosaicString() {
        Mosaic[] testMosaics = ExampleMosaics.validTestMosaics;
        String[] testMosaicStrings = ExampleMosaics.validTestStrings;

        for (int i = 0; i < testMosaics.length; i++) {
            String testMosaicString = testMosaicStrings[i];
            // Test mosaic
            Mosaic parsedMosaic = Mosaic.parseFromMosaicString(testMosaicString);
            Tile[][] parsedMosaicGrid = parsedMosaic.mosaicGrids;
            // Expected mosaic
            Mosaic testMosaic = testMosaics[i];
            Tile[][] testMosaicGrid = testMosaic.mosaicGrids;

            assertTrue(Arrays.deepEquals(parsedMosaicGrid, testMosaicGrid),
                    "\nExpected mosaic:\n" + parsedMosaic + "But got:\n" + testMosaic);
        }
    }

    @Test
    public void testSameTypeMosaic() {
        Mosaic[] testMosaics = ExampleMosaics.validTestMosaics;
        String[] testMosaicStrings = ExampleMosaics.validTestStrings;
        for (int i = 0; i < testMosaics.length; i++) {
            String testMosaicString = testMosaicStrings[i];
            // Test mosaic
            Mosaic parsedMosaic = Mosaic.parseFromMosaicString(testMosaicString);
            boolean isParsedMosaicBasic = parsedMosaic.isBasicMosaic();
            // Expected mosaic
            Mosaic testMosaic = testMosaics[i];
            boolean isTestMosaicBasic = testMosaic.isBasicMosaic();

            assertEquals(isParsedMosaicBasic, isTestMosaicBasic, getTypeErrorMessage(isTestMosaicBasic));
        }
    }

    @Test
    public void testInvalidMosaic() {
        String[] testMosaicStrings = ExampleMosaics.invalidTestStrings;

        for (String mosaicString : testMosaicStrings) {
            // Test if a flawed mosaic string can be parsed.
            try {
                Mosaic parsedMosaic = Mosaic.parseFromMosaicString(mosaicString);
                // If it can be parsed: it should be variant mosaic.
                if (parsedMosaic.isBasicMosaic()) {
                    fail("The given mosaic string is valid");
                }
            } catch (Exception ignored) {}
        }
    }
}