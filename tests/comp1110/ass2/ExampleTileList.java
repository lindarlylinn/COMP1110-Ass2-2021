package comp1110.ass2;

import java.util.Arrays;
import java.util.List;

import static comp1110.ass2.Tile.*;

class ExampleTileList {
    static List ListEmpty = Arrays.asList();
    static List List1 = Arrays.asList(Blue, Green);
    static List List2 = Arrays.asList(Red, Blue, Blue, Orange);
    static List ListOutOfRange = Arrays.asList(Purple, Orange,Red, Blue, Blue);


    static List[] testFactory = new List[]{
            ListEmpty,
            List1,
            List2,
            ListOutOfRange
    };
    static Tile[][] resultTile = {
            new Tile[]{null,null,null,null},
            new Tile[]{Blue,Green,null,null},
            new Tile[]{Red, Blue, Blue, Orange},
    };
}
