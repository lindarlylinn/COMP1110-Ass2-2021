package comp1110.ass2;

import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)


public class ToMosaicStringTest {
    @Test
    public void testtoMosaicString() {
        for (int i = 0; i < ExampleMosaics.validTestMosaics.length; i++) {
            assertEquals(ExampleMosaics.validTestStrings[i], ExampleMosaics.validTestMosaics[i].toMosaicString());
        }
    }
}