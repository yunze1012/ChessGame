package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.ChessTile;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends ChessPiece{
    // all possible move coordinate adjustments relative to the current King piece coordinate on the chess board:
    private final static int[] POSSIBLE_MOVE_REL_CRD= {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final int posn, final Team team) {
        super(posn, team);
    }

    // for general function purpose, see ChessPiece class file
    @Override
    public Collection<Move> allowedMoves(ChessBoard board) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final int curCoordinate : POSSIBLE_MOVE_REL_CRD) {
            final int realCoordinate = this.piecePosition + curCoordinate;
            // EXCEPTION CASE: If the King is on the first or last column, then the following rules will not apply to it.
            if (isOnFirstColumnInvalid(this.piecePosition, curCoordinate) ||
                    isOnLastColumnInvalid(this.piecePosition, curCoordinate)) {
                continue;
            }
            // if the current move coordinate is a valid coordinate on the chess board:
            if(BoardUtils.isValidTileCoordinate(realCoordinate)) {
                final ChessTile possibleDestinationTile = board.getTile(realCoordinate);
                // if the current targeted potential move destination tile is not occupied:
                if (!possibleDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.normalMove(board, this, realCoordinate));
                }
                // or if it is occupied:
                else {
                    final ChessPiece pieceAtTile = possibleDestinationTile.getPiece();
                    final Team teamOfPieceAtTile = pieceAtTile.getPieceTeam();
                    if (this.pieceTeam != teamOfPieceAtTile) {
                        legalMoves.add(new Move.killerMove(board, this, realCoordinate, pieceAtTile));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    // isOnFirstColumnValid(curPosition, movePosition) checks if the parameter current position is on the first column
    //  of the chess board and if the parameter movement position is invalid because of the first column.
    private static boolean isOnFirstColumnInvalid(final int curPosition, final int movePosition) {
        return BoardUtils.FIRST_COLUMN[curPosition] && ((movePosition == -9) || (movePosition == -1) ||
                (movePosition == 7));
    }

    // isOnLastColumnValid(curPosition, movePosition) checks if the parameter current position is on the last column
    //  of the chess board and if the parameter movement position is invalid because of the last column.
    private static boolean isOnLastColumnInvalid(final int curPosition, final int movePosition) {
        return BoardUtils.LAST_COLUMN[curPosition] && ((movePosition == -7) || (movePosition == 1) ||
                (movePosition == 9));
    }

    // toString() returns the type of the current piece.
    @Override
    public String toString() {
        return pieceType.KING.toString();
    }
}
