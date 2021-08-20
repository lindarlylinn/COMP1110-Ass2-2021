package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.*;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)
public class ParseFromStorageStringTest {

    @Test
    public void testParseFromStorageString() {
        Storage[] testStorages = ExampleStorages.testStorages;
        String[] testStorageStrings = ExampleStorages.testStrings;
        for (int i = 0; i < testStorages.length; i++) {
            String testStorageString = testStorageStrings[i];
            Storage parsedStorage = Storage.parseFromStorageString(testStorageString);
            Tile[][] parsedStorageString = parsedStorage.getStorageRows();
            Storage targetStorage = testStorages[i];
            Tile[][] targetStorageString = targetStorage.getStorageRows();

            assertTrue(Arrays.deepEquals(parsedStorageString, targetStorageString),
                    "\nExpected storage:\n" + parsedStorage + "But got:\n" + targetStorage);
        }
    }

}