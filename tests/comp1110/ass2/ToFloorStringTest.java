package comp1110.ass2;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@org.junit.jupiter.api.Timeout(value = 1000, unit = MILLISECONDS)


public class ToFloorStringTest {
    @Test
    public void testtoFloorString() {
        for (int i = 0; i < ExampleFloors.testFloors.length; i++) {
            assertEquals(ExampleFloors.testStrings[i], ExampleFloors.testFloors[i].toFloorString());
        }
    }
}