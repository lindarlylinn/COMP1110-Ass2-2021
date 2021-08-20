package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

/**
 * @author Qm9nb25nIFdhbmc=
 */
public class Game extends Application {
    /* board layout */
    private static final int BOARD_WIDTH = 1280;
    private static final int BOARD_HEIGHT = 768;

    // Size of tiles.
    private static final double TILE_SIZE = 54;
    // Size of score marker
    private static final double SCORE_MARKER_SIZE = 29;
    // Gap between tiles.
    private static final double TILE_GAP_STORAGE_MOSAIC = 5.2;
    private static final double TILE_GAP_FLOOR = 10;
    private static final double TILE_GAP_FACTORY = 3;
    // Gap between score
    private static final double SCORE_MARKER_X_GAP = 1.35;
    private static final double SCORE_MARKER_Y_GAP = 6.3;
    // Position of each part in player board.
    private static final double FIRST_COL_X_STORAGE = 287;
    private static final double FIRST_COL_X_MOSAIC = 374;
    private static final double FIRST_ROW_STORAGE_MOSAIC_Y = 287;
    private static final double FLOOR_X = 50;
    private static final double FLOOR_Y = 627;
    private static final double SCORE_MARKER_X = 55.5;
    private static final double SCORE_MARKER_Y = 55;


    private static final double TILE_COUNT_OFFSET_X = 10;
    private static final double TILE_COUNT_OFFSET_Y = 60;
    private static int[][] FACTORY_POSITIONS;
    private static int[][] CENTRE_POSITIONS;

    // Define a drop shadow effect at right bottom
    private static final DropShadow SHADOW;
    static {
        SHADOW = new DropShadow();
        SHADOW.setOffsetY(2.0);
        SHADOW.setRadius(20);
        SHADOW.setColor(Color.color(0, 0, 0, 1));
    }

    /* node groups */
    Group root = new Group();
    Group CentreArea = new Group();
    Group FactoriesArea = new Group();
    Group StorageArea = new Group();
    Group MosaicArea = new Group();
    Group FloorArea = new Group();
    Group ScoreArea = new Group();
    Group SwitchButton = new Group();
    Group PlayerBoard = new Group();
    Group TemporaryBoard = new Group();

    Group WelcomePage = new Group();
    Group WelcomePageTemporary = new Group();


    /* where to find media assets */
    private static final String URI_BASE = "assets/";
    private final String FONT= Objects.requireNonNull(
            Game.class.getResource(URI_BASE + "Algerian-Regular.ttf")).toExternalForm();

    /* Control of the game */
    static double VOLUME = 1;
    static int ANIMATION_SPEED = 1;

    GameBoard GAME_BOARD;
    Player PlayerInNextRound = Player.PLAYER1;
    /* The player is ordered by player index (i.e. Player1, Player2 ... )
     * Key represents the name of player.
     * Value represents the type of player
     *      0 - Human
     *      1 - Basic AI
     *      2 - Advanced AI
     */
    LinkedHashMap<Player, Integer> PlayerTypesMap = new LinkedHashMap<>();






    /**
     * Graphical tile view.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    static class GraphicalTile extends ImageView {
        Tile tile;

        GraphicalTile(Tile tile) {
            this.tile = tile;
            // Set the view of tile
            setImage(new Image(getGraphicalTileURI(tile)));
            setFitHeight(TILE_SIZE);
            setFitWidth(TILE_SIZE);
            setEffect(SHADOW);
        }
    }


    /**
     * Draggable graphical tile view.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    class DraggableTile extends GraphicalTile {
        // The origin position of a tile.
        double originX, originY;
        // The dragged position;
        double mouseX, mouseY;

        /* Type of a tile. Describes the current status.
         * Type 0: Locked, cannot move.
         * Type 1: In drafting move (drag from factories).
         * Type 2: In drafting move (drag from centre).
         * Type 3: In tiling move.
         */
        int type;

        DraggableTile(Tile tile, double originX, double originY, int type) {
            super(tile);

            // Set position.
            this.originX = originX;
            this.originY = originY;
            setLayoutX(originX);
            setLayoutY(originY);

            // Set type.
            this.type = type;
            ColorAdjust selectedShadowEffect = new ColorAdjust();
            selectedShadowEffect.setBrightness(0.3);
            switch (type) {
                case 0 -> {
                }
                // Tiles drag from factories to storage (or floor).
                case 1 -> {
                    int factoryIndex = getClickedFactoryGrid(getLayoutX(), getLayoutY())[0];
                    setOnMouseEntered(mouseEvent -> setEffect(selectedShadowEffect));
                    setOnMouseExited(mouseEvent -> {
                        setEffect(null);
                        setEffect(SHADOW);
                    });
                    setOnMousePressed(mouseEvent -> {
                        mouseX = mouseEvent.getSceneX();
                        mouseY = mouseEvent.getSceneY();
                        setListViewOfTilesInFactories();
                    });
                    dragTiles(false);
                    setOnMouseReleased(mouseEvent -> snapToStorageOrFloor(factoryIndex));
                }
                // Tiles drag from centre (or floor).
                case 2 -> {
                    setOnMouseEntered(mouseEvent -> setEffect(selectedShadowEffect));
                    setOnMouseExited(mouseEvent -> {
                        setEffect(null);
                        setEffect(SHADOW);
                    });
                    setOnMousePressed(mouseEvent -> {
                        mouseX = mouseEvent.getSceneX();
                        mouseY = mouseEvent.getSceneY();
                        Game.this.setListViewOfTilesInCentre(tile);
                    });
                    dragTiles(false);
                    setOnMouseReleased(mouseEvent -> snapToStorageOrFloor(-1));
                }
                // Tiles drag from storage to mosaic (or floor).
                case 3 -> {
                    setOnMousePressed(mouseEvent -> {
                        mouseX = mouseEvent.getSceneX();
                        mouseY = mouseEvent.getSceneY();
                        hideOtherTilesInRow(getLayoutX(), getLayoutY());
                    });
                    dragTiles(true);
                    setOnMouseReleased(mouseEvent -> snapToMosaic());
                }
            }
        }


        /**
         * Drag tiles from centre, factories or storage.
         *
         * @param isFromStorage If tiles are dragged from storage
         */
        private void dragTiles(boolean isFromStorage) {
            // Event: when tile is dragged:
            setOnMouseDragged(mouseEvent -> {
                // Move current tile to the top layer.
                toFront();
                // Move with mouse
                double movementX = mouseEvent.getSceneX() - mouseX;
                double movementY = mouseEvent.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();
                // If a tile is not from storage:
                /* When dragging tiles from storage, there will only be one tile
                 * displayed. However, when dragging tiles from centre or factories,
                 * tiles will be displayed as list view. */
                if (!isFromStorage) {
                    // Move the temporary board with mouse.
                    TemporaryBoard.setLayoutX(TemporaryBoard.getLayoutX() + movementX);
                    TemporaryBoard.setLayoutY(TemporaryBoard.getLayoutY() + movementY);
                    mouseEvent.consume();
                }
            });
        }


        /**
         * Snap a tile to mosaic.
         *
         * @author Qm9nb25nIFdhbmc=
         */
        private void snapToMosaic() {
            // Get the mosaic position where tile be placed.
            int[] mosaicGridPosition = getClickedMosaicGrid();
            // Get the floor position where the tile be placed.
            int floorPosition = getClickedFloorGrid();
            // Get row and column index of mosaic gird.
            int rowIndex = mosaicGridPosition[0];
            int colIndex = mosaicGridPosition[1];

            // Get current player and current player board.
            int currentPlayerBoardIndex = getCurrentPlayerBoardIndex();
            PlayerBoard currentPlayerBoard = GAME_BOARD.playerBoards[currentPlayerBoardIndex];
            if (rowIndex != -1 && colIndex != -1) {
                // Try to snap to the mosaic.
                try {
                    /* Check if the board is a basic board so that we could set different
                     * criteria for placing tiles. */
                    if (GAME_BOARD.isBasicGame()) {
                        currentPlayerBoard.tileToBasicMosaic(rowIndex, colIndex);
                    } else {
                        currentPlayerBoard.tileToVariantMosaic(rowIndex, colIndex);
                    }

                    // If tiles can be successfully snapping: remove tiles in storage.
                    currentPlayerBoard.removeTilesFromStorageByRow(rowIndex);
                    // Add the removed tiles at certain row into discard.
                    for (int i = 0; i < rowIndex; i++) {
                        GAME_BOARD.placeTilesToDiscard(new ArrayList<>() {{
                            add(tile);
                        }});
                    }
                    // Update the mosaic and storage view at the same time.
                    updateMosaic();
                    updateStorage(false);
                    // Play tiling sound.
                    playPlacingTileSound();

                    // Add the score after tiling.
                    int score = currentPlayerBoard.addLinkedBonusScoreAfterTiling(rowIndex, colIndex);
                    // Play moving score animation.
                    Animation animation = playAddTilingScoreAndMoveFloorTilesAnimation(score);
                    animation.play();

                    // If the animation is finished:
                    animation.setOnFinished(actionEvent -> {
                        /* Check if we have finished tiling
                         * By checking number of untiled rows in current player board:
                         * Reason: if the number of untiled rows is not 0, we need
                         * continue to tile to mosaic on current player board. */
                        // If we have finished tiling:
                        if (currentPlayerBoard.getUntiledRows().size() == 0) {
                            // Check if the game is complete:
                            if (GAME_BOARD.isGameComplete()) {
                                // Start to play evaluation animation.
                                playEvaluationAnimation(0);
                                // If the game is not complete:
                            } else {
                                // Start a new round
                                switchToNextPlayerBoardAfterTiling();
                            }
                        }
                        // Else: continue to tile to mosaic.
                    });
                    /* If the tile cannot be placed into the mosaic grid,
                     * place the dragged tile back. */
                } catch (Exception e) {
                    updateStorage(false);
                }
            } else if (floorPosition != -1) {
                if (GAME_BOARD.isBasicGame()) {
                    updateStorage(false);
                } else {
                    int[] originPosition = getClickedStorageGrid(originX, originY);
                    int originRow = originPosition[1];
                    int numOfPlaceableCols = currentPlayerBoard.getPlaceableColsInMosaicByRow(originRow).size();
                    if (numOfPlaceableCols == 0) {
                        ArrayList<Tile> allTilesInRow =
                                currentPlayerBoard.removeAllTilesFromStorageByRow(originRow);
                        ArrayList<Tile> redundantTiles =
                                currentPlayerBoard.moveTilesToFloor(allTilesInRow);
                        GAME_BOARD.placeTilesToDiscard(redundantTiles);
                        updateStorage(false);
                        updateFloor();
                        // Play tiling sound.
                        playPlacingTileSound();
                        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
                        pauseTransition.play();
                        pauseTransition.setOnFinished(actionEvent -> {
                            if (currentPlayerBoard.getUntiledRows().size() == 0) {
                                // Check if the game is complete:
                                if (GAME_BOARD.isGameComplete()) {
                                    // Start to play evaluation animation.
                                    playEvaluationAnimation(0);
                                    // If the game is not complete:
                                } else {
                                    // Start a new round
                                    switchToNextPlayerBoardAfterTiling();
                                }
                            }
                        });
                    } else {
                        updateStorage(false);
                    }
                }
            } else {
                updateStorage(false);
            }
        }


        /**
         * Snap a list of tiles into storage or floor.
         *
         * @author Qm9nb25nIFdhbmc=
         */
        private void snapToStorageOrFloor(int factoryIndex) {
            TemporaryBoard.getChildren().clear();

            int[] clickedPositionInStorage = getClickedStorageGrid(getLayoutX(), getLayoutY());
            int clickedPositionOnFloor = getClickedFloorGrid();
            // If a row index is not -1 and the row is placeable row:
            if (clickedPositionInStorage[0] != -1
                    && GAME_BOARD.playerBoards[getCurrentPlayerBoardIndex()].isPlaceableRowInStorage(tile, clickedPositionInStorage[0])) {
                ArrayList<Tile> drewTiles = drewTilesFrom(factoryIndex);

                // Place tiles into storage
                ArrayList<Tile> tilesToBePlacedOnFloor = GAME_BOARD.playerBoards[getCurrentPlayerBoardIndex()].
                        moveTilesToStorage(drewTiles, clickedPositionInStorage[0]);
                // Place tiles into floor
                ArrayList<Tile> tilesToBePlacedToDiscard = GAME_BOARD.playerBoards[getCurrentPlayerBoardIndex()].
                        moveTilesToFloor(tilesToBePlacedOnFloor);
                // Place redundant tiles into discard
                GAME_BOARD.placeTilesToDiscard(tilesToBePlacedToDiscard);

                // Reset the place of temporary board
                TemporaryBoard.setLayoutX(0);
                TemporaryBoard.setLayoutY(0);
                updateStorage(true);
                updateFloor();

                playFromFactoryToCentreAnimation(factoryIndex);

            } else if (clickedPositionOnFloor != -1) {
                ArrayList<Tile> drewTiles = drewTilesFrom(factoryIndex);

                // Place tiles into floor
                ArrayList<Tile> tilesToBePlacedToDiscard
                        = GAME_BOARD.playerBoards[getCurrentPlayerBoardIndex()].moveTilesToFloor(drewTiles);
                // Place redundant tiles into discard
                GAME_BOARD.placeTilesToDiscard(tilesToBePlacedToDiscard);

                // Reset the place of temporary board
                TemporaryBoard.setLayoutX(0);
                TemporaryBoard.setLayoutY(0);
                updateFloor();
                playFromFactoryToCentreAnimation(factoryIndex);

            } else {
                TemporaryBoard.setLayoutX(0);
                TemporaryBoard.setLayoutY(0);
                updateCentre();
                updateFactories();
            }
        }


