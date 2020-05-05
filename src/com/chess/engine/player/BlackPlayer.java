package com.chess.engine.player;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.ChessTile;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.ChessPiece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player {
    public BlackPlayer(final ChessBoard board, final Collection<Move> whiteMoves, final Collection<Move> blackMoves) {
        super(board, blackMoves, whiteMoves);
    }

    // for general function purpose, see Player class file:
    @Override
    public Collection<ChessPiece> getActivePieces() {
        return this.board.getBlackPieces();
    }
    @Override
    public Team getTeam() {
        return Team.BLACK;
    }
    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }

    @Override
    protected Collection<Move> calculateCastlingMoves(Collection<Move> legalMoves, Collection<Move> opponentLegalMoves) {
        final List<Move> castlingMoves = new ArrayList<>();
        // Condition for a castling move: the player must not be in check and it must be the King's first move.
        if(this.king.isFirstMove() && !this.isCheck()) {
            // The two tiles between the King and the Rook on the King side of the board (on Black team side) must also
            //  be empty.
            if(!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
                final ChessTile tileOfRook = this.board.getTile(7); // The original King side Rook's position on the board.
                // The rook must be present at its original place and must also be its first move. (Piece must be a rook)
                if(tileOfRook.getPiece().getPieceType().isRook() && tileOfRook.isTileOccupied() &&
                        tileOfRook.getPiece().isFirstMove()) {
                    // AND, none of these empty cases is targeted by enemy's pieces.
                    if(Player.attackOnTile(5, opponentLegalMoves).isEmpty() &&
                            Player.attackOnTile(6, opponentLegalMoves).isEmpty()) {
                        //TODO complete implementation
                        castlingMoves.add(null);
                    }
                }
            }
            // OR the three tiles between the King and the Rook on the Queen side of the board (on Black team side) must
            //  be empty.
            if(!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied()
                    && !this.board.getTile(3).isTileOccupied()) {
                final ChessTile tileOfRook = this.board.getTile(0); // The original Queen side Rook's position on the board.
                // The rook must be present at its original place and must also be its first move. (Piece must be a rook)
                if(tileOfRook.getPiece().getPieceType().isRook() && tileOfRook.isTileOccupied() &&
                        tileOfRook.getPiece().isFirstMove()) {
                    //TODO complete implementation
                    castlingMoves.add(null);
                }
            }
        }
        return ImmutableList.copyOf(castlingMoves);
    }
}
