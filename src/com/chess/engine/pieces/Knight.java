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

public class Knight extends ChessPiece{
    // all possible move coordinate adjustments relative to the current Knight piece coordinate on the chess board:
    private final static int[] POSSIBLE_MOVE_REL_CRD= {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int posn, final Team team) {
        super(pieceType.KNIGHT, posn, team);
    }

    // for general function purpose, see ChessPiece class file.
    // this override is limited to the Knight chess piece.
    @Override
    public Collection<Move> allowedMoves(final ChessBoard board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int curCoordinate : POSSIBLE_MOVE_REL_CRD) {
            // actual potential move coordinate of the current Knight on the chess board:
            int realCoordinate = this.piecePosition + curCoordinate;
            // if the current move coordinate is a valid coordinate on the chess board:
            if (BoardUtils.isValidTileCoordinate(realCoordinate)) {
                // EXCEPTION CASE: If the Knight is on the first, second, seventh, or last column, then the following
                //  rules will not apply to it. We need then different rules.
                if (isOnFirstColumnInvalid(this.piecePosition, curCoordinate) ||
                        isOnSecondColumnInvalid(this.piecePosition, curCoordinate) ||
                        isOnSeventhColumnInvalid(this.piecePosition, curCoordinate) ||
                        isOnLastColumnInvalid(this.piecePosition, curCoordinate)) {
                    continue;
                }

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
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    // isOnFirstColumnValid(curPosition, movePosition) checks if the parameter current position is on the first column
    //  of the chess board and if the parameter movement position is invalid because of the first column.
    private static boolean isOnFirstColumnInvalid(final int curPosition, final int movePosition) {
        return BoardUtils.FIRST_COLUMN[curPosition] && ((movePosition == -17) || (movePosition == -10) ||
                (movePosition == 6) || (movePosition == 15));
    }

    // isOnSecondColumnValid(curPosition, movePosition) checks if the parameter current position is on the second column
    //  of the chess board and if the parameter movement position is invalid because of the second column.
    private static boolean isOnSecondColumnInvalid(final int curPosition, final int movePosition) {
        return BoardUtils.SECOND_COLUMN[curPosition] && ((movePosition == -10) || (movePosition == 6));
    }

    // isOnSeventhColumnValid(curPosition, movePosition) checks if the parameter current position is on the seventh column
    //  of the chess board and if the parameter movement position is invalid because of the seventh column.
    private static boolean isOnSeventhColumnInvalid(final int curPosition, final int movePosition) {
        return BoardUtils.SEVENTH_COLUMN[curPosition] && ((movePosition == -6) || (movePosition == 10));
    }

    // isOnLastColumnValid(curPosition, movePosition) checks if the parameter current position is on the last column
    //  of the chess board and if the parameter movement position is invalid because of the last column.
    private static boolean isOnLastColumnInvalid(final int curPosition, final int movePosition) {
        return BoardUtils.LAST_COLUMN[curPosition] && ((movePosition == -15) || (movePosition == -6) ||
                (movePosition == 10) || (curPosition == 17));
    }

    // toString() returns the type of the current piece.
    @Override
    public String toString() {
        return pieceType.KNIGHT.toString();
    }

    // for general function purpose, see ChessPiece class file.
    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getDestinationCrd(), move.getMovingPiece().getPieceTeam());
    }
}
