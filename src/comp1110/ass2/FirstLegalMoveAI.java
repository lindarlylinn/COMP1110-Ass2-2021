package comp1110.ass2;

import java.util.*;

public class FirstLegalMoveAI extends AI {

    FirstLegalMoveAI(GameBoard gameBoard, Player player) {
        this.player = player;
        this.gameBoard = gameBoard;
    }

    /**
     * Initialize an AI always applies first move.
     *
     * @param gameBoard Current game board
     * @return An AI using first legal move
     *
     * @author WmFuZSBIYWlkZXI=, Qm9nb25nIFdhbmc= (Modified)
     */
    public static FirstLegalMoveAI initFirstLegalMoveAI(GameBoard gameBoard, Player player) {
        return new FirstLegalMoveAI(gameBoard, player);
    }


    /**
     * Generate next drafting move according to current game board.
     *
     * @return The drafting move decided by AI
     *
     * @author WmFuZSBIYWlkZXI=
     */
    @Override
    public String generateDraftingMove() {
        ArrayList<String> draftingMoves = getLegalDraftingMoves();
        return draftingMoves.get(0);
    }


    /**
     * Generate next tiling move according to current game board.
     *
     * @return The tiling move decided by AI
     *
     * @author WmFuZSBIYWlkZXI=
     */
    @Override
    public ArrayList<String> generateTilingMoves() {
        // Get all legal moves.
        ArrayList<String> legalMoves = getLegalTilingMoves();
        // If the game is variant game:
        if (!gameBoard.isBasicGame()) {
            // Get all moves combination.
            ArrayList<ArrayList<String>> allMoves = getAllReasonableTilingMovesForVariantGame(legalMoves);

            // Iterate through all moves, find the first legal moves.
            for (ArrayList<String> moves : allMoves) {
                try {
                    GameBoard testGameBoard = GameBoard.parseFromGameState(gameBoard.toGameState());
                    testGameBoard.castToVariantBoards();
                    testGameBoard.applyMultipleTilingMoves(moves);
                    return moves;
                } catch (Exception ignored) {
                }
            }
            return null;
        // If the game is basic game:
        } else {
            // Return all legal moves.
            return legalMoves;
        }
    }
}