        /**
         * Drew tiles from factory or centre. If factory index is -1, drew tiles
         * from factory.
         *
         * @param factoryIndex Index of factory
         * @return Drew tiles
         * @author Qm9nb25nIFdhbmc=
         */
        private ArrayList<Tile> drewTilesFrom(int factoryIndex) {
            ArrayList<Tile> drewTiles;
            if (factoryIndex == -1) {
                drewTiles = GAME_BOARD.drawTileFromCentre(tile);
            } else {
                ArrayList<ArrayList<Tile>> drewTilesAndRedundantTiles
                        = GAME_BOARD.drawTilesFromFactory(factoryIndex, tile);
                drewTiles = drewTilesAndRedundantTiles.get(0);
                ArrayList<Tile> tilesToBePlacedIntoCentre = drewTilesAndRedundantTiles.get(1);
                GAME_BOARD.placeTilesToCentre(tilesToBePlacedIntoCentre);
            }

            return drewTiles;
        }


        /**
         * Check which grid we are about to place in mosaic.
         *
         * @author Qm9nb25nIFdhbmc=
         */
        private int[] getClickedMosaicGrid() {
            double rowPosition = (getLayoutY() - FIRST_ROW_STORAGE_MOSAIC_Y) / (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC);
            double colPosition = (getLayoutX() - FIRST_COL_X_MOSAIC) / (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC);

            if (rowPosition > 4.5 || rowPosition < -0.5) {
                return new int[]{-1, -1};
            }

            if (colPosition > 4.5 || colPosition < -0.5) {
                return new int[]{-1, -1};
            }
            return new int[]{(int) Math.round(rowPosition), (int) Math.round(colPosition)};
        }


        /**
         * Get which grid we've clicked on floor.
         *
         * @return tile we clicked
         * @author Qm9nb25nIFdhbmc=
         */
        private int getClickedFloorGrid() {
            if (Math.abs(FLOOR_Y - getLayoutY()) / TILE_SIZE >= 1) {
                return -1;
            }

            double x_dist = (getLayoutX() - FLOOR_X) / (TILE_SIZE + TILE_GAP_FLOOR);
            if (x_dist > 6.5 || x_dist < -0.5) {
                return -1;
            }

            return (int) Math.round(x_dist);
        }


        /**
         * Set a list view of tiles when dragging from factories.
         *
         * @author Qm9nb25nIFdhbmc=
         */
        private void setListViewOfTilesInFactories() {
            int factoryIndex = getClickedFactoryGrid(getLayoutX(), getLayoutY())[0];
            Tile[] factory = GAME_BOARD.getFactories()[factoryIndex];

            int counter = 0;
            for (int i = 0; i < 4; i++) {
                Tile currTile = factory[i];
                if (currTile == tile) {
                    DraggableTile draggableTile = new DraggableTile(
                            tile,
                            getLayoutX() - (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * counter,
                            getLayoutY(),
                            0);
                    TemporaryBoard.getChildren().add(draggableTile);
                    hideTileInFactoryByIndex(factoryIndex, i);
                    counter++;
                }
            }
        }
    }


    /**
     * Initialize a game board by giving if we need to initialize a basic board
     * and number of players.
     *
     * @param isBasic If we need to initialize basic game
     * @param numOfPlayers Number of players
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void initGameBoard(int numOfPlayers, boolean isBasic) {
        if (isBasic) {
            GAME_BOARD = GameBoard.initBasicGameBoards(numOfPlayers);
        } else {
            GAME_BOARD = GameBoard.initVariantGameBoards(numOfPlayers);
        }
        // Set the view of factories positions by given number of players.
        setFactoryPositions();
        // Set the view centre positions by given number of players.
        setCentrePositions();

        // Set view of game board.
        String BOARD_URI = getGraphicalBoardURI(GAME_BOARD);
        ImageView BOARD_IMAGE = new ImageView(BOARD_URI);
        root.getChildren().add(BOARD_IMAGE);
    }


    /**
     * Update player board and switch button.
     */
    private void updatePlayerBoard(boolean isLocked) {
        setPlayerBoard(getCurrentPlayerBoardIndex(), isLocked);
        updatePlayerBoardSwitchButton();
    }


    /**
     * Set the view of a player board.
     *
     * @param playerBoardIndex Index of the player board to be visualized.
     * @param isLocked If is the storage area is locked
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setPlayerBoard(int playerBoardIndex, boolean isLocked) {
        StorageArea.getChildren().clear();
        MosaicArea.getChildren().clear();
        FloorArea.getChildren().clear();
        ScoreArea.getChildren().clear();
        setStorageWithTiles(playerBoardIndex, isLocked);
        setMosaicWithTiles(playerBoardIndex);
        setFloorWithTiles(playerBoardIndex);
        setScoreMarker(playerBoardIndex);
    }


    /**
     * Update storage view.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void updateStorage(boolean isLocked) {
        StorageArea.getChildren().clear();
        setStorageWithTiles(getCurrentPlayerBoardIndex(), isLocked);
    }


    /**
     * Update storage view.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void updateMosaic() {
        MosaicArea.getChildren().clear();
        setMosaicWithTiles(getCurrentPlayerBoardIndex());
    }


    /**
     * Update floor view.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void updateFloor() {
        FloorArea.getChildren().clear();
        setFloorWithTiles(getCurrentPlayerBoardIndex());
    }


    /**
     * Update centre view and centre count.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void updateCentre() {
        CentreArea.getChildren().clear();
        setCentreWithTiles();
        setCentreTilesCount();
    }


    /**
     * Update factories view.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void updateFactories() {
        FactoriesArea.getChildren().clear();
        setFactoriesWithTiles();
    }


    /**
     * Update score marker.
     */
    private void updateScore() {
        ScoreArea.getChildren().clear();
        setScoreMarker(Player.getIndexOfPlayer(GAME_BOARD.getTurn()) - 1);
    }


    /**
     * Set tiles on storage based on a player board.
     *
     * @param playerBoardIndex Index of player board
     * @param isLocked If the tiles on the board is movable.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setStorageWithTiles(int playerBoardIndex, boolean isLocked) {
        // Get the player board by player board index
        PlayerBoard currPlayerBoard = GAME_BOARD.playerBoards[playerBoardIndex];
        Storage storage = currPlayerBoard.getStorage();

        for (int rowIndex = 0; rowIndex < 5; rowIndex++) {
            Tile[] tiles = storage.getRowByRowIndex(rowIndex);
            for (int colIndex = 0; colIndex < tiles.length; colIndex++) {
                Tile tile = tiles[colIndex];
                if (tile != null) {
                    double x = FIRST_COL_X_STORAGE - (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * colIndex;
                    double y = FIRST_ROW_STORAGE_MOSAIC_Y + (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * rowIndex;

                    boolean isFirstUntiledRow = false;
                    ArrayList<Integer> untiledRows = currPlayerBoard.getUntiledRows();
                    if (untiledRows.size() != 0 && untiledRows.get(0) == rowIndex) {
                        isFirstUntiledRow= true;
                    }

                    DraggableTile draggableTile;
                    // If the row is full and it's the first placeable row, set it as draggable.
                    boolean isRowFull = storage.getNumOfTilesByRow(rowIndex) == rowIndex + 1;
                    if (isRowFull && isFirstUntiledRow && !isLocked) {
                        draggableTile = new DraggableTile(tile, x, y, 3);
                    } else {
                        draggableTile = new DraggableTile(tile, x, y, 0);
                    }
                    StorageArea.getChildren().add(draggableTile);
                }
            }
        }
    }


    /**
     * Set tiles on mosaic based on a player board.
     *
     * @param playerBoardIndex Index of player board
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setMosaicWithTiles(int playerBoardIndex) {
        // Get the player board by player board index
        PlayerBoard currPlayerBoard = GAME_BOARD.playerBoards[playerBoardIndex];
        Mosaic mosaic = currPlayerBoard.getMosaic();

        for (int rowIndex = 0; rowIndex < 5; rowIndex++) {
            for (int colIndex = 0; colIndex < 5; colIndex++) {
                // Get the tile by row and column index
                Tile tile = mosaic.getTileByRowAndCol(rowIndex, colIndex);
                if (tile != null) {
                    // Set the position of tile.
                    double x = FIRST_COL_X_MOSAIC + (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * colIndex;
                    double y = FIRST_ROW_STORAGE_MOSAIC_Y + (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * rowIndex;
                    DraggableTile draggableTile = new DraggableTile(tile, x, y, 0);
                    // Add the tile to mosaic area.
                    MosaicArea.getChildren().add(draggableTile);
                }
            }
        }
    }


    /**
     * Set tiles on floor based on a player board.
     *
     * @param playerBoardIndex Index of player board
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setFloorWithTiles(int playerBoardIndex) {
        // Get the player board by player board index
        PlayerBoard currPlayerBoard = GAME_BOARD.playerBoards[playerBoardIndex];
        Tile[] tilesOnFloor = currPlayerBoard.getTilesOnFloor();

        for (int i = 0; i < 7; i++) {
            Tile tile = tilesOnFloor[i];
            if (tile != null) {
                double x = FLOOR_X + (TILE_SIZE + TILE_GAP_FLOOR) * i;
                DraggableTile draggableTile = new DraggableTile(tile, x, FLOOR_Y, 0);
                FloorArea.getChildren().add(draggableTile);
            }
        }
    }


    /**
     * Set a score marker on a player board.
     *
     * @param playerBoardIndex Index of player board
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setScoreMarker(int playerBoardIndex) {
        PlayerBoard currPlayerBoard = GAME_BOARD.playerBoards[playerBoardIndex];
        int score = currPlayerBoard.getScore();

        Rectangle scoreMarker = new Rectangle();
        scoreMarker.setWidth(SCORE_MARKER_SIZE);
        scoreMarker.setHeight(SCORE_MARKER_SIZE);

        scoreMarker.setArcWidth(10);
        scoreMarker.setArcHeight(10);

        scoreMarker.setEffect(SHADOW);

        double[] scoreMarkerPosition = getScoreMarkerPosition(score);
        scoreMarker.setLayoutX(scoreMarkerPosition[0]);
        scoreMarker.setLayoutY(scoreMarkerPosition[1]);

        scoreMarker.setFill(Color.BLACK);
        ScoreArea.getChildren().add(scoreMarker);
    }


    /**
     * Get the position of score marker.
     *
     * @param score Current score
     * @return Position of score marker
     */
    private double[] getScoreMarkerPosition(int score) {
        double[] position = new double[2];
        if (score == 0) {
            position[0] = SCORE_MARKER_X;
            position[1] = SCORE_MARKER_Y;
        } else {
            int colIndex = ((score - 1) % 20);
            int rowIndex = (int) (Math.ceil((double) score / 20));
            position[0] = SCORE_MARKER_X + (SCORE_MARKER_SIZE + SCORE_MARKER_X_GAP) * colIndex;
            position[1] = SCORE_MARKER_Y + (SCORE_MARKER_SIZE + SCORE_MARKER_Y_GAP) * rowIndex;
            switch (rowIndex) {
                case 3 -> position[1] += 1.5;
                case 4 -> position[1] += 3.2;
                case 5 -> position[1] += 7;
            }
        }

        return position;
    }


