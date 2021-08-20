package comp1110.ass2;

import java.util.ArrayList;

/*******************************************************************************
 *                           Class: GreedyMoveAI                               *
 *                 This class is contributed by Qm9nb25n                       *
 ******************************************************************************/

public class GreedyMoveAI extends AI{

    GreedyMoveAI(GameBoard gameBoard, Player player) {
        this.gameBoard = gameBoard;
        this.player = player;
    }


    /**
     * Initialize an AI using greedy strategy.
     *
     * @param gameBoard Current game board.
     * @return An AI using greedy strategy
     */
    public static GreedyMoveAI initGreedyMoveAI(GameBoard gameBoard, Player player) {
        return new GreedyMoveAI(gameBoard, player);
    }


    /**
     * Generate next drafting move according to current game board.
     *
     * @return The drafting move decided by AI
     */
    @Override
    public String generateDraftingMove() {
        ArrayList<String> draftingMoves = getLegalDraftingMoves();
        String bestMove = draftingMoves.get(0);
        double bestScore = -10000;
        for (String move : draftingMoves) {
            GameBoard gameBoardTest = GameBoard.parseFromGameState(gameBoard.toGameState());
            gameBoardTest.applyDraftingMove(move);
            PlayerBoard currPlayerBoard = gameBoardTest.getPlayerBoardByPlayer(gameBoard.turn);
            double currBoardEvalScore = draftingMoveEvaluation(currPlayerBoard);
            if (currBoardEvalScore > bestScore) {
                bestMove = move;
                bestScore = currBoardEvalScore;
            }
        }

        return bestMove;
    }


    /**
     * Generate next tiling move according to current game board.
     *
     * @return The tiling move decided by AI
     */
    @Override
    public ArrayList<String> generateTilingMoves() {
        // Get all legal moves.
        ArrayList<String> legalMoves = getLegalTilingMoves();
        // If the game is variant game:
        if (!gameBoard.isBasicGame()) {
            // Get all moves combination.
            ArrayList<ArrayList<String>> allMoves = getAllReasonableTilingMovesForVariantGame(legalMoves);

            ArrayList<String> bestMoves = new ArrayList<>();
            int bestScore = -10000;
            // Iterate through all moves, find the best legal moves.
            for (ArrayList<String> moves : allMoves) {
                try {
                    GameBoard testGameBoard = GameBoard.parseFromGameState(gameBoard.toGameState());
                    testGameBoard.castToVariantBoards();
                    testGameBoard.applyMultipleTilingMoves(moves);

                    int scoreAfterTiling = testGameBoard.getPlayerBoardByPlayer(gameBoard.turn).getScore();
                    if (scoreAfterTiling > bestScore) {
                        bestMoves = moves;
                        bestScore = scoreAfterTiling;
                    }
                } catch (Exception ignored) {}
            }
            return bestMoves;
        // If the game is basic game:
        } else {
            // Return all legal moves.
            return legalMoves;
        }
    }
}
