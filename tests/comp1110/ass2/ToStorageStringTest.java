package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)


public class ToStorageStringTest {
    @Test
    public void testtoStorageString() {
        for (int i = 0; i < ExampleStorages.testStorages.length; i++) {
            assertEquals(ExampleStorages.testStrings[i], ExampleStorages.testStorages[i].toStorageString());
        }
    }
}