    /**
     * Set the tiles' view in every factories according to GAME_BOARD.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setFactoriesWithTiles() {
        // Set view of all factories.
        for (int i = 0; i < GAME_BOARD.getFactories().length; i++) {
            setFactoryWithTiles(i);
        }
    }


    /**
     * According to GAME_BOARD. Set the view of a factory by given index.
     * Set all the ties into type 2.
     *
     * @param factoryIndex Index of a factory
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setFactoryWithTiles(int factoryIndex) {
        // Get factory in game board by index.
        Tile[] factory = GAME_BOARD.getFactories()[factoryIndex];

        // Get centre coordinates of the factory.
        int factoryCentreX = FACTORY_POSITIONS[factoryIndex][0];
        int factoryCentreY = FACTORY_POSITIONS[factoryIndex][1];

        // Iterate through all the tiles:
        for (int i = 0; i < GAME_BOARD.getNumOfTilesInFactory(factoryIndex); i++) {
            // Get the tile.
            Tile tile = factory[i];
            // Set it's position
            double tilePositionX = factoryCentreX + Math.pow(-1, i + 1) * (TILE_SIZE / 2 + TILE_GAP_FACTORY);
            double tilePositionY = factoryCentreY + Math.pow(-1, (double) (i / 2) + 1) * (TILE_SIZE / 2 + TILE_GAP_FACTORY);

            // Add the tile to shared board.
            DraggableTile draggableTile = new DraggableTile(tile, tilePositionX, tilePositionY, 1);
            FactoriesArea.getChildren().add(draggableTile);
        }
    }


    /**
     * Set the tiles' view in centre according to GAME_BOARD.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setCentreWithTiles() {
        // Get all tiles in centre.
        Tile[] allTiles = Tile.getAllTiles();

        LinkedHashMap<Tile, Integer> tileMap = GAME_BOARD.getSortedCentreTilesMap();

        int i = 0;
        for (int numOfTiles : tileMap.values()) {
            Tile currTile = allTiles[i];
            setCentreWithTile(currTile, numOfTiles);
            i++;
        }
    }


    /**
     * Hide first player token at centre.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void hideFirstPlayerTokenInCentre() {
        for (Node node : CentreArea.getChildren()) {
            if (node instanceof DraggableTile && ((DraggableTile) node).tile == Tile.FirstPlayerToken) {
                CentreArea.getChildren().remove(node);
                break;
            }
        }
    }


    /**
     * Set a tile's view on it's corresponding position by giving it's count.
     *
     * @param tile The tile to be placed
     * @param numOfTiles Number of tiles
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setCentreWithTile(Tile tile, int numOfTiles) {
        Tile[] allTiles = Tile.getAllTiles();
        int tileIndex = Arrays.asList(allTiles).indexOf(tile);
        // If the number of tiles is not 0: add the tile.
        if (numOfTiles != 0) {
            DraggableTile draggableTile;
            if (tile == Tile.FirstPlayerToken) {
                draggableTile = new DraggableTile(tile,
                        CENTRE_POSITIONS[tileIndex][0],
                        CENTRE_POSITIONS[tileIndex][1], 0);
            } else {
                draggableTile = new DraggableTile(tile,
                        CENTRE_POSITIONS[tileIndex][0],
                        CENTRE_POSITIONS[tileIndex][1], 2);
            }
            CentreArea.getChildren().add(draggableTile);
        }
    }


    /**
     * Set label of number of each tiles in centre.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setCentreTilesCount() {
        Tile[] allTiles = Tile.getAllTiles();

        for (Tile tile : allTiles) {
            setCentreTileCount(tile);
        }
    }


    /**
     * Set the label that shows number of tiles in centre.
     *
     * @param tile The tile to be shown.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setCentreTileCount(Tile tile) {
        // If the tile is first player token. don't show.
        if (tile != Tile.FirstPlayerToken) {
            // Get the number of all tiles in centre.
            LinkedHashMap<Tile, Integer> tileMap = GAME_BOARD.getSortedCentreTilesMap();
            // Get the number of tiles.
            int tileCount = tileMap.get(tile);

            Tile[] allTiles = Tile.getAllTiles();
            int tileIndex = Arrays.asList(allTiles).indexOf(tile);
            // If the number of tiles is not 0: show it.
            if (tileCount != 0) {
                // Set font.
                Label countLabel = new Label("×" + tileCount);
                countLabel.setFont(Font.loadFont(FONT, 23));
                // Set position.
                countLabel.setLayoutX(CENTRE_POSITIONS[tileIndex][0] + TILE_COUNT_OFFSET_X);
                countLabel.setLayoutY(CENTRE_POSITIONS[tileIndex][1] + TILE_COUNT_OFFSET_Y);
                CentreArea.getChildren().add(countLabel);
            }
        }
    }


    /**
     * Get player board index of current turn.
     *
     * @return Player board index
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private int getCurrentPlayerBoardIndex() {
        return Player.getIndexOfPlayer(GAME_BOARD.getTurn()) - 1;
    }




    /**
     * Get position of a tile in centre.
     *
     * @param tile Tile to find position
     * @return Position of the tile
     *         return {0, 0} if the tile is not exist
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private int[] getTilePositionInCentre(Tile tile) {
        Tile[] allTiles = Tile.getAllTiles();
        for (int i = 0; i < allTiles.length; i++) {
            if (tile == allTiles[i]) {
                return CENTRE_POSITIONS[i];
            }
        }
        return new int[] {0, 0};
    }


    /**
     * Set the list view of selected tiles in centre.
     *
     * @param tile Tile selected
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setListViewOfTilesInCentre(Tile tile) {
        // Hide the counting in centre.
        hideCentreTilesCount(tile);
        double tileX = getTilePositionInCentre(tile)[0];
        double tileY = getTilePositionInCentre(tile)[1];

        // Create list view of tiles in centre.
        LinkedHashMap<Tile, Integer> tileMap = GAME_BOARD.getSortedCentreTilesMap();
        int numOfTiles = tileMap.get(tile);
        for (int i = 0; i < numOfTiles; i++) {
            DraggableTile draggableTile = new DraggableTile(
                    tile,
                    tileX - (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * i,
                    tileY,
                    0);
            if (i == 0) {
                draggableTile.setEffect(null);
            }
            TemporaryBoard.getChildren().add(draggableTile);
        }

        // If centre contains first player token, create a view for it (to temporary board).
        if (tileMap.get(Tile.FirstPlayerToken) == 1) {
            DraggableTile firstPlayerToken = new DraggableTile(
                    Tile.FirstPlayerToken,
                    tileX - (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * numOfTiles,
                    tileY,
                    0);
            TemporaryBoard.getChildren().add(firstPlayerToken);
            hideFirstPlayerTokenInCentre();
        }
    }


    /**
     * Hide the count label of a given tile.
     *
     * @param tile The tile to be hide label.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void hideCentreTilesCount(Tile tile) {
        Tile[] allTiles = Tile.getAllTiles();

        // Get the position of the label.
        int tileIndex = Arrays.asList(allTiles).indexOf(tile);
        double labelX = CENTRE_POSITIONS[tileIndex][0] + TILE_COUNT_OFFSET_X;
        double labelY = CENTRE_POSITIONS[tileIndex][1] + TILE_COUNT_OFFSET_Y;

        for (Node node : CentreArea.getChildren()) {
            // Get the node position.
            double nodeX = node.getLayoutX();
            double nodeY = node.getLayoutY();
            // If the position matches, remove it.
            if (nodeX == labelX && nodeY == labelY) {
                CentreArea.getChildren().remove(node);
                break;
            }
        }
    }


    /**
     * Update player board switch button.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void updatePlayerBoardSwitchButton() {
        SwitchButton.getChildren().clear();
        setPlayerBoardSwitchButton();
    }


    /**
     * Set player board switch button
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setPlayerBoardSwitchButton() {
        int numOfPlayers = GAME_BOARD.getNumOfPlayers();
        ToggleGroup switchGroup = new ToggleGroup();
        double totalWidth = 678;
        HBox hbox = new HBox();
        int currentPlayerBoardIndex = getCurrentPlayerBoardIndex();

        ToggleButton switchToPlayerBoard0 = new ToggleButton();
        ToggleButton switchToPlayerBoard1 = new ToggleButton();
        ToggleButton switchToPlayerBoard2 = new ToggleButton();
        ToggleButton switchToPlayerBoard3 = new ToggleButton();

        ToggleButton[] buttons = new ToggleButton[] {
                switchToPlayerBoard0,
                switchToPlayerBoard1,
                switchToPlayerBoard2,
                switchToPlayerBoard3};

        buttons[currentPlayerBoardIndex].setSelected(true);

        // Set font size of label.
        double fontSize = 0;
        switch (numOfPlayers) {
            case 2 -> fontSize = 24;
            case 3 -> fontSize = 18;
            case 4 -> fontSize = 14.5;
        }

        for (int i = 0; i < numOfPlayers; i++) {
            ToggleButton button = buttons[i];
            button.setMinWidth(totalWidth / numOfPlayers);

            // Set mouse event.
            int finalI = i;
            button.setOnMouseClicked(mouseEvent -> {
                setPlayerBoard(finalI, getCurrentPlayerBoardIndex() != finalI);
                if (finalI == currentPlayerBoardIndex) {
                    CentreArea.setDisable(false);
                    FactoriesArea.setDisable(false);
                } else {
                    CentreArea.setDisable(true);
                    FactoriesArea.setDisable(true);
                }
            });

            // Set text on button.
            if (i == currentPlayerBoardIndex) {
                button.setText("PLAYER BOARD " + (i + 1));
                button.setFont(Font.loadFont(FONT, fontSize));
                button.setTextFill(Color.RED);
            } else {
                button.setText("PLAYER BOARD " + (i + 1));
                button.setFont(Font.loadFont(FONT, fontSize));
            }
            button.setMinHeight(44);
            button.setToggleGroup(switchGroup);
            hbox.getChildren().add(button);
        }

        hbox.setLayoutX(18);
        SwitchButton.getChildren().add(hbox);
    }


    /**
     * Set up a game board.
     *
     * @param numOfPlayers Number of players in the game.
     * @param isBasicGame Is basic game or variant game.
     * @param firstPlayerIndex First player in the game.
     */
    private void setUpBoard(int numOfPlayers, boolean isBasicGame, int firstPlayerIndex) {
        // Remove welcome page.
        root.getChildren().clear();
        initGameBoard(numOfPlayers, isBasicGame);
        GAME_BOARD.initGame(firstPlayerIndex);

        // Add all the group panes into root group.
        root.getChildren().add(CentreArea);
        root.getChildren().add(FactoriesArea);
        PlayerBoard.getChildren().addAll(MosaicArea, FloorArea, StorageArea, ScoreArea);
        root.getChildren().add(PlayerBoard);
        root.getChildren().add(SwitchButton);
        root.getChildren().add(TemporaryBoard);

        // Set up player board switch button.
        setPlayerBoardSwitchButton();
        // Set the first player of the game.
        setPlayerBoard(firstPlayerIndex - 1, true);
        // Set up factories.
        setFactoriesWithTiles();
        // Set up centre.
        setCentreWithTiles();
        setCentreTilesCount();

        Player firstPlayer = Player.getPlayerByIndex(firstPlayerIndex);
        if (PlayerTypesMap.get(firstPlayer) == 1) {
            applyBasicAIMove();
        } else if (PlayerTypesMap.get(firstPlayer) == 2) {
            applyAdvancedAIMove();
        }
    }



    /**
     * Set the positions of factories' centre (for setting tiles in factories).
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setFactoryPositions() {
        int numberOfPlayers = GAME_BOARD.getNumOfPlayers();

        int[][] factoryPositions;
        switch (numberOfPlayers) {
            case 2 -> factoryPositions = new int[][] {
                    {959, 150},   // Factory 0
                    {1174, 307},  // Factory 1
                    {1092, 565},  // Factory 2
                    {825, 565},   // Factory 3
                    {743, 307},   // Factory 4
            };
            case 3 -> factoryPositions = new int[][] {
                    {966, 136},  // Factory 0
                    {1139, 221}, // Factory 1
                    {1178, 411}, // Factory 2
                    {1059, 561}, // Factory 3
                    {868, 561},  // Factory 4
                    {753, 411},  // Factory 5
                    {793, 221}   // Factory 6
            };
            case 4 -> factoryPositions = new int[][] {
                    {955, 125},  // Factory 0
                    {1097, 180}, // Factory 1
                    {1179, 325}, // Factory 2
                    {1150, 485}, // Factory 3
                    {1034, 589}, // Factory 4
                    {873, 589},  // Factory 5
                    {760, 485},  // Factory 6
                    {732, 325},  // Factory 7
                    {810, 180}   // Factory 8
            };
            default -> throw new IllegalStateException("Unexpected value: " + numberOfPlayers);
        }
        FACTORY_POSITIONS = factoryPositions;
    }


    /**
     * Set the positions of tiles in centre.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void setCentrePositions() {
        int numberOfPlayers = GAME_BOARD.getNumOfPlayers();

        int[][] centrePositions;
        switch (numberOfPlayers) {
            case 2 -> centrePositions = new int[][]{
                    {965, 270},  // Blue tile (Blue one)
                    {870, 350},  // Green tile (Yellow with red pattern)
                    {905, 450},  // Orange tile (Red one)
                    {1030, 450}, // Purple tile (Black with blue pattern)
                    {1055, 350}, // Red tile (White with blue pattern)
                    {965, 365}   // First player token
            };
            case 3, 4 -> centrePositions = new int[][]{
                    {965, 250},  // Blue tile (Blue one)
                    {870, 330},  // Green tile (Yellow with red pattern)
                    {905, 430},  // Orange tile (Red one)
                    {1030, 430}, // Purple tile (Black with blue pattern)
                    {1055, 330}, // Red tile (White with blue pattern)
                    {965, 345}   // First player token
            };
            default -> throw new IllegalStateException("Unexpected value: " + numberOfPlayers);
        }

        CENTRE_POSITIONS = centrePositions;
    }


    /**
     * Check which grid we are about to place in storage.
     *
     * @param x x coordinate of clicked position
     * @param y y coordinate of clicked position
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private int[] getClickedStorageGrid(double x, double y) {
        double rowPosition = (y - FIRST_ROW_STORAGE_MOSAIC_Y) / (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC);

        if (rowPosition > 4.5 || rowPosition < -0.5) {
            return new int[] {-1, -1};
        }

        double colPosition = rowPosition + (x - FIRST_COL_X_STORAGE) / (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC);

        // Check if clicked area is out of bound.
        int intRowPosition = (int) Math.round(rowPosition);
        if (colPosition > intRowPosition + 0.5 || colPosition < -0.5) {
            return new int[] {-1, -1};
        }

        return new int[] {(int) Math.round(rowPosition), (int) Math.round(colPosition)};
    }


    /**
     * Get factory index of a clicked tile.
     *
     * @return An array represent the position in factory.
     * <p>
     * First value represents the factory index.
     * <p>
     * Second index represents the position index.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private int[] getClickedFactoryGrid(double x, double y) {
        int factoryIndex = 0;

        for (int i = 0; i < FACTORY_POSITIONS.length; i++) {
            double factoryX = FACTORY_POSITIONS[i][0];
            double factoryY = FACTORY_POSITIONS[i][1];

            double offset = (TILE_SIZE / 2 + TILE_GAP_FACTORY);
            double leftBoundary = factoryX - offset - 1;
            double rightBoundary = factoryX + offset + 1;
            double topBoundary = factoryY - offset - 1;
            double bottomBoundary = factoryY + offset + 1;

            boolean isInLeftRightBoundary = leftBoundary < x && x < rightBoundary;
            boolean isInTopBottomBoundary = topBoundary < y && y < bottomBoundary;
            if (isInLeftRightBoundary && isInTopBottomBoundary) {
                factoryIndex = i;
                break;
            }
        }

        int positionIndex;
        if (y > FACTORY_POSITIONS[factoryIndex][1]) {
            if (x > FACTORY_POSITIONS[factoryIndex][0]) {
                positionIndex = 3;
            } else {
                positionIndex = 2;
            }
        } else {
            if (x > FACTORY_POSITIONS[factoryIndex][0]) {
                positionIndex = 1;
            } else {
                positionIndex = 0;
            }
        }

        return new int[] {factoryIndex, positionIndex};
    }


    /**
     * Hide a tile in factory by it's index.
     *
     * @param factoryIndex Index of a factory
     * @param tileIndex Index of a tile in factory
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void hideTileInFactoryByIndex(int factoryIndex, int tileIndex) {
        for (Node node : FactoriesArea.getChildren()) {
            double nodeX = node.getLayoutX();
            double nodeY = node.getLayoutY();
            int nodeFactoryIndex = getClickedFactoryGrid(nodeX, nodeY)[0];
            int nodeTileIndexInFactory = getClickedFactoryGrid(nodeX, nodeY)[1];
            if (nodeFactoryIndex == factoryIndex && nodeTileIndexInFactory == tileIndex) {
                FactoriesArea.getChildren().remove(node);
                break;
            }
        }
    }


    /**
     * Hide other tiles in storage.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void hideOtherTilesInRow(double x, double y) {
        ArrayList<Node> hiddenTiles = new ArrayList<>();
        int clickedRowIndex = getClickedStorageGrid(x, y)[0];
        int clickedColIndex = getClickedStorageGrid(x, y)[1];
        for (Node node : StorageArea.getChildren()) {
            int nodeRowIndex = getClickedStorageGrid(node.getLayoutX(), node.getLayoutY())[0];
            int nodeColIndex = getClickedStorageGrid(node.getLayoutX(), node.getLayoutY())[1];

            if (nodeRowIndex == clickedRowIndex && nodeColIndex != clickedColIndex) {
                hiddenTiles.add(node);
            }
        }
        StorageArea.getChildren().removeAll(hiddenTiles);
    }


    /**
     * Switch to next player board after tiling step.
     */
    private void switchToNextPlayerBoardAfterTiling() {
        // Get next player:
        Player nextPlayer = Player.nextPlayer(GAME_BOARD.getTurn(), GAME_BOARD.getNumOfPlayers());

        int nextPlayerType = PlayerTypesMap.get(nextPlayer);
        // Get next player board:
        PlayerBoard nextPlayerBoard = GAME_BOARD.getPlayerBoardByPlayer(nextPlayer);

        // If next player board has untiled rows, switch to next player board.
        if (nextPlayerBoard.getUntiledRows().size() != 0) {
            GAME_BOARD.changeToNextPlayer();
            updatePlayerBoard(false);
            if (nextPlayerType == 1) {
                applyBasicAIMove();
            } else if (nextPlayerType == 2) {
                applyAdvancedAIMove();
            }
        // If next player board has no untiled rows:
        } else {
            // If next player board has some tiles on floor but doesn't has
            // any untiled tiles on storage:
            if (nextPlayerBoard.getNumOfTilesOnFloor() != 0) {
                /* Change to next player board, and start moving those tiles
                 * into discard. */
                GAME_BOARD.changeToNextPlayer();
                updatePlayerBoard(false);
                int playerBoardIndex = getCurrentPlayerBoardIndex();
                moveTilesFromFloorToDiscardIter(playerBoardIndex);
            // If the next doesn't has tiles on floor or doesn't has any untiles rows:
            // (This means we could refill factories and entre next round)
            } else {
                Player nextPlayerHasUntiledRows = GAME_BOARD.getNextPlayerHasUntiledRows();
                if (nextPlayerHasUntiledRows == null) {
                    // Play refill factories sound
                    playRefillFactoriesSound();
                    // Crease a pause animation for playing sound.
                    PauseTransition pauseTransition = new PauseTransition();
                    pauseTransition.setDuration(Duration.seconds(1));
                    // If the sound is finished playing:
                    pauseTransition.setOnFinished(actionEvent -> {
                        // Change the player board to in next round
                        GAME_BOARD.setTurn(PlayerInNextRound);
                        // Refill factories.
                        GAME_BOARD.refillAllFactories();
                        // Update factories, centre and player board.
                        updateCentre();
                        updateFactories();
                        updatePlayerBoard(false);
                        int playerInNextRoundType = PlayerTypesMap.get(PlayerInNextRound);
                        if (playerInNextRoundType == 1) {
                            applyBasicAIMove();
                        } else if (playerInNextRoundType == 2) {
                            applyAdvancedAIMove();
                        }
                    });
                    pauseTransition.play();
                } else {
                    GAME_BOARD.setTurn(nextPlayerHasUntiledRows);
                    updatePlayerBoard(false);
                }
            }
        }
    }


