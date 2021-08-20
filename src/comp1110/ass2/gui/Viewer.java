package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class Viewer extends Application {

    private static final int VIEWER_WIDTH = 1200;
    private static final int VIEWER_HEIGHT = 700;

    // Set normal size of tiles.
    private final double TILE_SIZE = 40;
    private final double TILE_GAP = TILE_SIZE / 20;

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group boardView = new Group();
    private TextField playerTextField;
    private TextField boardTextField;


    /**
     * Draw a placement in the window, removing any previously drawn placements
     *
     * @param state an array of two strings, representing the current game state
     *              TASK 4
     */
    void displayState(String[] state) {
        // FIXED Task 4: implement the simple state viewer
        // Clear the previous canvas.
        boardView.getChildren().clear();
        root.getChildren().remove(boardView);

        String[] gameState = new String[] {state[1], state[0]};

        // Try to convert board state to game board.
        try {
            GameBoard gameBoard = GameBoard.parseFromGameState(gameState);
            Group gameBoardView = getGameBoardView(gameBoard);
            boardView.getChildren().add(gameBoardView);
        // If a game state cannot be convert to game board: show error message.
        } catch (Exception e) {
            TextFlow errorView = new TextFlow();
            /* We want to show: Error!
             *     The given game state is not valid.
             *
             *             Error message:
             * Invalid xxx xxx xxx xxx xxx xxx xxx xxx .
             *
             * * Part 1: "Error!"
             * * Part 2: "The given game state is not valid."
             * * Part 3: "Error message"
             * * Part 4: show the error message.
             */
            // Part 1:
            Text error = new Text("Error!");
            error.setFill(Color.CRIMSON);
            error.setFont(Font.font("Futura", 40));
            errorView.getChildren().add(error);
            errorView.getChildren().add(new Text(System.lineSeparator()));

            // Part 2:
            Text notValid = new Text("The given game state is not valid.");
            notValid.setFont(Font.font("Futura", 35));
            errorView.getChildren().add(notValid);
            errorView.getChildren().add(new Text(System.lineSeparator()));
            errorView.getChildren().add(new Text(System.lineSeparator()));
            errorView.getChildren().add(new Text(System.lineSeparator()));
            errorView.getChildren().add(new Text(System.lineSeparator()));

            // Part 3:
            Text errorMessage = new Text("Error message:");
            errorMessage.setFont(Font.font("Monaco"));
            errorView.getChildren().add(errorMessage);
            errorView.getChildren().add(new Text(System.lineSeparator()));

            // Part 4:
            Text actualErrorMessage = new Text(e.toString());
            actualErrorMessage.setFont(Font.font("Monaco"));
            errorView.getChildren().add(actualErrorMessage);

            // Set text alignment and it's position.
            errorView.setTextAlignment(TextAlignment.CENTER);
            errorView.setTranslateX((double) VIEWER_WIDTH / 4 + e.toString().length());
            errorView.setTranslateY((double) VIEWER_HEIGHT / 2 - 100);
            boardView.getChildren().add(errorView);
        }
        root.getChildren().add(boardView);
    }


    /**
     * Get the view of game board.
     *
     * @param gameBoard A game board
     * @return A view of game board
     */
    Group getGameBoardView(GameBoard gameBoard) {
        Group gameBoardView = new Group();
        // Set background
        // Background of player boards.
        Rectangle playerBoard0Area = new Rectangle(0.36 * VIEWER_WIDTH, 0.54 * VIEWER_HEIGHT, Color.LIGHTYELLOW);
        playerBoard0Area.setTranslateX(0.025 * VIEWER_WIDTH);
        playerBoard0Area.setTranslateY(0.38 * VIEWER_HEIGHT);
        Rectangle playerBoard1Area = new Rectangle(0.36 * VIEWER_WIDTH, 0.54 * VIEWER_HEIGHT, Color.LIGHTYELLOW);
        playerBoard1Area.setTranslateX((0.043 + 0.36) * VIEWER_WIDTH);
        playerBoard1Area.setTranslateY(0.38 * VIEWER_HEIGHT);
        gameBoardView.getChildren().addAll(playerBoard0Area, playerBoard1Area);

        // Background of factories.
        Rectangle factoriesArea = new Rectangle(0.43 * VIEWER_WIDTH, 0.3 * VIEWER_HEIGHT, Color.GAINSBORO);
        factoriesArea.setTranslateX(0.025 * VIEWER_WIDTH);
        factoriesArea.setTranslateY(0.035 * VIEWER_HEIGHT);
        gameBoardView.getChildren().add(factoriesArea);

        // Background of centre.
        Rectangle centreArea = new Rectangle(0.3 * VIEWER_WIDTH, 0.3 * VIEWER_HEIGHT, Color.LAVENDER);
        centreArea.setTranslateX(0.46 * VIEWER_WIDTH);
        centreArea.setTranslateY(0.035 * VIEWER_HEIGHT);
        gameBoardView.getChildren().add(centreArea);

        // Background of bag.
        Rectangle bagArea = new Rectangle(0.2 * VIEWER_WIDTH, 0.4 * VIEWER_HEIGHT, Color.CORNSILK);
        bagArea.setTranslateX(0.785 * VIEWER_WIDTH);
        bagArea.setTranslateY(0.035 * VIEWER_HEIGHT);
        gameBoardView.getChildren().add(bagArea);

        // Background of discard.
        Rectangle discardArea = new Rectangle(0.2 * VIEWER_WIDTH, 0.4 * VIEWER_HEIGHT, Color.CORNSILK);
        discardArea.setTranslateX(0.785 * VIEWER_WIDTH);
        discardArea.setTranslateY(0.52 * VIEWER_HEIGHT);
        gameBoardView.getChildren().add(discardArea);


        // Show player in next round
        Player currPlayer = gameBoard.getTurn();
        TextFlow currPlayerView = new TextFlow();
        /* We want to show: Current Player: Player X
         * * Part 1: "Current Player: Player"
         * * Part 2: X
         */
        Text part1AtCurrPlayerView = new Text("Current Player: Player ");
        part1AtCurrPlayerView.setFont(Font.font("Futura"));
        Text part2AtCurrPlayerView = new Text(Player.getIndexOfPlayerStr(currPlayer));
        part2AtCurrPlayerView.setFont(Font.font("Futura"));
        part2AtCurrPlayerView.setFill(Color.CORAL);

        currPlayerView.getChildren().addAll(part1AtCurrPlayerView, part2AtCurrPlayerView);
        currPlayerView.setTranslateX(0.34 * VIEWER_WIDTH);
        currPlayerView.setTranslateY(0.35 * VIEWER_HEIGHT);

        gameBoardView.getChildren().add(currPlayerView);

        // Show Factories.
        Tile[][] factories = gameBoard.getFactories();
        Group factoriesView = getFactoriesView(factories);
        factoriesView.setScaleX(0.9);
        factoriesView.setScaleY(0.9);
        factoriesView.setTranslateX(40);
        factoriesView.setTranslateY(40);
        gameBoardView.getChildren().add(factoriesView);

        // Show Centre
        ArrayList<Tile> centre = gameBoard.getCentre();
        Group centreView = getCentreView(centre);
        centreView.setScaleX(0.9);
        centreView.setScaleY(0.9);
        centreView.setTranslateX(0.75 * VIEWER_WIDTH - (TILE_SIZE + TILE_GAP) * 8);
        centreView.setTranslateY(40);
        gameBoardView.getChildren().add(centreView);

        // Show Bag
        ArrayList<Tile> bag = gameBoard.getBag();
        Group bagView = getStatisticalView(bag, "bag",
                0.8 * (TILE_SIZE + TILE_GAP)
        );
        bagView.setScaleX(0.9);
        bagView.setScaleY(0.9);
        bagView.setTranslateX(0.83 * VIEWER_WIDTH);
        bagView.setTranslateY(40);
        gameBoardView.getChildren().add(bagView);

        // Show Discard
        ArrayList<Tile> discard = gameBoard.getDiscard();
        Group discardView = getStatisticalView(discard, "discard",
                0.3 * (TILE_SIZE + TILE_GAP)
        );
        discardView.setScaleX(0.9);
        discardView.setScaleY(0.9);
        discardView.setTranslateX(0.83 * VIEWER_WIDTH);
        discardView.setTranslateY(40 + (TILE_SIZE + TILE_GAP) * 8);
        gameBoardView.getChildren().add(discardView);


        // Show playerBoards.
        PlayerBoard[] playerBoards = gameBoard.getPlayerBoards();
        Group playerBoardsView = getPlayerBoardsView(playerBoards);
        playerBoardsView.setScaleX(0.9);
        playerBoardsView.setScaleY(0.9);
        playerBoardsView.setTranslateX(-10);
        playerBoardsView.setTranslateY(270);
        gameBoardView.getChildren().add(playerBoardsView);

        return gameBoardView;
    }


    /**
     * Get the view of factories.
     *
     * @param factories A list of factories
     * @return A view of factories
     */
    Group getFactoriesView(Tile[][] factories) {
        Group factoriesView = new Group();

        // Iterate through every factories.
        for (int i = 0; i < factories.length; i++) {
            // Get factory by index.
            Tile[] currFactory = factories[i];

            // Visualize current factory.
            Group currFactoryView = getFactoryView(currFactory);
            currFactoryView.setTranslateX(2.5 * (TILE_SIZE + TILE_GAP) * i);
            factoriesView.getChildren().add(currFactoryView);

            // Show factory index.
            TextFlow factoryInfo = new TextFlow();
            /* We want to show: Factory N
             * * Part 1: "Factory "
             * * Part 2: "N" */
            Text part0AtFactoriesView = new Text("Factory ");
            part0AtFactoriesView.setFont(Font.font("American Typewriter", 15));
            Text part1AtFactoriesView = new Text(String.valueOf(i + 1));
            part1AtFactoriesView.setFont(Font.font("American Typewriter", 15));
            factoryInfo.getChildren().addAll(part0AtFactoriesView, part1AtFactoriesView);
            factoryInfo.setTranslateX(2.5 * i * (TILE_SIZE + TILE_GAP) + 7);
            factoryInfo.setTranslateY((TILE_SIZE + TILE_GAP) * 2.1);

            factoriesView.getChildren().add(factoryInfo);

            // Show factory label.
            Text factoriesLabel = new Text("Factories");
            factoriesLabel.setFont(Font.font("Futura", 30));
            factoriesLabel.setTranslateX(4.5 * (TILE_SIZE + TILE_GAP));
            factoriesLabel.setTranslateY(4.2 * (TILE_SIZE + TILE_GAP));
            factoriesView.getChildren().add(factoriesLabel);
        }

        return factoriesView;
    }


    /**
     * Get the view of a factory.
     *
     * @param factory A factory
     * @return View of a single factory
     */
    Group getFactoryView(Tile[] factory) {
        Group factoryView = new Group();
        // Iterate through every space in factory.
        for (int i = 0; i < 4; i++) {
            // Get factory by index.
            Tile currTile = factory[i];

            // Get view of a tile in factory.
            Rectangle tileView;
            if (currTile == null) {
                tileView = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREY);
            } else {
                tileView = getTileView(currTile);
            }

            // Show factories in first line.
            if (i < 2) {
                tileView.setX((TILE_SIZE + TILE_GAP) * i);
                tileView.setY(0);
            // Show factories in second line.
            } else {
                tileView.setX((TILE_SIZE + TILE_GAP) * (i - 2));
                tileView.setY(TILE_SIZE + TILE_GAP);
            }

            factoryView.getChildren().add(tileView);
        }

        return factoryView;
    }


    /**
     * Get the view of a centre.
     *
     * @param centre A list of tiles that represents centre
     * @return A view of centre
     */
    Group getCentreView(ArrayList<Tile> centre) {
        Group centreView = new Group();

        // Initialize x and y coordinate of tiles in centre.
        double x = 0;
        double y = 0;
        for (int i = 0; i < centre.size(); i++) {
            // Get tile in ith position.
            Tile currTile = centre.get(i);
            Rectangle tileView = getTileView(currTile);

            // Display 8 tiles every row.
            if (i % 8 == 0 && i != 0) {
                x = 0;
                y += TILE_SIZE + TILE_GAP;
            }
            // Set tiles' position in factory.
            tileView.setX(x);
            tileView.setY(y);
            // Set the position of next tile.
            x += TILE_SIZE + TILE_GAP;

            centreView.getChildren().add(tileView);

        }
        // Show factory label.
        Text centreLabel = new Text("Centre");
        centreLabel.setFont(Font.font("Futura", 30));
        centreLabel.setTranslateX(3.2 * (TILE_SIZE + TILE_GAP));
        centreLabel.setTranslateY(4.2 * (TILE_SIZE + TILE_GAP));
        centreView.getChildren().add(centreLabel);

        return centreView;
    }


    /**
     * Get a view of bag or discard.
     *
     * @param tiles A list of tiles that represents bag or discard
     * @param label Name of label
     * @param labelX X coordinate of label
     * @return A view of bag or label
     */
    Group getStatisticalView(ArrayList<Tile> tiles, String label, double labelX) {
        Group statisticalView = new Group();

        // Get the statistical data of tiles.
        int[] tilesStatistic = Tile.getTilesStatistic(tiles);
        Tile[] colouredTiles = Tile.getAllColouredTiles();

        // Iterate through every tile:
        for (int i = 0; i < 5; i++) {
            TextFlow tileInfo = new TextFlow();
            /* We want to show: N █ tiles.
             * Part1: "N"
             * Part2: █
             * Part 3:" tiles" */
            // Get number of tiles.
            int numOfTiles = tilesStatistic[i];
            // Part 1:
            Text part1AtStatView;
            if (numOfTiles < 10) {
                part1AtStatView = new Text("  " + numOfTiles);
            } else {
                part1AtStatView = new Text(String.valueOf(numOfTiles));
            }
            part1AtStatView.setFont(Font.font("American Typewriter", 25));

            // Part 3:
            Text part3AtStatView;
            if (numOfTiles == 0 || numOfTiles == 1) {
                part3AtStatView = new Text("         tile");
            } else {
                part3AtStatView = new Text("         tiles");
            }
            part3AtStatView.setFont(Font.font("American Typewriter", 25));

            // Set the position of each data.
            tileInfo.getChildren().addAll(part1AtStatView, part3AtStatView);
            tileInfo.setTranslateY((TILE_SIZE + TILE_GAP) * i + 5);
            statisticalView.getChildren().add(tileInfo);

            // Place tile at the centre of two strings.
            Tile currTile = colouredTiles[i];
            Rectangle tileView = getTileView(currTile);
            tileView.setX(37);
            tileView.setY((TILE_SIZE + TILE_GAP) * i);
            statisticalView.getChildren().add(tileView);
        }

        // Show bag or factory label.
        Text currLabel = new Text(label);
        currLabel.setFont(Font.font("Futura", 30));
        currLabel.setTranslateX(labelX);
        currLabel.setTranslateY(252.0);
        statisticalView.getChildren().add(currLabel);

        return statisticalView;
    }

    /**
     * Get the view of player boards. (Can only view 2 players now).
     *
     * @param playerBoards A list of player board
     * @return View of player boards
     */
    Group getPlayerBoardsView(PlayerBoard[] playerBoards) {
        Group playerBoardsView = new Group();

        // Set playerBoard 0.
        PlayerBoard playerBoard0 = playerBoards[0];
        Group playerBoard0View = getPlayerBoardView(playerBoard0);

        // Set playerBoard 1.
        PlayerBoard playerBoard1 = playerBoards[1];
        Group playerBoard1View = getPlayerBoardView(playerBoard1);
        playerBoard1View.setTranslateX((TILE_SIZE + TILE_GAP) * 12);

        playerBoardsView.getChildren().addAll(playerBoard0View, playerBoard1View);

        return playerBoardsView;
    }


    /**
     * A view of a single player board.
     *
     * @param playerBoard A player board
     * @return A view of a player board
     */
    Group getPlayerBoardView(PlayerBoard playerBoard) {
        Group playerBoardView = new Group();
        double storageMosaicDistance = (TILE_SIZE + TILE_GAP) * 6;

        // Get player of player board.
        Player player = playerBoard.getPlayer();
        // Show player of player board.
        TextFlow playerView = new TextFlow();
        /* We want to have: Player X's player board
         * * Part 1: "Player "
         * * Part 2: "X"
         * * Part 3: "'s player board" */
        // Set uniform font and font size.
        double fontSizeAtPlayerView = 25;
        String fontAtPlayerView = "Futura";
        // Part 1:
        Text part1AtPlayerView = new Text("Player ");
        part1AtPlayerView.setFont(Font.font(fontAtPlayerView, fontSizeAtPlayerView));
        // Part 2:
        Text part2AtPlayerView = new Text(Player.getIndexOfPlayerStr(player));
        part2AtPlayerView.setFill(Color.CORAL);
        part2AtPlayerView.setFont(Font.font(fontAtPlayerView, FontWeight.BOLD, fontSizeAtPlayerView));
        // Part 3:
        Text part3AtPlayerView = new Text("'s player board");
        part3AtPlayerView.setFont(Font.font(fontAtPlayerView, fontSizeAtPlayerView));
        // Merge part1, part2 and part3 together.
        playerView.getChildren().addAll(part1AtPlayerView, part2AtPlayerView, part3AtPlayerView);
        // Adjust the position.
        playerView.setTranslateX(55);
        // Add to playerBoardView.
        playerBoardView.getChildren().add(playerView);

        // Get score of player board.
        int score = playerBoard.getScore();
        // Show score of player board.
        TextFlow scoreView = new TextFlow();
        /* We want to have: Player X's player board
         * * Part 1: "Score: "
         * * Part 2: "X" */
        // Set uniform font and font size.
        double fontSizeAtScoreView = 20;
        String fontAtScoreView = "American Typewriter";
        // Part 1
        Text part1AtScoreView = new Text("Score: ");
        part1AtScoreView.setFont(Font.font(fontAtScoreView, fontSizeAtScoreView));
        // Part 2
        Text part2AtScoreView = new Text(String.valueOf(score));
        part2AtScoreView.setFill(Color.CORAL);
        part2AtScoreView.setFont(Font.font(fontAtScoreView, fontSizeAtScoreView));
        // Merge part 1 and part 2 together.
        scoreView.getChildren().addAll(part1AtScoreView, part2AtScoreView);
        // Adjust the position.
        scoreView. setTranslateY(60);
        // Add to playerBoardView.
        playerBoardView.getChildren().add(scoreView);

        // Get storage of player board.
        Storage storage = playerBoard.getStorage();
        // Show storage of player board.
        Group storageView = getStorageView(storage);
        storageView.setTranslateY(60);
        playerBoardView.getChildren().add(storageView);

        // Get mosaic of player board.
        Mosaic mosaic = playerBoard.getMosaic();
        // Show mosaic of player board.
        Group mosaicView = getMosaicView(mosaic);
        mosaicView.setTranslateX(storageMosaicDistance);
        mosaicView.setTranslateY(60);
        playerBoardView.getChildren().add(mosaicView);

        // Get floor of player board.
        Tile[] floor = playerBoard.getTilesOnFloor();
        Group floorView = getFloorView(floor);
        floorView.setTranslateY(60 + (TILE_SIZE + TILE_GAP) * 5.5);
        playerBoardView.getChildren().add(floorView);

        return playerBoardView;
    }


    /**
     * Get the view of a mosaic.
     *
     * @param mosaic A mosaic
     * @return A view of a mosaic
     */
    Group getMosaicView(Mosaic mosaic) {
        Group mosaicView = new Group();
        // Initialize the position of tiles.
        double x = 0;
        double y = 0;
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                // Get tile in mosaic by row and column.
                Tile currTile = mosaic.getTileByRowAndCol(row, col);
                Rectangle tileView;
                // If tile is null, draw grey tile.
                if (currTile == null) {
                    tileView = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREY);
                // Otherwise: draw its corresponding tile.
                } else {
                    tileView = getTileView(currTile);
                }
                // Set the position of tile.
                tileView.setX(x);
                tileView.setY(y);
                mosaicView.getChildren().add(tileView);

                // Set the position for next tiles.
                x += TILE_SIZE + TILE_GAP;
            }
            // Set the position for tiles in next row.
            y += TILE_SIZE + TILE_GAP;
            x = 0;
        }
        return mosaicView;
    }


    /**
     * Get the view of a storage.
     *
     * @param storage A storage
     * @return A view of a storage
     */
    Group getStorageView(Storage storage) {
        Group storageView = new Group();

        // Iterate through every tile in the storage.
        for (int i = 0; i < 5; i++) {
            Tile[] row = storage.getRowByRowIndex(i);
            for (int j = 0; j < row.length; j++) {
                Tile currTile = row[j];

                Rectangle tileView;
                // If there is no tile at certain position:
                if (currTile == null) {
                    // Set the tiles grey.
                    tileView = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREY);
                // Otherwise, set the tiles to it's corresponding colour.
                } else {
                    tileView = getTileView(currTile);
                }
                // Set the position of tile.
                tileView.setX((TILE_SIZE + TILE_GAP) * (4 - j));
                tileView.setY((TILE_SIZE + TILE_GAP) * i);

                storageView.getChildren().add(tileView);
            }
        }

        return storageView;
    }


    /**
     * Get a view of a floor.
     *
     * @param tilesOnFloor An array of array of tiles that represents floor
     * @return A view of floor
     *         * First row represents the first 7 tiles,
     *         * Second row represents the rest of tiles (if there exists)
     */
    Group getFloorView(Tile[] tilesOnFloor) {
        Group floorView = new Group();

        for (int i = 0; i < 7; i++) {
            Tile currTile = tilesOnFloor[i];
            Rectangle tileView;
            // If there is no tile at certain position:
            if (currTile == null) {
                // Set the tiles grey.
                tileView = new Rectangle(TILE_SIZE, TILE_SIZE, Color.GREY);
            // Otherwise, set the tiles to it's corresponding colour.
            } else {
                tileView = getTileView(currTile);
            }
            // Set the position of tile.
            tileView.setX((TILE_SIZE + TILE_GAP) * i);

            floorView.getChildren().add(tileView);
        }
        return floorView;
    }


    /**
     * Get a view of a tile.
     *
     * @param tile A tile (not including null)
     * @return A view of a tile
     */
    Rectangle getTileView(Tile tile) {
        // Set size of tiles.
        Rectangle rectangle = new Rectangle(TILE_SIZE, TILE_SIZE);
        switch (tile) {
            case Blue -> rectangle.setFill(Color.BLUE);
            case Green -> rectangle.setFill(Color.GREEN);
            case Orange -> rectangle.setFill(Color.ORANGE);
            case Purple -> rectangle.setFill(Color.PURPLE);
            case Red -> rectangle.setFill(Color.RED);
            case FirstPlayerToken -> rectangle.setFill(Color.LIGHTBLUE);
        }
        return rectangle;
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label playerLabel = new Label("Player State:");
        playerTextField = new TextField();
        playerTextField.setPrefWidth(100);
        Label boardLabel = new Label("Board State:");
        boardTextField = new TextField();
        boardTextField.setPrefWidth(100);
        Button button = new Button("Refresh");
        button.setOnAction(e -> displayState(new String[]{playerTextField.getText(),
                boardTextField.getText()}));
        HBox hb = new HBox();
        hb.getChildren().addAll(playerLabel, playerTextField, boardLabel,
                boardTextField, button);
        hb.setSpacing(10);
        hb.setLayoutX(50);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Azul Viewer");

        // Add icon to viewer.
        String GameIconURI = Objects.requireNonNull(Game.class.getResource("assets/game-icon.png")).toString();

        primaryStage.getIcons().add(new Image(GameIconURI));

        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(controls);

        makeControls();

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


