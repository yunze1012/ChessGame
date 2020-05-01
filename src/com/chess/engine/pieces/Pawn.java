package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;

import static com.chess.engine.board.Move.*;

public class Pawn extends ChessPiece{
    // all possible move coordinate adjustments relative to the current Pawn piece coordinate on the chess board:
    private final static int[] POSSIBLE_MOVE_REL_CRD= {7, 8, 9, 16};

    public Pawn(final int posn, final Team team) {
        super(pieceType.PAWN, posn, team);
    }

    // for general function purpose, see ChessPiece class file.
    @Override
    public Collection<Move> allowedMoves(ChessBoard board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int curCoordinate : POSSIBLE_MOVE_REL_CRD) {
            // actual potential move coordinate of the current Pawn on the chess board depending on which Team:
            final int realCoordinate = this.piecePosition + (curCoordinate * this.pieceTeam.getDirection());
            // if the destination tile is not a valid tile coordinate, then skip it:
            if (!BoardUtils.isValidTileCoordinate(realCoordinate)) {
                continue;
            }
            // if the pawn is moving one tile forward, and the tile forward is not occupied:
            if (curCoordinate == 8 && !board.getTile(realCoordinate).isTileOccupied()) {
                legalMoves.add(new normalMove(board, this, realCoordinate)); // INCOMPLETE CONSTRUCTOR, PROMOTIONS
            }
            // if the pawn is moving its first move, and the pawn is part of the white team and on the second row OR
            //  part of the back team and on the seventh row, and the pawn is moving two tiles forward:
            else if (curCoordinate == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceTeam().isBlack()) ||
                            (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceTeam().isWhite()))) {
                // Coordinate of the front tile:
                final int oneFrontTile = this.piecePosition + (this.pieceTeam.getDirection() * 8);
                // if the front tile AND the destination tile (two tiles in front) are not occupied,
                //  then it is a valid move:
                if (!board.getTile(oneFrontTile).isTileOccupied() && !board.getTile(realCoordinate).isTileOccupied()) {
                    legalMoves.add(new normalMove(board, this, realCoordinate));
                }
                // the following are diagonal attack moves:
                // if the pawn is attacking right and the pawn is not on the last column if with the white team OR the
                //  pawn is not on the first column if with the black team:
                else if (curCoordinate == 7 &&
                        !((BoardUtils.LAST_COLUMN[this.piecePosition] && this.pieceTeam.isWhite()) ||
                                (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isBlack()))) {
                    // if the target tile is occupied...:
                    if(board.getTile(realCoordinate).isTileOccupied()) {
                        final ChessPiece pieceOnTarget = board.getTile(realCoordinate).getPiece();
                        //... with an enemy team chess piece, then it is a valid attack move:
                        if(this.pieceTeam != pieceOnTarget.getPieceTeam()) {
                            // TO COMPLETE ATTACK MOVE CONSTRUCTOR:
                            legalMoves.add(new normalMove(board, this, realCoordinate));
                        }
                    }
                }
                // if the pawn is attacking left and the pawn is not on the first column if with the white team OR the
                //  pawn is not on the last column if with the black team:
                else if (curCoordinate == 9 &&
                        !((BoardUtils.LAST_COLUMN[this.piecePosition] && this.pieceTeam.isBlack()) ||
                                (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isWhite()))) {
                    // if the target tile is occupied...:
                    if(board.getTile(realCoordinate).isTileOccupied()) {
                        final ChessPiece pieceOnTarget = board.getTile(realCoordinate).getPiece();
                        //... with an enemy team chess piece, then it is a valid attack move:
                        if(this.pieceTeam != pieceOnTarget.getPieceTeam()) {
                            // TO COMPLETE ATTACK MOVE CONSTRUCTOR:
                            legalMoves.add(new normalMove(board, this, realCoordinate));
                        }
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    // toString() returns the type of the current piece.
    @Override
    public String toString() {
        return pieceType.PAWN.toString();
    }
}