    /**
     * Iteratively move tiles on floor into discard.
     * (If the board doesn't have untiled rows.)
     *
     * @param playerBoardIndex Index of player board which need move tiles
     *                         into discard.
     */
    private void moveTilesFromFloorToDiscardIter(int playerBoardIndex) {
        // Get current player board.
        PlayerBoard playerBoard = GAME_BOARD.playerBoards[playerBoardIndex];

        boolean hasTilesOnFloor = playerBoard.getNumOfTilesOnFloor() != 0;
        boolean hasUntiledTiles = playerBoard.getUntiledRows().size() != 0;
        // If we have tiles on floor and don't have any untiled tiles on
        // current player board:
        if (hasTilesOnFloor && !hasUntiledTiles) {
            // Play move tiles to discard animation.
            Animation moveTilesToDiscard = playMoveTilesFromFloorToDiscardAnimation();
            // If the animation is finished:
            moveTilesToDiscard.setOnFinished(actionEvent -> {
                // Change to next player and update player board.
                GAME_BOARD.changeToNextPlayer();
                updatePlayerBoard(false);
                // If the current player board is the last player board:
                if (playerBoardIndex == GAME_BOARD.getNumOfPlayers() - 1) {
                    // The next player board (player board 1) need to move tiles on floor to discard.
                    moveTilesFromFloorToDiscardIter(0);
                    // If not, simply perform moving tiles on floor to discard.
                } else {
                    moveTilesFromFloorToDiscardIter(playerBoardIndex + 1);
                }
            });
            moveTilesToDiscard.play();
            // If the next player doesn't have any tiles on floor or untiled rows:
        } else if (!hasTilesOnFloor && !hasUntiledTiles) {
            // Refill factories.
            playRefillFactoriesSound();
            PauseTransition pauseTransition = new PauseTransition();
            pauseTransition.setDuration(Duration.seconds(1));
            pauseTransition.setOnFinished(actionEvent -> {
                GAME_BOARD.setTurn(PlayerInNextRound);
                GAME_BOARD.refillAllFactories();
                // Update factories view.
                updateCentre();
                updateFactories();
                updatePlayerBoard(false);
                int playerInNextRoundType = PlayerTypesMap.get(PlayerInNextRound);
                if (playerInNextRoundType == 1) {
                    applyBasicAIMove();
                } else if (playerInNextRoundType == 2) {
                    applyAdvancedAIMove();
                }
            });
            pauseTransition.play();
        }
    }


