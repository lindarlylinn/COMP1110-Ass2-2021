package comp1110.ass2;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static comp1110.ass2.Tile.*;


public class ExampleFactories {
    static List<Object> ListEmpty = Collections.emptyList();
    static List<Tile> List1 = Arrays.asList(
            Red, Blue, Blue, Orange,
            Purple, Green);

    static List<Tile> List2 = Arrays.asList(
            Blue, Green, Red, Blue,
            Blue, Orange, Red, Purple,
            Blue, Orange, Purple, Orange,
            Red, Blue, Blue, Orange,
            Purple, Green, Red, Blue);

    static List<Tile> ListOutOfRange = Arrays.asList(
            Purple, Orange,Red, Blue,
            Blue, Blue, Orange, Purple,
            Orange,Purple, Green, Red,
            Blue, Orange, Red, Purple,
            Red, Blue, Blue, Orange,
            Orange);


    static List[] testFactory = new List[]{
            ListEmpty,
            List1,
            List2,
            ListOutOfRange
    };
    static Tile[][][] resultTile = {
            new Tile[][]{
                    {null,null,null,null},
                    {null,null,null,null},
                    {null,null,null,null},
                    {null,null,null,null},
                    {null,null,null,null}},
            new Tile[][]{
                    {Red, Blue, Blue, Orange},
                    {Purple, Green,null,null},
                    {null,null,null,null},
                    {null,null,null,null},
                    {null,null,null,null}},
            new Tile[][]{
                    {Blue, Green, Red, Blue},
                    {Blue, Orange, Red, Purple},
                    {Blue, Orange, Purple, Orange},
                    {Red, Blue, Blue, Orange},
                    {Purple, Green, Red, Blue}},
            new Tile[][]{
                    {Purple, Orange,Red, Blue},
                    {Blue, Blue, Orange, Purple},
                    {Orange,Purple, Green, Red},
                    {Blue, Orange, Red, Purple},
                    {Red, Blue, Blue, Orange}}
    };

}
