package com.chessgame.pieces;

import com.chessgame.player.Team;
import com.chessgame.board.ChessBoard;
import com.chessgame.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chessgame.board.Move.*;

public class Pawn extends ChessPiece{
    // all possible move coordinate adjustments relative to the current Pawn piece coordinate on the chessgame board:
    private final static int[] POSSIBLE_MOVE_REL_CRD= {7, 8, 9, 16};
    // constructor when it is the piece's first move:
    public Pawn(final int posn, final Team team) {
        super(pieceType.PAWN, posn, team, true);
    }

    public Pawn(final int posn, final Team team, final boolean isFirstMove) {
        super(pieceType.PAWN, posn, team, isFirstMove);
    }

    // for general function purpose, see ChessPiece class file.
    @Override
    public Collection<Move> allowedMoves(ChessBoard board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int curCoordinate : POSSIBLE_MOVE_REL_CRD) {
            // actual potential move coordinate of the current Pawn on the chessgame board depending on which Team:
            final int realCoordinate = this.piecePosition + (curCoordinate * this.pieceTeam.getDirection());
            // if the destination tile is not a valid tile coordinate, then skip it:
            if (!ChessBoard.isValidTileCoordinate(realCoordinate)) {
                continue;
            }
            // if the pawn is moving one tile forward, and the tile forward is not occupied:
            if (curCoordinate == 8 && !board.getTile(realCoordinate).isTileOccupied()) {
                // if the pawn is moving towards a pawn promotion possible tile:
                if(this.pieceTeam.isPromotionTile(realCoordinate)) {
                    legalMoves.add(new pawnPromotion(new PawnMove(board, this, realCoordinate)));
                }
                else {
                    legalMoves.add(new PawnMove(board, this, realCoordinate));
                }
            }
            // if the pawn is moving its first move, and the pawn is part of the white team and on the second row OR
            //  part of the back team and on the seventh row, and the pawn is moving two tiles forward:
            else if (curCoordinate == 16 && this.isFirstMove() &&
                    ((ChessBoard.SECOND_ROW[this.piecePosition] && this.getPieceTeam().isBlack()) ||
                            (ChessBoard.SEVENTH_ROW[this.piecePosition] && this.getPieceTeam().isWhite()))) {
                // Coordinate of the front tile:
                final int oneFrontTile = this.piecePosition + (this.pieceTeam.getDirection() * 8);
                // if the front tile AND the destination tile (two tiles in front) are not occupied,
                //  then it is a valid move:
                if (!board.getTile(oneFrontTile).isTileOccupied() && !board.getTile(realCoordinate).isTileOccupied()) {
                    legalMoves.add(new PawnDoubleMove(board, this, realCoordinate));
                }
            }
            // the following are diagonal attack moves:
                // if the pawn is attacking right and the pawn is not on the last column if with the white team OR the
                //  pawn is not on the first column if with the black team:
            else if (curCoordinate == 7 &&
                        !((ChessBoard.LAST_COLUMN[this.piecePosition] && this.pieceTeam.isWhite()) ||
                                (ChessBoard.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isBlack()))) {
                // if the target tile is occupied...:
                if(board.getTile(realCoordinate).isTileOccupied()) {
                    final ChessPiece pieceOnTarget = board.getTile(realCoordinate).getPiece();
                    //... with an enemy team chessgame piece, then it is a valid attack move:
                    if(this.pieceTeam != pieceOnTarget.getPieceTeam()) {
                        // if the pawn is moving towards a pawn promotion possible tile:
                        if(this.pieceTeam.isPromotionTile(realCoordinate)) {
                            legalMoves.add(new pawnPromotion(new PawnKillerMove(board, this, realCoordinate, pieceOnTarget)));
                        }
                        else {
                            legalMoves.add(new PawnKillerMove(board, this, realCoordinate, pieceOnTarget));
                        }
                    }
                }
                // else if there is an en passant pawn:
                else if(board.getEnPassantPawn() != null) {
                    // if the enemy pawn is next to your pawn (on your right), then you can execute the en passant move:
                    if(board.getEnPassantPawn().getPiecePosition() ==
                            (this.piecePosition + (this.pieceTeam.getEnemyDirection()))) {
                        final ChessPiece pieceOnTarget = board.getEnPassantPawn();
                        if(this.pieceTeam != pieceOnTarget.getPieceTeam()) { // check that is is an enemy pawn
                            legalMoves.add(new EnPassantMove(board, this, realCoordinate, pieceOnTarget));
                        }
                    }
                }
            }
            // if the pawn is attacking left and the pawn is not on the first column if with the white team OR the
            //  pawn is not on the last column if with the black team:
            else if (curCoordinate == 9 &&
                    !((ChessBoard.LAST_COLUMN[this.piecePosition] && this.pieceTeam.isBlack()) ||
                            (ChessBoard.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isWhite()))) {
                // if the target tile is occupied...:
                if(board.getTile(realCoordinate).isTileOccupied()) {
                    final ChessPiece pieceOnTarget = board.getTile(realCoordinate).getPiece();
                    //... with an enemy team chessgame piece, then it is a valid attack move:
                    if(this.pieceTeam != pieceOnTarget.getPieceTeam()) {
                        // if the pawn is moving towards a pawn promotion possible tile:
                        if(this.pieceTeam.isPromotionTile(realCoordinate)) {
                            legalMoves.add(new pawnPromotion(new PawnKillerMove(board, this, realCoordinate, pieceOnTarget)));
                        }
                        else {
                            legalMoves.add(new PawnKillerMove(board, this, realCoordinate, pieceOnTarget));
                        }
                    }
                }
                // else if there is an en passant pawn:
                else if(board.getEnPassantPawn() != null) {
                    // if the enemy pawn is next to your pawn (on your left, then you can execute the en passant move:
                    if(board.getEnPassantPawn().getPiecePosition() ==
                            (this.piecePosition + (this.pieceTeam.getDirection()))) {
                        final ChessPiece pieceOnTarget = board.getEnPassantPawn();
                        if(this.pieceTeam != pieceOnTarget.getPieceTeam()) { // check that is is an enemy pawn
                            legalMoves.add(new EnPassantMove(board, this, realCoordinate, pieceOnTarget));
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

    // for general function purpose, see ChessPiece class file.
    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCrd(), move.getMovingPiece().getPieceTeam());
    }

    // getPromotedPiece() returns the new promoted Queen from the old Pawn.
    // NOTE: This is a direct promotion to Queen, the highest level piece.
    public ChessPiece getPromotedPiece() {
        return new Queen(this.piecePosition, this.pieceTeam, false);
    }
}