    /**
     * Play moving tiles from floor to discard animation
     * And move tiles from floor to discard in GAME_BOARD
     *
     * @return Animation that moves floor to discard.
     */
    private Animation playMoveTilesFromFloorToDiscardAnimation() {
        // Get current player board index.
        int currentPlayerBoardIndex = getCurrentPlayerBoardIndex();
        // Get current player board.
        PlayerBoard currentPlayerBoard = GAME_BOARD.playerBoards[currentPlayerBoardIndex];

        // Get deduction score when moving tiles to discard.
        int finalScore = currentPlayerBoard.minusLostScoreOnFloor();

        // Move tiles from floor to discard.
        ArrayList<Tile> tilesToBePlacedIntoDiscard = currentPlayerBoard.emptyFloor();
        GAME_BOARD.placeTilesToDiscard(tilesToBePlacedIntoDiscard);

        // Initialize a sequential animation.
        SequentialTransition sequentialTransition = new SequentialTransition();
        /* Initialize a parallel animation so that we could move tiles
         * together into discard. */
        ParallelTransition parallelTransition = new ParallelTransition();

        // Iterate through node:
        for (Node node : FloorArea.getChildren()) {
            // Initialize a translation animation to move tiles to discard.
            TranslateTransition translateTransition = new TranslateTransition();
            // Set destination.
            translateTransition.setToY(BOARD_HEIGHT);
            // Set the duration of animation.
            translateTransition.setDuration(Duration.seconds(ANIMATION_SPEED));
            // Bind the current node (tile) with animation.
            translateTransition.setNode(node);
            // Add this animation into parallel translation.
            parallelTransition.getChildren().add(translateTransition);
        }

        // Create a short pause for playing sound.
        PauseTransition shortPause = new PauseTransition(Duration.seconds(0.1));
        // If there are some tiles moved to discard, play sound.
        if (parallelTransition.getChildren().size() != 0) {
            shortPause.setOnFinished(actionEvent -> playMoveTilesToDiscardSound());
        }
        sequentialTransition.getChildren().add(shortPause);
        sequentialTransition.getChildren().add(parallelTransition);
        sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(ANIMATION_SPEED)));
        sequentialTransition.getChildren().add(playScoreMarkerMoveAnimation(finalScore));
        sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(0.5 * ANIMATION_SPEED)));

        // If the animation is finished, update floor.
        sequentialTransition.setOnFinished(actionEvent -> updateFloor());

        return sequentialTransition;
    }


    /**
     * Apply basic AI move and plays animation for moving.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void applyBasicAIMove() {
        FirstLegalMoveAI firstLegalMoveAI = FirstLegalMoveAI.initFirstLegalMoveAI(GAME_BOARD, GAME_BOARD.getTurn());
        SequentialTransition sequentialTransition  = new SequentialTransition();
        if (!GAME_BOARD.isDraftingMoveFinished()) {

            // Start a refresh board animation
            PauseTransition refreshBoard = new PauseTransition();
            refreshBoard.setDuration(Duration.seconds(0.5));
            refreshBoard.setOnFinished(actionEvent -> updatePlayerBoard(true));
            sequentialTransition.getChildren().add(refreshBoard);

            // Drafting move
            PauseTransition draftingMove = new PauseTransition();
            draftingMove.setOnFinished(actionEvent -> {
                GAME_BOARD.applyDraftingMove(firstLegalMoveAI.generateDraftingMove());
                updatePlayerBoard(true);
                updateCentre();
                updateFactories();
                playPlacingTileSound();
            });
            sequentialTransition.getChildren().add(draftingMove);

            // After tiling
            PauseTransition afterTiling = new PauseTransition();
            afterTiling.setDuration(Duration.seconds(2));
            afterTiling.setOnFinished(actionEvent -> {
                if (GAME_BOARD.isDraftingMoveFinished()) {
                    PlayerInNextRound = GAME_BOARD.getPlayerInNextRound();
                    switchToNextPlayerBoardAfterTiling();
                } else {
                    GAME_BOARD.changeToNextPlayer();
                    int nextPlayerType = PlayerTypesMap.get(GAME_BOARD.getTurn());
                    switch (nextPlayerType) {
                        case 0 -> updatePlayerBoard(true);
                        case 1 -> applyBasicAIMove();
                        case 2 -> applyAdvancedAIMove();
                    }
                }
            });
            sequentialTransition.getChildren().add(afterTiling);
        } else {
            /* Steps:
             * Pause a second
             * Generate tiling moves - apply tiling moves - update player board
             * Update score
             * Remove tiles from floor - update player board
             * Update score
             * Pause a second
             * Next round
             */
            SequentialTransition tilingMovesAnimation = new SequentialTransition();

            PauseTransition pauseASecond = new PauseTransition(Duration.seconds(1));
            tilingMovesAnimation.getChildren().add(pauseASecond);

            PauseTransition applyTilingMoves = new PauseTransition(Duration.seconds(0.2));
            applyTilingMoves.setOnFinished(actionEvent -> {
                playPlacingTileSound();
                ArrayList<String> tilingMoves = firstLegalMoveAI.generateTilingMoves();
                GAME_BOARD.applyMultipleTilingMoves(tilingMoves);
                updateStorage(true);
                updateMosaic();
            });
            tilingMovesAnimation.getChildren().add(applyTilingMoves);

            PauseTransition updateScore1 = new PauseTransition(Duration.seconds(0.5));
            updateScore1.setOnFinished(actionEvent -> updateScore());
            tilingMovesAnimation.getChildren().add(updateScore1);

            PauseTransition moveTilesToDiscard = new PauseTransition(Duration.seconds(1));
            moveTilesToDiscard.setOnFinished(actionEvent -> {
                playMoveTilesToDiscardSound();
                PlayerBoard currPlayerBoard = GAME_BOARD.playerBoards[getCurrentPlayerBoardIndex()];
                currPlayerBoard.minusLostScoreOnFloor();
                ArrayList<Tile> tilesToBePlacedIntoDiscard = currPlayerBoard.emptyFloor();
                GAME_BOARD.placeTilesToDiscard(tilesToBePlacedIntoDiscard);
                updateFloor();
            });
            tilingMovesAnimation.getChildren().add(moveTilesToDiscard);

            PauseTransition updateScore2 = new PauseTransition(Duration.seconds(0.5));
            updateScore2.setOnFinished(actionEvent -> updateScore());
            tilingMovesAnimation.getChildren().add(updateScore2);

            PauseTransition pauseAnotherSecond = new PauseTransition(Duration.seconds(2));
            tilingMovesAnimation.getChildren().add(pauseAnotherSecond);

            tilingMovesAnimation.setOnFinished(actionEvent -> {
                /* Check if we have finished tiling
                 * By checking number of untiled rows in current player board:
                 * Reason: if the number of untiled rows is not 0, we need
                 * continue to tile to mosaic on current player board. */
                    // Check if the game is complete:
                    if (GAME_BOARD.isGameComplete()) {
                        // Start to play evaluation animation.
                        playEvaluationAnimation(0);
                        // If the game is not complete:
                    } else {
                        // Start a new round
                        switchToNextPlayerBoardAfterTiling();
                    }
            });
            sequentialTransition.getChildren().add(tilingMovesAnimation);
        }
        sequentialTransition.setOnFinished(actionEvent -> {
            FactoriesArea.setDisable(false);
            FactoriesArea.setDisable(false);
        });
        sequentialTransition.play();
    }


    /**
     * Apply advanced AI move and play a advanced AI move animation.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void applyAdvancedAIMove() {
        GreedyMoveAI greedyMoveAI = GreedyMoveAI.initGreedyMoveAI(GAME_BOARD, GAME_BOARD.getTurn());
        SequentialTransition sequentialTransition  = new SequentialTransition();
        if (!GAME_BOARD.isDraftingMoveFinished()) {

            // Start a refresh board animation
            PauseTransition refreshBoard = new PauseTransition();
            refreshBoard.setDuration(Duration.seconds(0.5));
            refreshBoard.setOnFinished(actionEvent -> updatePlayerBoard(true));
            sequentialTransition.getChildren().add(refreshBoard);

            // Drafting move
            PauseTransition draftingMove = new PauseTransition();
            draftingMove.setOnFinished(actionEvent -> {
                GAME_BOARD.applyDraftingMove(greedyMoveAI.generateDraftingMove());
                updatePlayerBoard(true);
                updateCentre();
                updateFactories();
                playPlacingTileSound();
            });
            sequentialTransition.getChildren().add(draftingMove);

            // After tiling
            PauseTransition afterTiling = new PauseTransition();
            afterTiling.setDuration(Duration.seconds(2));
            afterTiling.setOnFinished(actionEvent -> {
                if (GAME_BOARD.isDraftingMoveFinished()) {
                    PlayerInNextRound = GAME_BOARD.getPlayerInNextRound();
                    switchToNextPlayerBoardAfterTiling();
                } else {
                    GAME_BOARD.changeToNextPlayer();
                    int nextPlayerType = PlayerTypesMap.get(GAME_BOARD.getTurn());
                    switch (nextPlayerType) {
                        case 0 -> updatePlayerBoard(true);
                        case 1 -> applyBasicAIMove();
                        case 2 -> applyAdvancedAIMove();
                    }
                }
            });
            sequentialTransition.getChildren().add(afterTiling);
        } else {
            /* Steps:
             * Pause a second
             * Generate tiling moves - apply tiling moves - update player board
             * Update score
             * Remove tiles from floor - update player board
             * Update score
             * Pause a second
             * Next round
             */
            SequentialTransition tilingMovesAnimation = new SequentialTransition();

            PauseTransition pauseASecond = new PauseTransition(Duration.seconds(1));
            tilingMovesAnimation.getChildren().add(pauseASecond);

            PauseTransition applyTilingMoves = new PauseTransition(Duration.seconds(0.2));
            applyTilingMoves.setOnFinished(actionEvent -> {
                playPlacingTileSound();
                ArrayList<String> tilingMoves = greedyMoveAI.generateTilingMoves();
                GAME_BOARD.applyMultipleTilingMoves(tilingMoves);
                updateStorage(true);
                updateMosaic();
            });
            tilingMovesAnimation.getChildren().add(applyTilingMoves);

            PauseTransition updateScore1 = new PauseTransition(Duration.seconds(0.5));
            updateScore1.setOnFinished(actionEvent -> updateScore());
            tilingMovesAnimation.getChildren().add(updateScore1);

            PauseTransition moveTilesToDiscard = new PauseTransition(Duration.seconds(1));
            moveTilesToDiscard.setOnFinished(actionEvent -> {
                playMoveTilesToDiscardSound();
                PlayerBoard currPlayerBoard = GAME_BOARD.playerBoards[getCurrentPlayerBoardIndex()];
                currPlayerBoard.minusLostScoreOnFloor();
                ArrayList<Tile> tilesToBePlacedIntoDiscard = currPlayerBoard.emptyFloor();
                GAME_BOARD.placeTilesToDiscard(tilesToBePlacedIntoDiscard);
                updateFloor();
            });
            tilingMovesAnimation.getChildren().add(moveTilesToDiscard);

            PauseTransition updateScore2 = new PauseTransition(Duration.seconds(0.5));
            updateScore2.setOnFinished(actionEvent -> updateScore());
            tilingMovesAnimation.getChildren().add(updateScore2);

            PauseTransition pauseAnotherSecond = new PauseTransition(Duration.seconds(2));
            tilingMovesAnimation.getChildren().add(pauseAnotherSecond);

            tilingMovesAnimation.setOnFinished(actionEvent -> {
                /* Check if we have finished tiling
                 * By checking number of untiled rows in current player board:
                 * Reason: if the number of untiled rows is not 0, we need
                 * continue to tile to mosaic on current player board. */
                // Check if the game is complete:
                if (GAME_BOARD.isGameComplete()) {
                    // Start to play evaluation animation.
                    playEvaluationAnimation(0);
                    // If the game is not complete:
                } else {
                    // Start a new round
                    switchToNextPlayerBoardAfterTiling();
                }
            });
            sequentialTransition.getChildren().add(tilingMovesAnimation);
        }
        sequentialTransition.setOnFinished(actionEvent -> {
            FactoriesArea.setDisable(false);
            FactoriesArea.setDisable(false);
        });
        sequentialTransition.play();
    }


    /**
     * Play moving score marker animation and placing tiles on floor to
     * discard animation.
     *
     * @param score Score after tiling
     * @return Animation contains moving score marker and place tiles on floor
     *         to discard animation.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private Animation playAddTilingScoreAndMoveFloorTilesAnimation(int score) {
        // Get current player board index.
        int currentPlayerBoardIndex = getCurrentPlayerBoardIndex();
        // Get current player board.
        PlayerBoard currentPlayerBoard = GAME_BOARD.playerBoards[currentPlayerBoardIndex];
        ArrayList<Integer> untiledRows = currentPlayerBoard.getUntiledRows();

        SequentialTransition sequentialTransition = new SequentialTransition();

        // Add moving score marker animation to sequential translation.
        sequentialTransition.getChildren().add(playScoreMarkerMoveAnimation(score));

        if (untiledRows.size() == 0) {
            // Add moving tiles to discard animation to sequential translation.
            sequentialTransition.getChildren().add(playMoveTilesFromFloorToDiscardAnimation());
        }

        return sequentialTransition;
    }


    /**
     * Play moving tiles from factory to centre animation.
     * Update factory and centre.
     *
     * @param factoryIndex Index of a factory.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void playFromFactoryToCentreAnimation(int factoryIndex) {
        /* Disable centre area and factories area in case invalid move performed
         * during animation. */
        CentreArea.setDisable(true);
        FactoriesArea.setDisable(true);

        // Initialize an arraylist for obtaining tiles that need to play animation.
        ArrayList<DraggableTile> animationTiles = new ArrayList<>();
        // Iterate through nodes (draggable tiles) in factories area:
        for (Node node : FactoriesArea.getChildren()) {
            // Get the position of the tile.
            double tileX = node.getLayoutX();
            double tileY = node.getLayoutY();
            // Get factory index of the node (tile)
            double nodeFactoryIndex = getClickedFactoryGrid(tileX, tileY)[0];
            // If the node matches with factory index:
            if (factoryIndex == nodeFactoryIndex) {
                // Add the tile to animation tiles
                animationTiles.add((DraggableTile) node);
            }
        }

        // Initialize a parallel transition:
        ParallelTransition parallelTransition = new ParallelTransition();
        Tile[] allTiles = Tile.getAllTiles();
        // Iterate through animation tiles to bind animation to tiles.
        for (DraggableTile tile : animationTiles) {
            // Set the effect of the tiles when moving
            tile.setEffect(null);
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.color(0, 0, 0, 0.2));
            tile.setEffect(shadow);

            // Initialize a translate animation for current tile.
            TranslateTransition translateTransition = new TranslateTransition();
            // Set the duration of current animation.
            translateTransition.setDuration(Duration.seconds(0.5));
            // Set the destination of tiles
            int index = Arrays.asList(allTiles).indexOf(tile.tile);
            translateTransition.setToX(CENTRE_POSITIONS[index][0] - tile.getLayoutX());
            translateTransition.setToY(CENTRE_POSITIONS[index][1] - tile.getLayoutY());
            // Bind with animation
            translateTransition.setNode(tile);
            // Add this animation into parallel translation.
            parallelTransition.getChildren().add(translateTransition);
        }
        // When all the tiles are moved into centre, update centre view.
        parallelTransition.setOnFinished(actionEvent -> updateCentre());

        // Add a sequential animation.
        SequentialTransition sequentialTransition = new SequentialTransition();

        // Add some time before switching to next player board.
        if (factoryIndex != -1) {
            sequentialTransition.getChildren().add(parallelTransition);
            sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(0.8 * ANIMATION_SPEED)));
        } else {
            sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(1.2 * ANIMATION_SPEED)));
        }

        // After the tile is placed, play sound.
        playPlacingTileSound();
        sequentialTransition.play();

        /* If the factory index is -1, this means the tile is dragged from
         * centre, update centre */
        if (factoryIndex == -1) {
            updateCentre();
        }

        // If the animation is finished:
        sequentialTransition.setOnFinished(actionEvent -> {
            // Clear temporary board.
            TemporaryBoard.getChildren().clear();
            // Update centre and factories.
            updateCentre();
            updateFactories();

            // Check if we've finished drafting move:
            // If finished:
            if (GAME_BOARD.isDraftingMoveFinished()) {
                PlayerInNextRound = GAME_BOARD.getPlayerInNextRound();
                switchToNextPlayerBoardAfterTiling();
            } else {
                GAME_BOARD.changeToNextPlayer();
                int nextPlayerType = PlayerTypesMap.get(GAME_BOARD.getTurn());
                switch (nextPlayerType) {
                    case 0 -> updatePlayerBoard(true);
                    case 1 -> applyBasicAIMove();
                    case 2 -> applyAdvancedAIMove();
                }
            }
            // Stop freezing centre and factories area.
            CentreArea.setDisable(false);
            FactoriesArea.setDisable(false);
        });
    }


    /**
     * Play score marker move animation.
     *
     * @param finalScore final score
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private Animation playScoreMarkerMoveAnimation(int finalScore) {
        // Free player when score marker is moving.
        PlayerBoard.setDisable(true);
        // Get score marker of current turn's board.
        Rectangle currentMarker = (Rectangle) ScoreArea.getChildren().get(0);
        // Set speed of animation.
        double duration = 0.8 * ANIMATION_SPEED;

        // Get the final position of the score marker.
        double[] finalPosition = getScoreMarkerPosition(finalScore);
        double finalX = finalPosition[0];
        double finalY = finalPosition[1];

        // Initialize a parallel animation.
        ParallelTransition parallelTransition = new ParallelTransition();

        // Initialize a translate animation for moving tiles.
        TranslateTransition scoreMarkerMove = new TranslateTransition();
        // Set the duration and final position of score marker.
        scoreMarkerMove.setDuration(Duration.seconds(duration));
        scoreMarkerMove.setToX(finalX - currentMarker.getLayoutX());
        scoreMarkerMove.setToY(finalY - currentMarker.getLayoutY());
        // Bind animation with score marker.
        scoreMarkerMove.setNode(currentMarker);

        parallelTransition.getChildren().add(scoreMarkerMove);

        // When the animation is finished, stop freezing current player board.
        parallelTransition.setOnFinished(actionEvent -> PlayerBoard.setDisable(false));

        return parallelTransition;
    }


    /**
     * Get the URI of graphical board.
     *
     * @param gameBoard A game board
     * @return A string that represents it's graphical game board
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private static String getGraphicalBoardURI(GameBoard gameBoard) {
        int numOfPlayers = gameBoard.getNumOfPlayers();
        boolean isBasicBoard = gameBoard.isBasicGame();

        String board_URI = "";

        if (isBasicBoard) {
            board_URI += "basic-game-board";
        } else {
            board_URI += "variant-game-board";
        }

        board_URI += "-" + numOfPlayers + "-players.png";

        return Objects.requireNonNull(Game.class.getResource(URI_BASE + board_URI)).toString();
    }


    /**
     * Give a tile. Obtain it's URI.
     *
     * @param tile The tile to be obtained URI
     * @return URI of the tile
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private static String getGraphicalTileURI(Tile tile) {
        if (tile == Tile.FirstPlayerToken) {
            return Objects.requireNonNull(Game.class.getResource(URI_BASE + "first-player-token.png")).toString();
        }
        String tile_URI = "";
        tile_URI += tile.getColour() + "-tile.png";
        return Objects.requireNonNull(Game.class.getResource(URI_BASE + tile_URI)).toString();
    }



    /**
     * Play tiling sound.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void playPlacingTileSound() {
        final String TILE_ON_STORAGE_MOSAIC_SOUND = Objects.requireNonNull(
                Game.class.getResource(URI_BASE + "place-tile.wav")).toString();
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(TILE_ON_STORAGE_MOSAIC_SOUND));
        mediaPlayer.setVolume(VOLUME);
        mediaPlayer.play();
    }


    /**
     * Play moving tiles from floor to discard sound.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void playMoveTilesToDiscardSound() {
        final String TILE_ON_FLOOR_SOUND = Objects.requireNonNull(
                Game.class.getResource(URI_BASE + "place-tiles-on-floor.wav")).toString();
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(TILE_ON_FLOOR_SOUND));
        mediaPlayer.setVolume(VOLUME);
        mediaPlayer.play();
    }


    /**
     * Play moving refill factories sound.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private void playRefillFactoriesSound() {
        final String REFILL_FACTORIES_SOUND = Objects.requireNonNull(
                Game.class.getResource(URI_BASE + "refill-factories.wav")).toString();
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(REFILL_FACTORIES_SOUND));
        mediaPlayer.setVolume(VOLUME);
        mediaPlayer.play();
    }


    /**
     * Set mute when press "M".
     */
    private void setMute() {
        root.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.M) {
                if (VOLUME == 0) {
                    VOLUME = 1;
                } else {
                    VOLUME = 0;
                }
            }
        });
    }
