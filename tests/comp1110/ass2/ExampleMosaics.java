package comp1110.ass2;

import static comp1110.ass2.Tile.*;

/**
 * Test Mosaics
 */
public class ExampleMosaics {
    /**                Basic Mosaic                    */
    static Mosaic emptyBasicMosaic = Mosaic.initBasicMosaic();

    // Full basic mosaic
    static Tile[][] completeBasicGameGrid = new Tile[][]{
            {Blue, Green, Orange, Purple, Red},
            {Red, Blue, Green, Orange, Purple},
            {Purple, Red, Blue, Green, Orange},
            {Orange, Purple, Red, Blue, Green},
            {Green, Orange, Purple, Red, Blue}};
    static Mosaic completeBasicMosaic = new Mosaic(true, completeBasicGameGrid);

    /**                  Incomplete game                */
    static Tile[][] testMosaicTiles1_A = new Tile[][] {
            {null, null, null, Purple, null},
            {null, null, null, Orange, null},
            {null, null, null, Green, null},
            {null, null, Red, null, null},
            {null, null, null, null, null}
    };
    static Mosaic testMosaic1_A = new Mosaic(true, testMosaicTiles1_A);

    static Tile[][] testMosaicTiles1_B = new Tile[][] {
            {Blue, null, null, null, null},
            {null, null, Green, null, null},
            {Purple, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
    };
    static Mosaic testMosaic1_B = new Mosaic(true, testMosaicTiles1_B);


    /**                    Complete game                */
    // Basic Mosaic with 1 complete row
    static Tile[][] completeBasicGameGrid0 = new Tile[][]{
            {null, null, null, null, null},
            {null, null, null, null, null},
            {Purple, Red, Blue, Green, Orange},
            {null, null, null, null, null},
            {null, null, null, null, null}};
    static Mosaic completeBasicMosaic0 = new Mosaic(true, completeBasicGameGrid0);

    // Basic Mosaic with 2 complete rows
    static Tile[][] completeBasicGameGrid1 = new Tile[][]{
            {Blue,   null,   Orange, Purple, null},
            {Red,    Blue,   Green,  Orange, Purple},
            {null,   Red,    Blue,   null,   Orange},
            {Orange, Purple, null,   Blue,   Green},
            {Green,  Orange, Purple, Red,    Blue}};
    static Mosaic completeBasicMosaic1 = new Mosaic(true, completeBasicGameGrid1);

    // Basic Mosaic with 1 complete row, 1 complete column
    static Tile[][] completeBasicGameGrid2 = new Tile[][]{
            {null, Green, null, null, null},
            {null, Blue, null, null, null},
            {null, Red, null, null, null},
            {Orange, Purple, Red, Blue, Green},
            {null, Orange, null, null, null}};
    static Mosaic completeBasicMosaic2 = new Mosaic(true, completeBasicGameGrid2);

    // Basic Mosaic with 1 complete row, 3 complete column
    static Tile[][] completeBasicGameGrid3 = new Tile[][]{
            {Blue, null, Orange, null, Red},
            {Red, null, null, Orange, Purple},
            {Purple, null, Blue, Green, Orange},
            {Orange, Purple, Red, Blue, Green},
            {Green, null, Purple, null, Blue}};
    static Mosaic completeBasicMosaic3 = new Mosaic(true, completeBasicGameGrid3);

    // Basic Mosaic with 3 complete rows, 3 complete columns
    static Tile[][] completeBasicGameGrid4 = new Tile[][]{
            {Blue,   null,   Orange,  null,   Red},
            {Red,    Blue,   Green,   Orange, Purple},
            {Purple, Red,    Blue,    Green,  Orange},
            {Orange, Purple, Red,     Blue,   Green},
            {Green,  null,   Purple,  null,   Blue}};
    static Mosaic completeBasicMosaic4 = new Mosaic(true, completeBasicGameGrid4);



    /**                  Variant Mosaic                    */
    static Tile[][] completeMosaicGridVariant = new Tile[][]{
            {Purple, Red, Blue, Green, Orange},
            {Red, Blue, Green, Orange, Purple},
            {Orange, Purple, Red, Blue, Green},
            {Blue, Green, Orange, Purple, Red},
            {Green, Orange, Purple, Red, Blue}};
    static Mosaic completeVariantMosaic = new Mosaic(false, completeMosaicGridVariant);


    static Tile[][] mismatchingBasicMosaicGrid = new Tile[][] {
            // Correct placement should be {Blue, Green, Orange, null, null}.
            {Blue, Orange, Green, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null},
            {null, null, null, null, null}
    };
    static Mosaic matchingVariantMosaic = new Mosaic(false, mismatchingBasicMosaicGrid);


    static Mosaic[] validTestMosaics = {
            emptyBasicMosaic,
            completeBasicMosaic,
            testMosaic1_A,
            testMosaic1_B,
            completeBasicMosaic0,
            completeBasicMosaic1,
            completeBasicMosaic2,
            completeBasicMosaic3,
            completeBasicMosaic4,

            completeVariantMosaic,
            matchingVariantMosaic,
    };

    static String[] validTestStrings = {
            "",
            "a00b01c02d03e04e10a11b12c13d14d20e21a22b23c24c30d31e32a33b34b40c41d42e43a44",
            "d03c13b23e32",
            "a00b12d20",
            "d20e21a22b23c24",
            "a00c02d03e10a11b12c13d14e21a22c24c30d31a33b34b40c41d42e43a44",
            "b01a11e21c30d31e32a33b34c41",
            "a00c02e04e10c13d14d20a22b23c24c30d31e32a33b34b40d42a44",
            "a00c02e04e10a11b12c13d14d20e21a22b23c24c30d31e32a33b34b40d42a44",

            "d00e01a02b03c04e10a11b12c13d14c20d21e22a23b24a30b31c32d33e34b40c41d42e43a44",
            "a00c01b02",
    };

    static String[] invalidTestStrings = {
            "c12c13",
            "e04e14",
            "a00c01b02"
    };
}
