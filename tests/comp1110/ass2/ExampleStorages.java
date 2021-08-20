package comp1110.ass2;

import static comp1110.ass2.Tile.*;

/**
 * Test Storages
 */
public class ExampleStorages {

    static Storage emptyStorage = Storage.initStorage();

    /**                Untiled test storages               */
    static Storage testStorage1_A = new Storage(new Tile[][] {
            {Purple},
            {Orange, Orange},
            {Green, Green, Green},
            {Red, Red, Red, Red},
            {Red, null, null, null, null}
    });

    static Storage testStorage1_B = new Storage(new Tile[][] {
            {Blue},
            {Green, Green},
            {Purple, Purple, Purple},
            {Orange, Orange, null, null},
            {null, null, null, null, null}
    });

    static Storage testStorage2_A = new Storage(new Tile[][] {
            {null},
            {null, null},
            {Purple, Purple, Purple},
            {Green, Green, Green, Green},
            {Red, Red, Red, Red, Red}
    });

    static Storage testStorage2_B = new Storage(new Tile[][] {
            {Green},
            {Purple, Purple},
            {null, null, null},
            {Orange, Orange, null, null},
            {Blue, Blue, Blue, Blue, Blue}
    });

    static Storage testStorage3_A = new Storage(new Tile[][] {
            {Green},
            {Purple, Purple},
            {Blue, Blue, Blue},
            {Blue, null, null, null},
            {Purple, Purple, null, null, null}
    });

    static Storage testStorage3_B = new Storage(new Tile[][] {
            {null},
            {null, null},
            {Red, Red, Red},
            {Orange, Orange, Orange, Orange},
            {Orange, Orange, Orange, Orange, null}
    });

    static Storage testStorage4_A = new Storage(new Tile[][] {
            {Red},
            {Red, Red},
            {Orange, Orange, Orange},
            {Blue, Blue, Blue, Blue},
            {Purple, Purple, null, null, null}
    });

    //                "1e22c23d34c5",
    static Storage testStorage4_B = new Storage(new Tile[][] {
            {null},
            {Red, Red},
            {Orange, Orange, null},
            {Purple, Purple, Purple, null},
            {Orange, Orange, Orange, Orange, Orange}
    });


    static Storage testStorage5_A = new Storage(new Tile[][] {
            {null},
            {Blue, Blue},
            {Red, Red, null},
            {Orange, Orange, Orange, null},
            {Purple, Purple, Purple, Purple, Purple},
    });

    static Storage testStorage5_B = new Storage(new Tile[][] {
            {null},
            {Blue, Blue},
            {Orange, Orange, Orange},
            {Purple, Purple, Purple, null},
            {Green, Green, Green, Green, Green},
    });

    static Storage testStorage6_A = new Storage(new Tile[][] {
            {Blue},
            {Green, Green},
            {Red, Red, Red},
            {Orange, Orange, Orange, null},
            {null, null, null, null, null},
    });

    static Storage testStorage6_B = new Storage(new Tile[][] {
            {Orange},
            {Orange, Orange},
            {Blue, Blue, Blue},
            {Purple, Purple, Purple, Purple},
            {null, null, null, null, null},
    });


    static Storage[] testStorages = new Storage[] {
            emptyStorage,
            testStorage1_A,
            testStorage1_B,
            testStorage2_A,
            testStorage2_B,
            testStorage3_A,
            testStorage3_B,
            testStorage4_A,
            testStorage4_B,
            testStorage5_A,
            testStorage5_B,
            testStorage6_A,
            testStorage6_B,
    };

    static String[] testStrings = {
            "",
            "0d11c22b33e44e1",
            "0a11b22d33c2",
            "2d33b44e5",
            "0b11d23c24a5",
            "0b11d22a33a14d2",
            "2e33c44c4",
            "0e11e22c33a44d2",
            "1e22c23d34c5",
            "1a22e23c34d5",
            "1a22c33d34b5",
            "0a11b22e33c3",
            "0c11c22a33d4"
    };
}
