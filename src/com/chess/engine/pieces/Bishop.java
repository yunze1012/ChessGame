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

import static com.chess.engine.board.Move.*;

public class Bishop extends ChessPiece {
    // all possible move coordinate adjustments relative to the current Bishop piece coordinate on the chess board:
    private final static int[] POSSIBLE_MOVE_REL_CRD = {-9, -7, 7, 9};

    public Bishop (final int posn, final Team team) {
        super(posn, team);
    }

    // for general function purpose, see ChessPiece class file.
    @Override
    public Collection<Move> allowedMoves(final ChessBoard board) {
        final List<Move> legalMoves = new ArrayList<>();
        // checking each possible movement:
        for (final int curCoordinate: POSSIBLE_MOVE_REL_CRD) {
            int realCoordinate = this.piecePosition; // current piece coordinate
            while (BoardUtils.isValidTileCoordinate(realCoordinate)) {
                if (isOnFirstColumnInvalid(realCoordinate, curCoordinate) ||
                        isOnLastColumnInvalid(realCoordinate, curCoordinate)) {
                    break;
                }
                realCoordinate += curCoordinate; // possible movement resulting coordinate
                if (BoardUtils.isValidTileCoordinate(realCoordinate)) {
                    final ChessTile possibleDestinationTile = board.getTile(realCoordinate);
                    // if the current targeted potential move destination tile is not occupied:
                    if (!possibleDestinationTile.isTileOccupied()) {
                        legalMoves.add(new normalMove(board, this, realCoordinate));
                    }
                    // or if it is occupied:
                    else {
                        final ChessPiece pieceAtTile = possibleDestinationTile.getPiece();
                        final Team teamOfPieceAtTile = pieceAtTile.getPieceTeam();
                        if (this.pieceTeam != teamOfPieceAtTile) {
                            legalMoves.add(new killerMove(board, this, realCoordinate, pieceAtTile));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    // isOnFirstColumnValid(curPosition, movePosition) checks if the parameter current position is on the first column
    //  of the chess board and if the parameter movement position is invalid because of the first column.
    private static boolean isOnFirstColumnInvalid (final int curPosition, final int movePosition) {
        return BoardUtils.FIRST_COLUMN[curPosition] && ((movePosition == -9) || (movePosition == 7));
    }

    // isOnLastColumnValid(curPosition, movePosition) checks if the parameter current position is on the last column
    //  of the chess board and if the parameter movement position is invalid because of the last column.
    private static boolean isOnLastColumnInvalid (final int curPosition, final int movePosition) {
        return BoardUtils.LAST_COLUMN[curPosition] && ((movePosition == -7) || (movePosition == 9));
    }

    // toString() returns the type of the current piece.
    @Override
    public String toString() {
        return pieceType.BISHOP.toString();
    }
}