///////////////////////////////  Welcome Page  /////////////////////////////////


    /**
     * Set a welcome page
     */
    private void setWelcomePage() {
        root.getChildren().add(WelcomePage);
        // Set background.
        Image backGroundImage = new Image(Objects.requireNonNull(
                Game.class.getResource(URI_BASE + "board-background.jpg")).toString());
        ImageView backGround = new ImageView(backGroundImage);
        WelcomePage.getChildren().add(backGround);

        // Set shadow effect.
        DropShadow shadow = new DropShadow();
        shadow.setOffsetY(1.0);
        shadow.setColor(Color.color(0, 0, 0, 0.2));

        // Set font size for welcome message.
        double fontSize = 90;
        TextFlow welcomeMessage = new TextFlow();
        // Setting texts.
        Text welcomeTo = new Text("WELCOME TO ");
        welcomeTo.setFont(Font.loadFont(FONT, fontSize));
        Text A = new Text("A");
        A.setFont(Font.loadFont(FONT, fontSize));
        A.setFill(Color.rgb(64, 132, 165));
        Text Z = new Text("Z");
        Z.setFont(Font.loadFont(FONT, fontSize));
        Z.setFill(Color.rgb(234, 185, 89));
        Text U = new Text("U");
        U.setFont(Font.loadFont(FONT, fontSize));
        U.setFill(Color.rgb(220, 78, 77));
        Text L = new Text("L");
        L.setFont(Font.loadFont(FONT, fontSize));
        L.setFill(Color.rgb(27, 34, 37));
        welcomeMessage.setLayoutX(240);
        welcomeMessage.setLayoutY(30);
        welcomeMessage.getChildren().addAll(welcomeTo, A, Z, U, L);
        welcomeMessage.setEffect(shadow);
        WelcomePage.getChildren().add(welcomeMessage);

        // Set back ground factories.
        double scaleOfFactory = 0.6;
        // Obtain image.
        Image factory = new Image(Objects.requireNonNull(
                Game.class.getResource(URI_BASE + "factory.png")).toString());
        // Show three factories.
        for (int i = 0; i < 3; i++) {
            ImageView factoryView = new ImageView(factory);
            // Set the positions of factory images.
            factoryView.setLayoutX(-30 + (400) * i);
            factoryView.setLayoutY(100);
            // Adjust the factory image.
            ColorAdjust adjustFactory = new ColorAdjust();
            // adjustFactory.setBrightness(-0.5);  ** This line could adjust the brightness of the factories.
            factoryView.setEffect(adjustFactory);
            factoryView.setScaleX(scaleOfFactory);
            factoryView.setScaleY(scaleOfFactory);
            // Add factory images
            WelcomePage.getChildren().add(factoryView);
        }


        // Set font for hint messages.
        double setFontSize = 25;
        // Select *number of players*.
        // Initialize a text flow.
        TextFlow setNumOfPlayerTextFlow = new TextFlow();
        // Set text *PLEASE SELECT*.
        Text pleaseSelect0 = new Text("PLEASE SELECT ");
        // Set font.
        pleaseSelect0.setFont(Font.loadFont(FONT, setFontSize));
        // Set text: *NUMBER*.
        Text number = new Text("\nNUMBER");
        // Set font.
        number.setFont(Font.loadFont(FONT, setFontSize));
        // Set colour.
        number.setFill(Color.rgb(64, 132, 165));
        // Set text *OF PLAYERS*
        Text ofPlayers = new Text(" OF PLAYERS");
        // Set font.
        ofPlayers.setFont(Font.loadFont(FONT, setFontSize));
        // Add text to text flow.
        setNumOfPlayerTextFlow.getChildren().addAll(pleaseSelect0, number, ofPlayers);
        // Set alignment.
        setNumOfPlayerTextFlow.setTextAlignment(TextAlignment.CENTER);
        // Set the position of texts.
        setNumOfPlayerTextFlow.setLayoutX(120);
        setNumOfPlayerTextFlow.setLayoutY(530);

        // Select *game type*
        TextFlow setGameTypeTextFlow = new TextFlow();
        Text pleaseSelect1 = new Text("PLEASE SELECT ");
        pleaseSelect1.setFont(Font.loadFont(FONT, setFontSize));
        Text game = new Text("\nGAME");
        game.setFont(Font.loadFont(FONT, setFontSize));
        game.setFill(Color.rgb(226, 126, 73));
        Text type = new Text(" TYPE");
        type.setFont(Font.loadFont(FONT, setFontSize));
        setGameTypeTextFlow.getChildren().addAll(pleaseSelect1, game, type);
        setGameTypeTextFlow.setTextAlignment(TextAlignment.CENTER);
        setGameTypeTextFlow.setLayoutX(550);
        setGameTypeTextFlow.setLayoutY(530);

        // Select *players' type*
        TextFlow setPlayerTypeTextFlow = new TextFlow();
        Text pleaseSelect2 = new Text("PLEASE SELECT ");
        pleaseSelect2.setFont(Font.loadFont(FONT, setFontSize));
        Text players = new Text("\nPLAYERS'");
        players.setFont(Font.loadFont(FONT, setFontSize));
        players.setFill(Color.rgb(220, 78, 77));
        Text type1 = new Text(" Type");
        type1.setFont(Font.loadFont(FONT, setFontSize));
        setPlayerTypeTextFlow.getChildren().addAll(pleaseSelect2, players, type1);
        setPlayerTypeTextFlow.setTextAlignment(TextAlignment.CENTER);
        setPlayerTypeTextFlow.setLayoutX(950);
        setPlayerTypeTextFlow.setLayoutY(530);

        // Add text to welcome page.
        WelcomePage.getChildren().addAll(setNumOfPlayerTextFlow, setGameTypeTextFlow, setPlayerTypeTextFlow);

        WelcomePage.getChildren().add(WelcomePageTemporary);

        // Set select first player buttons. (Default value: 2)
        Group selectFirstPlayerButtons = setFirstPlayerButtons(2);
        // Set select players' type buttons. (Default value: 2)
        Group selectPlayersTypeButtons = setSelectAllPlayersTypeButtons(2);
        // Add these buttons into Temporary page.
        WelcomePageTemporary.getChildren().addAll(selectFirstPlayerButtons, selectPlayersTypeButtons);

        // Set select number of players buttons.
        Group selectNumOfPlayersButtons = setSelectNumOfPlayersButtons();
        // Add these buttons into welcome page.
        WelcomePage.getChildren().add(selectNumOfPlayersButtons);

        // Set select game type buttons.
        Group selectGameTypeButtons = setSelectGameTypeButtons();
        // Add these buttons into welcome page.
        WelcomePage.getChildren().add(selectGameTypeButtons);

        // Set start game button.
        Button startGameButton = new Button();
        // Set text.
        startGameButton.setText("START GAME");
        // Set font.
        startGameButton.setFont(Font.loadFont(FONT, 45));
        // Set shape of button.
        Rectangle buttonShape = new Rectangle();
        buttonShape.setWidth(70);
        buttonShape.setHeight(40);
        buttonShape.setArcWidth(5);
        buttonShape.setArcHeight(10);
        startGameButton.setShape(buttonShape);
        // Set position of button
        startGameButton.setLayoutX(475);
        startGameButton.setLayoutY(630);

        // Set event handler of button.
        startGameButton.setOnMouseClicked(mouseEvent -> {
            int numOfPlayers = getNumOfPlayersFromButton(selectNumOfPlayersButtons);
            boolean isBasicGame = getIsBasicGame(selectGameTypeButtons);
            int firstPlayerIndex = getFirstPlayer((Group) WelcomePageTemporary.getChildren().get(0));
            int[] playerTypes = getSelectedPlayerTypes((Group) (WelcomePageTemporary.getChildren().get(1)));
            for (int i = 0; i < playerTypes.length; i++) {
                Player player = Player.getPlayerByIndex(i + 1);
                PlayerTypesMap.put(player, playerTypes[i]);
            }
            setUpBoard(numOfPlayers, isBasicGame, firstPlayerIndex);
        });

        // Add button into welcome page.
        WelcomePage.getChildren().add(startGameButton);
    }


    /**
     * Set select number of players buttons.
     * (This method will only be called in setWelcomePage.)
     *
     * @return A group contains these button
     */
    private Group setSelectNumOfPlayersButtons() {
        // Set size of buttons.
        double buttonSize = 90;
        // Set inner shadow effect for clicking.
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.RED);
        shadow.setRadius(30);

        // Set the background size of the button.
        BackgroundSize backgroundSize = new BackgroundSize(
                buttonSize,
                buttonSize,
                false,
                false,
                false,
                false);

        // Set two players button button.
        ToggleButton twoPlayersButton = new ToggleButton();
        // Set the size of button.
        twoPlayersButton.setPrefSize(buttonSize, buttonSize);
        // Set the background image of the button.
        BackgroundImage twoPlayersBackgroundImage = new BackgroundImage(
                new Image(Objects.requireNonNull(Game.class.getResource(URI_BASE + "two-players-icon.png")).toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                backgroundSize);
        // Set the background of the button.
        twoPlayersButton.setBackground(new Background(twoPlayersBackgroundImage));
        // Set the position of the button.
        twoPlayersButton.setLayoutX(195);
        twoPlayersButton.setLayoutY(240);
        // Set this button as default button.
        twoPlayersButton.setSelected(true);
        twoPlayersButton.setEffect(shadow);

        // Set three players button.
        ToggleButton threePlayersButton = new ToggleButton();
        threePlayersButton.setPrefSize(buttonSize, buttonSize);
        BackgroundImage threePlayersBackgroundImage = new BackgroundImage(
                new Image(Objects.requireNonNull(
                        Game.class.getResource(URI_BASE + "three-players-icon.png")).toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                backgroundSize);
        threePlayersButton.setBackground(new Background(threePlayersBackgroundImage));
        threePlayersButton.setLayoutX(125);
        threePlayersButton.setLayoutY(390);

        // Set four players button.
        ToggleButton fourPlayersButton = new ToggleButton();
        fourPlayersButton.setPrefSize(buttonSize, buttonSize);
        BackgroundImage fourPlayersButtonBackgroundImage = new BackgroundImage(
                new Image(Objects.requireNonNull(
                        Game.class.getResource(URI_BASE + "four-players-icon.png")).toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                backgroundSize);
        fourPlayersButton.setBackground(new Background(fourPlayersButtonBackgroundImage));
        fourPlayersButton.setLayoutX(265);
        fourPlayersButton.setLayoutY(390);

        // Initialize a toggle group.
        ToggleGroup toggleGroup = new ToggleGroup();

        // Group all the buttons.
        ToggleButton[] buttons = new ToggleButton[] {
                twoPlayersButton,
                threePlayersButton,
                fourPlayersButton
        };

        // Set event handler for every button.
        for (int i = 0; i < 3; i++) {
            // Get the button by index.
            ToggleButton button = buttons[i];
            // Copy of counter.
            int finalI = i;
            button.setOnMousePressed(mouseEvent -> {
                // If the button is not selected
                if (button.getEffect() == null) {
                    // Set effect.
                    button.setEffect(shadow);
                    /* When switching to a different number of players: update
                     * Select players' type area. */
                    refreshPlayersTypeArea(finalI + 2);
                    // Unselect other buttons.
                    for (ToggleButton otherButton : buttons) {
                        if (!button.equals(otherButton)) {
                            otherButton.setEffect(null);
                        }
                    }
                }
            });
            // Add current button to toggle group.
            button.setToggleGroup(toggleGroup);
        }

        Group playerButtonGroup = new Group();
        // Add buttons to a group.
        playerButtonGroup.getChildren().addAll(twoPlayersButton, threePlayersButton, fourPlayersButton);

        // Enable mute functionality.
        setMute();

        return playerButtonGroup;
    }


    /**
     * Give the number of players, refresh select players' type area.
     * (This method will only be called in setWelcomePage.)
     *
     * @param numOfPlayers Number of players in game.
     */
    private void refreshPlayersTypeArea(int numOfPlayers) {
        WelcomePageTemporary.getChildren().clear();
        WelcomePageTemporary.getChildren().add(setFirstPlayerButtons(numOfPlayers));
        WelcomePageTemporary.getChildren().add(setSelectAllPlayersTypeButtons(numOfPlayers));
    }


    /**
     * Give a group of setting number of players buttons, get the selected button.
     * (This method will only be called in setWelcomePage.)
     *
     * @param buttonsGroup Group contains selection button
     * @return Number of players selected
     */
    private int getNumOfPlayersFromButton(Group buttonsGroup) {
        int numOfPlayers = 0;

        // Iterate through group:
        for (int i = 0; i < buttonsGroup.getChildren().size(); i++) {
            // Get button.
            Node node = buttonsGroup.getChildren().get(i);
            if (node instanceof ToggleButton) {
                // If the effect is not null (selected):
                if (node.getEffect() != null) {
                    // Return number of players.
                    numOfPlayers = i + 2;
                    break;
                }
            }
        }

        return numOfPlayers;
    }


    /**
     * Set buttons for select basic game or variant game.
     *
     * @return Group of buttons.
     *
     * @author Qm9nb25nIFdhbmc=
     */
    private Group setSelectGameTypeButtons() {
        Group gameTypeButtons = new Group();
        double buttonWidth = 240;
        double buttonHeight = 80;

        // Set inner shadow effect for clicking.
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.CYAN);
        shadow.setRadius(30);

        // Set the background image size.
        BackgroundSize buttonBackgroundSize = new BackgroundSize(
                buttonWidth,
                buttonHeight,
                false,
                false,
                false,
                false);
        // Set "select basic button".
        ToggleButton selectBasicButton = new ToggleButton();
        // Set the size of button.
        selectBasicButton.setPrefSize(buttonWidth, buttonHeight);
        // Set the background image of the "select basic button".
        Image basicButtonImage = new Image(Objects.requireNonNull(
                Game.class.getResource(URI_BASE + "select-basic-player-board.png")).toString());
        BackgroundImage basicButtonBackgroundImage = new BackgroundImage(
                basicButtonImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                buttonBackgroundSize);
        // Set the background of "select basic button".
        selectBasicButton.setBackground(new Background(basicButtonBackgroundImage));
        // Set the position of the button.
        selectBasicButton.setLayoutX(520);
        selectBasicButton.setLayoutY(270);
        // Set this button as default button.
        selectBasicButton.setSelected(true);
        selectBasicButton.setEffect(shadow);

        /* Set "select variant button".
         * (Implementation is similar to "select basic button", if there is any
         * problem, refer to "select basic button" section. */
        ToggleButton selectVariantButton = new ToggleButton();
        selectVariantButton.setPrefSize(buttonWidth, buttonHeight);
        Image variantButtonImage = new Image(Objects.requireNonNull(
                Game.class.getResource(URI_BASE + "select-variant-player-board.png")).toString());
        BackgroundImage variantButtonBackgroundImage = new BackgroundImage(
                variantButtonImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                buttonBackgroundSize);
        selectVariantButton.setBackground(new Background(variantButtonBackgroundImage));
        selectVariantButton.setLayoutX(520);
        selectVariantButton.setLayoutY(390);

        // Initialize a toggle group for buttons.
        ToggleGroup toggleGroup = new ToggleGroup();
        // Group buttons.
        ToggleButton[] buttons = new ToggleButton[] {
                selectBasicButton,
                selectVariantButton
        };

        // Set event handler for buttons.
        setEventHandlerForSettingButtons(shadow, toggleGroup, buttons);

        // Add buttons to gameTypeButtons group.
        gameTypeButtons.getChildren().addAll(selectBasicButton, selectVariantButton);

        return gameTypeButtons;
    }


    /**
     * Give a group of game type buttons, get the type of the game.
     * (This method will only be called in setWelcomePage.)
     *
     * @param group Group contains game type buttons
     * @return Ture if basic game button is selected
     */
    private boolean getIsBasicGame(Group group) {
        if (group.getChildren().get(0) instanceof ToggleButton) {
            // Check if the first button is selected.
            return group.getChildren().get(0).getEffect() != null;
        }
        return false;
    }


    /**
     * Set select player's type buttons (for a single player).
     * (This method will only be called in setWelcomePage.)
     *
     * @return A group contains select player's type buttons.
     */
    private Group setSelectPlayersTypeButtons() {
        Group playerTypeButtons = new Group();
        double buttonSize = 50;
        // Set inner shadow effect for clicking.
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.YELLOW);
        shadow.setRadius(20);

        // Set toggle group for buttons.
        ToggleGroup toggleGroup = new ToggleGroup();

        // Set the button name.
        String[] buttonNames = new String[] {
                "human.png",
                "basic-ai.png",
                "advanced-ai.png"
        };

        // Initialize an array for storing buttons.
        ToggleButton[] buttons = new ToggleButton[3];

        // Initialize buttons one by one:
        for (int i = 0; i < 3; i++) {
            // Initialize a button.
            ToggleButton button = new ToggleButton();
            // If current button is the first button:
            if (i == 0) {
                // Set as default button.
                button.setSelected(true);
                button.setEffect(shadow);
            }
            // Set the size of the button.
            button.setPrefSize(buttonSize, buttonSize);
            // Get the name of the button.
            String buttonName = buttonNames[i];
            // Get the image of the button.
            Image buttonImage = new Image(
                    Objects.requireNonNull(Game.class.getResource(URI_BASE + buttonName)).toString());
            // Set background size.
            BackgroundSize buttonBackgroundSize = new BackgroundSize(
                    buttonSize,
                    buttonSize,
                    false,
                    false,
                    false,
                    false);
            // Set the background image of the button.
            BackgroundImage buttonBackgroundImage = new BackgroundImage(
                    buttonImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    buttonBackgroundSize);
            // Set the background of the current button.
            button.setBackground(new Background(buttonBackgroundImage));
            // Set the position of the current button.
            button.setLayoutX(40 + 60 * i);
            // Set id of button.
            button.setId("Button" + i);
            // Store the button.
            buttons[i] = button;

            playerTypeButtons.getChildren().add(button);
        }

        setEventHandlerForSettingButtons(shadow, toggleGroup, buttons);

        return playerTypeButtons;
    }


    /**
     * Set select player's type buttons for all players.
     * (This method will only be called in setWelcomePage.)
     *
     * @param numOfPlayers Number of list of buttons to display.
     * @return A group contains buttons.
     */
    private Group setSelectAllPlayersTypeButtons(int numOfPlayers) {
        Group allButtons = new Group();

        // Store vertical gaps and y positions of buttons.
        int[][] verticalGapsAndYPositions = new int[][] {
                {120, 280},
                {70, 270},
                {60, 250}
        };
        // Get vertical gap.
        double verticalGap = verticalGapsAndYPositions[numOfPlayers - 2][0];
        // Get horizontal translation value.
        double yPosition = verticalGapsAndYPositions[numOfPlayers - 2][1];

        for (int i = 0; i < numOfPlayers; i++) {
            // Get a group of buttons.
            Group buttons = setSelectPlayersTypeButtons();
            // Set position of group of tiles.
            buttons.setLayoutY(verticalGap * i);
            // Add to all buttons group.
            allButtons.getChildren().add(buttons);
        }

        // Set the position of all buttons.
        allButtons.setLayoutX(950);
        allButtons.setLayoutY(yPosition);

        return allButtons;
    }


    /**
     * Get selected player types by inspecting buttons.
     * (This method will only be called in setWelcomePage.)
     * @param group Group contains players' type selection buttons.
     * @return An array that contains player types.
     *         (0: human, 1: basic AI, 2: advanced AI)
     */
    private int[] getSelectedPlayerTypes(Group group) {
        // Get the number of players in game.
        int numOfPlayers = group.getChildren().size();
        // Initialize an array for storing players' type.
        int[] playerTypes = new int[numOfPlayers];
        // Iterate through group:
        for (int i = 0; i < numOfPlayers; i++) {
            // Get an list of buttons.
            Node playerTypeButtons = group.getChildren().get(i);
            // Check the button one by one to find the clicked button.
            for (int j = 0; j < 3; j++) {
                String id = "#Button" + j;
                Node buttonNode = playerTypeButtons.lookup(id);
                if (buttonNode instanceof ToggleButton) {
                    if (buttonNode.getEffect() != null) {
                        playerTypes[i] = j;
                    }
                }
            }
        }

        return playerTypes;
    }


    /**
     * Set event handler for buttons.
     * (This method will only be called in setWelcomePage.)
     *
     * @param shadow Shadow effect when selected.
     * @param toggleGroup Toggle group to group.
     * @param buttons A list of buttons to set event handler.
     */
    private void setEventHandlerForSettingButtons(DropShadow shadow, ToggleGroup toggleGroup, ToggleButton[] buttons) {
        // Iterate through all given buttons:
        for (ToggleButton button : buttons) {
            button.setOnMousePressed(mouseEvent -> {
                // Check if a button is selected by checking if it has effect:
                // If a button is not selected:
                if (button.getEffect() == null) {
                    // Set the shadow effect of button.
                    button.setEffect(shadow);
                    // Unselect other buttons.
                    for (ToggleButton otherButton : buttons) {
                        if (!button.equals(otherButton)) {
                            otherButton.setEffect(null);
                        }
                    }
                }
            });
            button.setToggleGroup(toggleGroup);
        }
    }


    /**
     * Set first player buttons.
     * (This method will only be called in setWelcomePage.)
     *
     * @param numOfPlayers Number of players
     * @return A group contains buttons that can set first player
     */
    private Group setFirstPlayerButtons(int numOfPlayers) {
        // Initialize buttons group.
        Group buttonsGroup = new Group();
        // Initialize a toggle group.
        ToggleGroup toggleGroup = new ToggleGroup();
        // Set size of buttons.
        double buttonSize = 50;

        // Set an array of buttons for storing.
        ToggleButton[] buttons = new ToggleButton[numOfPlayers];

        // Set button size.
        BackgroundSize firstPlayerTokenBackgroundSize = new BackgroundSize(
                buttonSize,
                buttonSize,
                false,
                false,
                false,
                false);
        // Set background image (empty) of button's background.
        BackgroundImage basicImage = new BackgroundImage(
                new Image(Objects.requireNonNull(
                        Game.class.getResource(URI_BASE + "select-first-player-background.png")).toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                firstPlayerTokenBackgroundSize
        );
        // Set first player token for button's background.
        BackgroundImage firstPlayerTokenImage = new BackgroundImage(
                new Image(Objects.requireNonNull(
                        Game.class.getResource(URI_BASE + "first-player-token.png")).toString()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                firstPlayerTokenBackgroundSize);

        // Set empty background.
        Background basicBackground = new Background(basicImage);
        // Set first player token background.
        Background firstPlayerTokenBackground = new Background(firstPlayerTokenImage);

        int[][] verticalGapsAndYPositions = new int[][] {
                {120, 280},
                {70, 270},
                {60, 250}
        };
        // Get the vertical gap.
        double verticalGap = verticalGapsAndYPositions[numOfPlayers - 2][0];
        // Get the horizontal translation value.
        double yPosition = verticalGapsAndYPositions[numOfPlayers - 2][1];

        // Set numOfPlayers of buttons:
        for (int i = 0; i < numOfPlayers; i++) {
            // Initialize a button.
            ToggleButton button = new ToggleButton();
            // Set size of button.
            button.setPrefSize(buttonSize, buttonSize);
            // Set the first button as default button.
            if (i == 0) {
                button.setBackground(firstPlayerTokenBackground);
                button.setText("P" + (i + 1));
                button.setFont(Font.loadFont(FONT, 15));
                button.setTextFill(Color.color(0, 0, 0, 0));
            } else {
                button.setBackground(basicBackground);
                button.setText("P" + (i + 1));
                button.setFont(Font.loadFont(FONT, 15));
            }
            // Set position of button.
            button.setLayoutY(verticalGap * i);
            // Add button to buttons array.
            buttons[i] = button;
            // Add the button to button group.
            buttonsGroup.getChildren().add(button);
        }

        // Set event handler:
        for (ToggleButton button : buttons) {
            button.setOnMousePressed(mouseEvent -> {
                // If the button is not selected before:
                if (!button.isSelected()) {
                    // Set the text invisible.
                    button.setTextFill(Color.color(0, 0, 0, 0));
                    // Set the background of the button as first player token.
                    button.setBackground(null);
                    button.setBackground(firstPlayerTokenBackground);
                    for (ToggleButton otherButton : buttons) {
                        if (!button.equals(otherButton)) {
                            otherButton.setTextFill(Color.BLACK);
                            otherButton.setBackground(null);
                            otherButton.setBackground(basicBackground);
                        }
                    }
                }
            });
            // Add button into toggle group.
            button.setToggleGroup(toggleGroup);
        }

        // Set the position of buttons.
        buttonsGroup.setLayoutX(930);
        buttonsGroup.setLayoutY(yPosition);

        return buttonsGroup;
    }


    /**
     * Get the first player form group of select first player buttons.
     * (This method will only be called in setWelcomePage.)
     *
     * @param group Group of selection buttons.
     * @return Player index of first player (1, 2, ...)
     */
    private int getFirstPlayer(Group group) {
        // Get number of players in the game.
        int numOfPlayers = group.getChildren().size();
        for (int i = 0; i < numOfPlayers; i++) {
            Node node = group.getChildren().get(i);
            if (node instanceof ToggleButton) {
                // If the text of the button invisible, it's selected button.
                Paint textFill = ((ToggleButton) node).getTextFill();
                if (!textFill.isOpaque()) {
                    return i + 1;
                }
            }
        }

        return 0;
    }
//////////////////////////  End Of Welcome Page  ///////////////////////////////



/////////////////////////////  Game Over Page  /////////////////////////////////
    /**
     * Play evaluation step at the end of the game (check 5 linked row ...)
     *
     * @param playerBoardIndex Player board index to play animation.
     */
    private void playEvaluationAnimation(int playerBoardIndex) {
        SwitchButton.setDisable(true);
        // If the given player board index is less than or equal to the number
        // of players:
        if (playerBoardIndex <= GAME_BOARD.getNumOfPlayers()) {
            /* If the player board equal to number of players
             * (finished playing animation in the last player board.) */
            if (playerBoardIndex == GAME_BOARD.getNumOfPlayers()) {
                // Start a final game over page.
                startGameOverPage();
            // If the player board is not the last player board:
            } else {
                SequentialTransition sequentialTransition = new SequentialTransition();
                // Switch player board.
                GAME_BOARD.setTurn(Player.getPlayerByIndex(playerBoardIndex + 1));
                // Update view of player board.
                updatePlayerBoard(true);
                // Get player board by player board index
                PlayerBoard playerBoard = GAME_BOARD.playerBoards[playerBoardIndex];
                // Get current score for moving score marker.
                int currScore = playerBoard.getScore();

                // Pop tiles in complete rows.
                ArrayList<Integer> completeRows = playerBoard.getCompleteRowsInMosaic();
                for (int rowIndex : completeRows) {
                    ParallelTransition parallelTransition = new ParallelTransition();
                    double rowY = FIRST_ROW_STORAGE_MOSAIC_Y + (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * rowIndex;
                    for (Node node : MosaicArea.getChildren()) {
                        if (node.getLayoutY() == rowY) {
                            parallelTransition.getChildren().add(playPopTileAnimation(node));
                        }
                    }
                    sequentialTransition.getChildren().add(parallelTransition);
                    currScore += 2;
                    // Play animation fro adding scores.
                    sequentialTransition.getChildren().add(playScoreMarkerMoveAnimation(currScore));
                }

                // Pop tiles in complete columns.
                ArrayList<Integer> completeCols = playerBoard.getCompleteColumnsInMosaic();
                for (int colIndex : completeCols) {
                    ParallelTransition parallelTransition = new ParallelTransition();
                    double colX = FIRST_COL_X_MOSAIC + (TILE_SIZE + TILE_GAP_STORAGE_MOSAIC) * colIndex;
                    for (Node node : MosaicArea.getChildren()) {
                        if (node.getLayoutX() == colX) {
                            parallelTransition.getChildren().add(playPopTileAnimation(node));
                        }
                    }
                    sequentialTransition.getChildren().add(parallelTransition);
                    currScore += 7;
                    // Play animation fro adding scores.
                    sequentialTransition.getChildren().add(playScoreMarkerMoveAnimation(currScore));
                }

                // Pop tiles in complete pattern.
                ArrayList<Tile> completePattern = playerBoard.getCompletePattern();
                for (Tile tile : completePattern) {
                    ParallelTransition parallelTransition = new ParallelTransition();
                    for (Node node : MosaicArea.getChildren()) {
                        DraggableTile currTile = (DraggableTile) node;
                        if (currTile.tile == tile) {
                            parallelTransition.getChildren().add(playPopTileAnimation(node));
                        }
                    }
                    sequentialTransition.getChildren().add(parallelTransition);
                    currScore += 10;
                    // Play animation fro adding scores.
                    sequentialTransition.getChildren().add(playScoreMarkerMoveAnimation(currScore));
                }

                playerBoard.addBonusScoreAtTheEndOfTheGame();
                sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(1)));
                // If the animation is finished playing, switch to next player board.
                sequentialTransition.setOnFinished(actionEvent -> playEvaluationAnimation(playerBoardIndex + 1));
                sequentialTransition.play();
            }
        }
    }


    /**
     * Start a final game over page.
     */
    private void startGameOverPage() {
        // This code block finds the winner(s).
        int maxScore = 0;
        int maxNumOfCompleteRows = 0;
        // Create an array that records the winner.
        ArrayList<Player> winners = new ArrayList<>();
        // Iterate through game board:
        for (int i = 0; i < GAME_BOARD.getNumOfPlayers(); i++) {
            // Get player board by index.
            PlayerBoard currPlayerBoard = GAME_BOARD.playerBoards[i];
            // If the score of current score is higher than the maximum score:
            if (currPlayerBoard.getScore() > maxScore) {
                // Substitute the max score.
                maxScore = currPlayerBoard.getScore();
                // Remove added winners.
                winners.clear();
                // Add player of current player board to winner array list.
                winners.add(currPlayerBoard.getPlayer());
                // Update number of complete rows.
                maxNumOfCompleteRows = currPlayerBoard.getCompleteRowsInMosaic().size();
            // If the score of current player board is equal to maximum score.
            } else if (currPlayerBoard.getScore() == maxScore) {
                // Get number of complete rows:
                int numOfCompleteRows = currPlayerBoard.getCompleteRowsInMosaic().size();
                // If number of complete rows is greater than the max value:
                if (numOfCompleteRows > maxNumOfCompleteRows) {
                    // Remove added winners.
                    winners.clear();
                    // Add current player to winners array list.
                    winners.add(currPlayerBoard.getPlayer());
                    // Update number of players.
                    maxNumOfCompleteRows = numOfCompleteRows;
                // If the number of complete rows is equal to max value:
                } else if (numOfCompleteRows == maxNumOfCompleteRows) {
                    // Add current player to winners array list.
                    winners.add(currPlayerBoard.getPlayer());
                }
            }
        }

        // Get number of players.
        int numOfPlayers = GAME_BOARD.getNumOfPlayers();

        TemporaryBoard.getChildren().add(getAllBoardsView());

        // Display a winner message.
        TextFlow winner = new TextFlow();

        // Set the font size of message.
        double fontSize = 60;

        // Adjust view message for different number of player boards.
        // Set text and font.
        Text winnerText = new Text("WINNER: ");
        if (numOfPlayers == 4) {
            winnerText = new Text("WINNER: \n");
        }
        winnerText.setFont(Font.loadFont(FONT, fontSize));
        winner.getChildren().add(winnerText);

        // Adjust view of winner's name for different number of winners.
        if (winners.size() == 1) {
            // Get the winner.
            Player player = winners.get(0);
            // Set text for winner.
            Text playerText = new Text(player.toString());
            playerText.setFont(Font.loadFont(FONT, fontSize));
            playerText.setFill(Color.GOLD);
            playerText.setEffect(new Bloom());
            playerText.setStroke(Color.BLACK);
            playerText.setStrokeWidth(1);
            winner.getChildren().add(playerText);
        } else {
            TextFlow players = new TextFlow();
            for (int i = 0; i < winners.size(); i++) {
                Player player = winners.get(i);
                Text playerText;
                if (i == winners.size() - 1) {
                    playerText = new Text(player.toString());
                } else {
                    playerText = new Text(player.toString() + ", ");
                }
                if (numOfPlayers == 4) {
                    playerText = new Text(player + "\n");
                }
                playerText.setFont(Font.loadFont(FONT, fontSize));
                playerText.setFill(Color.SILVER);
                playerText.setEffect(new Bloom());
                playerText.setStroke(Color.BLACK);
                playerText.setStrokeWidth(1);
                players.getChildren().add(playerText);
            }
            winner.getChildren().add(players);
        }


        // Add quit game button.
        Button quitGame = new Button(" QUIT GAME ");
        quitGame.setFont(Font.loadFont(FONT, 30));
        quitGame.setOnMouseClicked(mouseEvent -> {
            Platform.exit();
            System.exit(0);
        });


        // Add start new game button if players want to start a new game.
        Button newGame = new Button("  NEW GAME ");
        newGame.setFont(Font.loadFont(FONT, 30));
        newGame.setOnMouseClicked(mouseEvent -> {
            root.getChildren().clear();
            WelcomePage.getChildren().clear();
            WelcomePageTemporary.getChildren().clear();
            TemporaryBoard.getChildren().clear();
            PlayerBoard.getChildren().clear();
            StorageArea.getChildren().clear();
            MosaicArea.getChildren().clear();
            FloorArea.getChildren().clear();
            ScoreArea.getChildren().clear();
            FactoriesArea.getChildren().clear();
            CentreArea.getChildren().clear();

            setWelcomePage();
        });

        // Set the position of two buttons
        if (numOfPlayers == 2 || numOfPlayers == 3) {
            switch (winners.size()) {
                case 1 -> {
                    winner.setLayoutX(350);
                    winner.setLayoutY(20);
                }
                case 2 -> {
                    winner.setLayoutX(230);
                    winner.setLayoutY(20);
                }
                case 3 -> {
                    winner.setLayoutX(80);
                    winner.setLayoutY(20);
                }
            }
            quitGame.setLayoutX(200);
            quitGame.setLayoutY(700);
            newGame.setLayoutX(800);
            newGame.setLayoutY(700);
        } else {
            winner.setLayoutX(900);
            winner.setLayoutY(20);
            quitGame.setLayoutX(930);
            quitGame.setLayoutY(500);
            newGame.setLayoutX(930);
            newGame.setLayoutY(600);
        }



        TemporaryBoard.getChildren().add(winner);
        TemporaryBoard.getChildren().add(quitGame);
        TemporaryBoard.getChildren().add(newGame);

        // Set a easter for displaying authorship information.
        Button showAuthorshipInfoButton = new Button();
        showAuthorshipInfoButton.setMinWidth(30);
        showAuthorshipInfoButton.setMinHeight(30);
        showAuthorshipInfoButton.setStyle(
                "-fx-border-color: transparent;" +
                "-fx-border-width: 0;" +
                "-fx-background-radius: 0;" +
                "-fx-background-color: transparent;");
        showAuthorshipInfoButton.setTranslateX(BOARD_WIDTH - 30);
        showAuthorshipInfoButton.setTranslateY(BOARD_HEIGHT - 30);
        TextFlow authorshipInfo = new TextFlow();

        // Ordered by first character of last name.
        Text authorText = new Text("Author: \n");
        authorText.setFont(Font.font("Savoye LET", 30));
        Text author = new Text("Ym9nb25nd2FuZ3phbmVoYWlkZXJ5dXhpbnN1bg==\n");
        author.setFont(Font.font("Savoye LET", 30));
        Text license = new Text("GPL License");
        license.setFont(Font.font("HelveticaNeue", 10));
        authorshipInfo.getChildren().addAll(authorText, author, license);
        authorshipInfo.setTextAlignment(TextAlignment.CENTER);
        authorshipInfo.setLayoutX(1150);
        authorshipInfo.setLayoutY(610);
        showAuthorshipInfoButton.setOnMousePressed(mouseEvent -> {
            try {
                TemporaryBoard.getChildren().remove(authorshipInfo);
            } catch (Exception ignored) {
            }
            TemporaryBoard.getChildren().add(authorshipInfo);
        });
        TemporaryBoard.getChildren().add(showAuthorshipInfoButton);
    }


    /**
     * Get view of all boards.
     *
     * @return A group contains all boards
     */
    public Group getAllBoardsView() {
        Group boards = new Group();

        // Get number of players.
        int numOfPlayers = GAME_BOARD.getNumOfPlayers();

        // Initialize an array for saving board images.
        WritableImage[] boardImages = new WritableImage[numOfPlayers];

        // Purpose: Add board images to board images array.
        // Iterate through player boards:
        for (int i = 0; i < numOfPlayers; i++) {
            setPlayerBoard(i, true);
            // Get the snap shot of current board.
            WritableImage originalSnapShotOfBoard = root.snapshot(new SnapshotParameters(), null);
            // Resize the board.
            WritableImage snapShotOfBoard = new WritableImage(
                    originalSnapShotOfBoard.getPixelReader(),
                    18,    // Start x
                    45,   // Start y
                    675,  // Width
                    675); // Height
            // Add board image to image array.
            boardImages[i] = snapShotOfBoard;
        }

        // Set background image.
        Image backGroundImage = new Image(Objects.requireNonNull(
                Game.class.getResource("assets/" + "board-background.jpg")).toString());
        TemporaryBoard.getChildren().clear();
        TemporaryBoard.getChildren().add(new ImageView(backGroundImage));

        // Initialize board positions.
        double[][][] boardPositions = new double[][][] {
                // two players
                new double[][] {
                        new double[] {-10, 50, 0.8},
                        new double[] {600, 50, 0.8},
                },
                // three players
                new double[][] {
                        new double[] {-120, 30, 0.6},
                        new double[] {300, 30, 0.6},
                        new double[] {720, 30, 0.6},
                },
                // four players
                new double[][] {
                        new double[] {-120, -145, 0.55},
                        new double[] {270, -145, 0.55},
                        new double[] {-120, 235, 0.55},
                        new double[] {270, 235, 0.55},
                }
        };

        // Iterate through boardImages:
        for (int i = 0; i < numOfPlayers; i++) {
            // Add the image to the final page.
            WritableImage boardImage = boardImages[i];
            Group boardTextView = new Group();
            ImageView boardView = new ImageView(boardImage);
            Text boardIndex = new Text("BOARD " + (i + 1));
            boardIndex.setFont(Font.loadFont(FONT, 25));
            boardIndex.setFill(Color.rgb(0, 111, 138));
            boardIndex.setLayoutX(40);
            boardIndex.setLayoutY(395);
            boardTextView.getChildren().add(boardView);
            boardTextView.getChildren().add(boardIndex);

            // Set the position and size of the player boards based on boardPositions.
            boardTextView.setLayoutX(boardPositions[numOfPlayers - 2][i][0]);
            boardTextView.setLayoutY(boardPositions[numOfPlayers - 2][i][1]);
            boardTextView.setScaleX(boardPositions[numOfPlayers - 2][i][2]);
            boardTextView.setScaleY(boardPositions[numOfPlayers - 2][i][2]);
            boards.getChildren().add(boardTextView);
        }

        return boards;
    }


    /**
     * Play a pop up and down animation.
     *
     * @param node The node to be bind with this pop animation.
     * @return Pop animation
     */
    private Animation playPopTileAnimation(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.seconds(0.8));
        scaleTransition.setByX(0.2);
        scaleTransition.setByY(0.2);
        scaleTransition.setNode(node);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);

        return scaleTransition;
    }
////////////////////////  End Of Game Over Page  ///////////////////////////////


    @Override
    public void start(Stage stage) {
        stage.setTitle("Azul");
        // Set the window not resizable.
        stage.setResizable(false);

        // Set icon of game.
        String GameIconURI = Objects.requireNonNull(
                Game.class.getResource("assets/game-icon.png")).toString();
        stage.getIcons().add(new Image(GameIconURI));

        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);

        // Start game.
        setWelcomePage();

        stage.setScene(scene);
        stage.show();
    }
}