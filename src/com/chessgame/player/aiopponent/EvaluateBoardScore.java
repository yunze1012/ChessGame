package com.chessgame.player.aiopponent;

import com.chessgame.board.ChessBoard;
import com.chessgame.pieces.ChessPiece;
import com.chessgame.player.Player;

public class EvaluateBoardScore implements BoardScore {
    private static final int CHECK_SCORE = 50; // A check status on the opponent is worth half a pawn.
    private static final int CHECK_MATE_SCORE = 10000; // Same value as the King (killing the king)
    private static final int CASTLED_SCORE = 60;

    @Override
    public int score(final ChessBoard board, final int treeLevel) {
        // NOTE: By subtracting the white player's board score with the black player's board score, we can check that
        //  if it is positive, then WHITE player is currently winning (white player's score > black player's score),
        //  but if the score is negative, then BLACK player is currently winning (black player's score > white player's score)
        //  as defined in BoardScore interface. And if (white player's score == black player's score), then the difference
        //  is null so the score is neutral, which means that the game is currently equal.
        return playerScore(board.getWhitePlayer(), treeLevel) -
                playerScore(board.getBlackPlayer(), treeLevel);
    }

    // playerScore(board, player, treeLevel) returns the current player's score on the given tree level with the chess
    //  board.
    private int playerScore(final Player player, final int treeLevel) {
        return piecesPoints(player) + playerMoveOptions(player) + opponentCheckStatus(player) +
                opponentCheckMateStatus(player, treeLevel) + hasCastledScore(player);
    }

    // piecesPoints(player) returns a sum of points of all the given player's current pieces on the chess board.
    private static int piecesPoints(final Player player) {
        int currentSum = 0;
        for(final ChessPiece piece : player.getActivePieces()) {
            currentSum += piece.getPiecePoints();
        }
        return currentSum;
    }

    // playerMoveOptions(player) returns the number of legal move options for the given player.
    private static int playerMoveOptions(final Player player) {
        return player.getLegalMoves().size();
    }

    // opponentCheckStatus(player) checks if the opponent of the given player is in check and if so, return a check score.
    private static int opponentCheckStatus(final Player player) {
        if(player.getOpponent().isCheck()) {
            return CHECK_SCORE;
        }
        return 0;
    }

    // opponentCheckMateStatus(player) checks if the opponent of the given player has a possibility of being in a check
    //  mate in the current tree level, and if so, return a check mate score.
    private static int opponentCheckMateStatus(final Player player, final int treeLevel) {
        if(player.getOpponent().isCheckMate()) {
            return CHECK_MATE_SCORE * treeLevelMultiplicator(treeLevel);
        }
        return 0;
    }

    // treeLevelMultiplicator(treeLevel) multiplies a score according to the deepness of the given tree level.
    //  (The deeper in the tree, the higher the final score, since the sooner we found the ideal move)
    private static int treeLevelMultiplicator(final int treeLevel) {
        if(treeLevel == 0) {
            return 1; // null multiplicator
        }
            return 100 * treeLevel;
    }

    // hasCastledScore(player) checks if the given player played his castling move and if so, return a castled score.
    private static int hasCastledScore(final Player player) {
        if(player.isCastled()) {
            return CASTLED_SCORE;
        }
        return 0;
    }
}
