package com.chessgame.player.aiopponent;

import com.chessgame.board.ChessBoard;
import com.chessgame.pieces.ChessPiece;
import com.chessgame.player.Player;

public class InitialBoardScore implements BoardScore {

    @Override
    public int score(final ChessBoard board, final int treeLevel) {
        // NOTE: By subtracting the white player's board score with the black player's board score, we can check that
        //  if it is positive, then WHITE player is currently winning (white player's score > black player's score),
        //  but if the score is negative, then BLACK player is currently winning (black player's score > white player's score)
        //  as defined in BoardScore interface. And if (white player's score == black player's score), then the difference
        //  is null so the score is neutral, which means that the game is currently equal.
        return playerScore(board, board.getWhitePlayer(), treeLevel) -
                playerScore(board, board.getBlackPlayer(), treeLevel);
    }

    // playerScore(board, player, treeLevel) returns the current player's score on the given tree level with the chess
    //  board.
    private int playerScore(final ChessBoard board, final Player player, final int treeLevel) {
        return piecesPoints(player);
    }

    // piecesPoints(player) returns a sum of points of all the given player's current pieces on the chess board.
    private static int piecesPoints(final Player player) {
        int currentSum = 0;
        for(final ChessPiece piece : player.getActivePieces()) {
            currentSum += piece.getPiecePoints();
        }
        return currentSum;
    }
}
