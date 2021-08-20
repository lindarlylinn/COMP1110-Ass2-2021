package comp1110.ass2;

import static comp1110.ass2.Tile.*;

public class ExampleFloors {
   static Floor emptyFloor = Floor.initFloor();

   static Floor testFloor0 = new Floor(new Tile[] {Red, null, null, null, null, null, null});
   static Floor testFloor1 = new Floor(new Tile[] {FirstPlayerToken, null, null, null, null, null, null});
   static Floor testFloor2 = new Floor(new Tile[] {Red, FirstPlayerToken, null, null, null, null, null});
   static Floor testFloor3 = new Floor(new Tile[] {Red, Blue, Red, Red, Orange, Green, Purple});
   static Floor testFloor4 = new Floor(new Tile[] {Red, Blue, FirstPlayerToken, Green, null, null, null});

   static Floor[] testFloors = {
           emptyFloor,
           testFloor0,
           testFloor1,
           testFloor2,
           testFloor3,
           testFloor4,
   };

   static String[] testStrings = {
           "",
           "e",
           "f",
           "ef",
           "abcdeee",
           "abef",
   };
}
