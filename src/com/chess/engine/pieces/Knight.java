package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.ChessTile;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Knight extends ChessPiece{
    // all possible move coordinate adjustments relative to the current Knight piece coordinate on the chess board:
    private final static int[] POSSIBLE_MOVE_REL_CRD= {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final int posn, final Team team) {
        super(posn, team);
    }

    // for function purpose, see ChessPiece class file.
    @Override
    public List<Move> allowedMoves(ChessBoard board) {
        int realCoordinate; // actual potential move coordinate of the current Knight on the chess board.
        final List<Move> legalMoves = new ArrayList<>();
        for (final int curCoordinate : POSSIBLE_MOVE_REL_CRD) {
            realCoordinate = this.piecePosition + curCoordinate; // get the real coordinate of the potential move
            if (true /*it is a valid tile coordinate*/) {
                final ChessTile possibleDestinationTile = board.getTile(realCoordinate);
                // if the current targeted potential move destination tile is not occupied:
                if (!possibleDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move()); // PLACEHOLDER to change later after Move class implementation
                }
                // or if it is occupied:
                else {
                    final ChessPiece pieceAtTile = possibleDestinationTile.getPiece();
                    final Team teamOfPieceAtTile = pieceAtTile.getPieceTeam();
                    if (this.pieceTeam != teamOfPieceAtTile) {
                        legalMoves.add(new Move());
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
}
