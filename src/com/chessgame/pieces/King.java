package com.chessgame.pieces;

import com.chessgame.player.Team;
import com.chessgame.board.ChessBoard;
import com.chessgame.board.ChessTile;
import com.chessgame.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chessgame.board.Move.*;

public class King extends ChessPiece{
    // all possible move coordinate adjustments relative to the current King piece coordinate on the chessgame board:
    private final static int[] POSSIBLE_MOVE_REL_CRD= {-9, -8, -7, -1, 1, 7, 8, 9};
    // constructor when it is the piece's first move:
    public King(final int posn, final Team team) {
        super(pieceType.KING, posn, team, true);
    }

    public King(final int posn, final Team team, final boolean isFirstMove) {
        super(pieceType.KING, posn, team, isFirstMove);
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
            // if the current move coordinate is a valid coordinate on the chessgame board:
            if(ChessBoard.isValidTileCoordinate(realCoordinate)) {
                final ChessTile possibleDestinationTile = board.getTile(realCoordinate);
                // if the current targeted potential move destination tile is not occupied:
                if (!possibleDestinationTile.isTileOccupied()) {
                    legalMoves.add(new NormalMove(board, this, realCoordinate));
                }
                // or if it is occupied:
                else {
                    final ChessPiece pieceAtTile = possibleDestinationTile.getPiece();
                    final Team teamOfPieceAtTile = pieceAtTile.getPieceTeam();
                    if (this.pieceTeam != teamOfPieceAtTile) {
                        legalMoves.add(new NonPawnKillerMove(board, this, realCoordinate, pieceAtTile));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    // isOnFirstColumnValid(curPosition, movePosition) checks if the parameter current position is on the first column
    //  of the chessgame board and if the parameter movement position is invalid because of the first column.
    private static boolean isOnFirstColumnInvalid(final int curPosition, final int movePosition) {
        return ChessBoard.FIRST_COLUMN[curPosition] && ((movePosition == -9) || (movePosition == -1) ||
                (movePosition == 7));
    }

    // isOnLastColumnValid(curPosition, movePosition) checks if the parameter current position is on the last column
    //  of the chessgame board and if the parameter movement position is invalid because of the last column.
    private static boolean isOnLastColumnInvalid(final int curPosition, final int movePosition) {
        return ChessBoard.LAST_COLUMN[curPosition] && ((movePosition == -7) || (movePosition == 1) ||
                (movePosition == 9));
    }

    // toString() returns the type of the current piece.
    @Override
    public String toString() {
        return pieceType.KING.toString();
    }

    // for general function purpose, see ChessPiece class file.
    @Override
    public King movePiece(final Move move) {
        return new King(move.getDestinationCrd(), move.getMovingPiece().getPieceTeam());
    }
}
