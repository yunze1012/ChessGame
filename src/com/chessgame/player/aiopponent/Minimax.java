package com.chessgame.player.aiopponent;

import com.chessgame.board.ChessBoard;
import com.chessgame.movement.Move;
import com.chessgame.movement.BoardUpdate;

public class Minimax implements Algorithms{
    private final BoardScore boardScore;
    private final int treeLevel;

    public Minimax(final int treeLevel) {
        this.boardScore = new EvaluateBoardScore();
        this.treeLevel = treeLevel;
    }

    @Override
    public Move runAlgorithm(ChessBoard board) {
        final long startTime = System.currentTimeMillis(); // to take account how long to execute a move
        Move bestMove = null;
        int currentHighestValue = Integer.MIN_VALUE;
        int currentLowestValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.getCurrentMovingPlayer() + " THINKING WITH TREE LEVEL = " + this.treeLevel); // DEBUG PURPOSE
        // number of moves available for current player:
        int currentPlayerNumMoves = board.getCurrentMovingPlayer().getLegalMoves().size();
        // check all current player's possible legal moves:
        for(final Move move : board.getCurrentMovingPlayer().getLegalMoves()) {
            // We want to apply the algorithm to the next board after the current player has moved:
            final BoardUpdate update = board.getCurrentMovingPlayer().makeMove(move); // update the current board
            // if the move is made successfully:
            if(update.getMoveStatus().isCompleted()) {
                // if it is currently the White player moving ("enemy" for the AI opponent):
                if(board.getCurrentMovingPlayer().getTeam().isWhite()) {
                    // then for the AI's next move, you will try to minimize the value so BLACK team (AI) can win
                    //  since a negative score means AI opponent is winning:
                    currentValue = minValue(update.getUpdatedBoard(), this.treeLevel - 1);
                }
                // if it is currently the Black player moving (AI moving):
                else {
                    // then just do the opposite for the White player:
                    currentValue = maxValue(update.getUpdatedBoard(), this.treeLevel - 1);
                }
                // update highest current value and best move if white is playing and current value is bigger than
                //  previous highest value (it means that white has the best move right now):
                if(board.getCurrentMovingPlayer().getTeam().isWhite() && currentValue >= currentHighestValue) {
                    currentHighestValue = currentValue;
                    bestMove = move;
                }
                // update lowest current value and best move if black is player and current value is smaller than
                //  previous lowest value (it means black has the best move right now):
                else if(board.getCurrentMovingPlayer().getTeam().isBlack() && currentValue <= currentLowestValue) {
                    currentLowestValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long elapsedTime = System.currentTimeMillis() - startTime; // time for algorithm execution
        return bestMove;
    }

    @Override
    public String toString() {
        return "Minimax";
    }

    // Basic Algorithm Process: get the min or max value at the lowest tree level (depending if odd or even level) and
    //  propagate that value back up to the top of the tree using minValue and maxValue's mutual recursion
    // minValue() returns the minimum value on the specified tree level.
    public int minValue(final ChessBoard board, final int treeLevel) {
        // TO STOP THE MINIMIZING PROCESS:
        if(treeLevel == 0 || isGameOver(board)) {
            return this.boardScore.score(board, treeLevel);
        }
        int currentLowestValue = Integer.MAX_VALUE; // current seen lowest value in this level
        // check all current player's possible legal moves:
        for(final Move move : board.getCurrentMovingPlayer().getLegalMoves()) {
            // We want to apply the algorithm to the next board after the current player has moved:
            final BoardUpdate update = board.getCurrentMovingPlayer().makeMove(move); // update the current board
            // if the move is made successfully:
            if(update.getMoveStatus().isCompleted()) {
                // when alternating between tree levels, we alternate between finding the minimum and maximum value too:
                final int currentValue = maxValue(update.getUpdatedBoard(), treeLevel - 1);
                if(currentValue <= currentLowestValue) {
                    currentLowestValue = currentValue;
                }
            }
        }
        return currentLowestValue;
    }

    // maxValue() returns the maximum value on the specified tree level.
    public int maxValue(final ChessBoard board, final int treeLevel) {
        // TO STOP THE MAXIMIZING PROCESS:
        if(treeLevel == 0 || isGameOver(board)) {
            return this.boardScore.score(board, treeLevel);
        }
        int currentHighestValue = Integer.MIN_VALUE; // current seen lowest value in this level
        // check all current player's possible legal moves:
        for(final Move move : board.getCurrentMovingPlayer().getLegalMoves()) {
            // We want to apply the algorithm to the next board after the current player has moved:
            final BoardUpdate update = board.getCurrentMovingPlayer().makeMove(move); // update the current board
            // if the move is made successfully:
            if(update.getMoveStatus().isCompleted()) {
                // when alternating between tree levels, we alternate between finding the maximum and minimum value too:
                final int currentValue = minValue(update.getUpdatedBoard(), treeLevel - 1);
                if(currentValue >= currentHighestValue) {
                    currentHighestValue = currentValue;
                }
            }
        }
        return currentHighestValue;
    }

    // isGameOver(board) checks if the current chess board is game over (check mate or tie).
    private static boolean isGameOver(final ChessBoard board) {
        return board.getCurrentMovingPlayer().isCheckMate() || board.getCurrentMovingPlayer().isStaleMate();
    }
}
