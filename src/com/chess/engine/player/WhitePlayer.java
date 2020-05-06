package com.chess.engine.player;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.ChessTile;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.ChessPiece;
import com.chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class WhitePlayer extends Player {
    public WhitePlayer(final ChessBoard board, final Collection<Move> whiteMoves, final Collection<Move> blackMoves) {
        super(board, whiteMoves, blackMoves);
    }

    // for general function purpose, see Player class file:
    @Override
    public Collection<ChessPiece> getActivePieces() {
        return this.board.getWhitePieces();
    }
    @Override
    public Team getTeam() {
        return Team.WHITE;
    }
    @Override
    public Player getOpponent() {
        return this.board.getBlackPlayer();
    }
    @Override
    protected Collection<Move> calculateCastlingMoves(final Collection<Move> legalMoves,
                                                      final Collection<Move> opponentLegalMoves) {
        final List<Move> castlingMoves = new ArrayList<>();
        // Condition for a castling move: the player must not be in check and it must be the King's first move.
        if(this.king.isFirstMove() && !this.isCheck()) {
            // The two tiles between the King and the Rook on the King side of the board (on White team side) must also
            //  be empty.
            if(!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                final ChessTile tileOfRook = this.board.getTile(63); // The original King side Rook's position on the board.
                // The rook must be present at its original place and must also be its first move. (Piece must be a rook)
                if(tileOfRook.getPiece().getPieceType().isRook() && tileOfRook.isTileOccupied() &&
                        tileOfRook.getPiece().isFirstMove()) {
                    // AND, none of these empty cases is targeted by enemy's pieces.
                    if(Player.attackOnTile(61, opponentLegalMoves).isEmpty() &&
                            Player.attackOnTile(62, opponentLegalMoves).isEmpty()) {
                        // adding a new King side castling move with the corresponding destination coordinates for the
                        //  King and Rook for the white team:
                        castlingMoves.add(new kingSideCastleMove(this.board, this.king, 62,
                                (Rook)tileOfRook.getPiece(), tileOfRook.getTileCoordinates(), 61));
                    }
                }
            }
            // OR the three tiles between the King and the Rook on the Queen side of the board (on White team side) must
            //  be empty.
            if(!this.board.getTile(57).isTileOccupied() && !this.board.getTile(58).isTileOccupied()
                    && !this.board.getTile(59).isTileOccupied()) {
                final ChessTile tileOfRook = this.board.getTile(56); // The original Queen side Rook's position on the board.
                // The rook must be present at its original place and must also be its first move. (Piece must be a rook)
                if(tileOfRook.getPiece().getPieceType().isRook() && tileOfRook.isTileOccupied() &&
                        tileOfRook.getPiece().isFirstMove()) {
                    // adding a new Queen side castling move with the corresponding destination coordinates for the
                    //  King and Rook for the white team:
                    castlingMoves.add(new queenSideCastleMove(this.board, this.king, 58,
                            (Rook)tileOfRook.getPiece(), tileOfRook.getTileCoordinates(), 59));
                }
            }
        }
        return ImmutableList.copyOf(castlingMoves);
    }
}