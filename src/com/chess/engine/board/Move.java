package com.chess.engine.board;

import com.chess.engine.pieces.ChessPiece;

public abstract class Move {
    final ChessBoard curBoard;
    final ChessPiece curPiece; // current moving piece
    final int destinationCrd; // coordinate of the destination tile

    public Move (final ChessBoard board, final ChessPiece piece, final int destCrd) {
        this.curBoard = board;
        this.curPiece = piece;
        this.destinationCrd = destCrd;
    }

    // TODO complete implementation of all the Overrides:
    // executeMove() returns a new ChessBoard object that has the configuration after executing the current move on the
    //  old ChessBoard.
    public abstract ChessBoard executeMove();

    // This subclass defines a killing moving move (piece removal).
    public static final class killerMove extends Move {
        final ChessPiece targetedPiece; // the enemy chess piece that is being attacked by our current piece
        public killerMove(final ChessBoard board, final ChessPiece piece, final int destCrd, final ChessPiece target) {
            super(board, piece, destCrd);
            this.targetedPiece = target;
        }

        @Override
        public ChessBoard executeMove() {
            return null;
        }
    }
    // This subclass defines a normal moving move.
    public static final class normalMove extends Move {

        public normalMove (final ChessBoard board, final ChessPiece piece, final int destCrd) {
            super(board, piece, destCrd);
        }

        @Override
        public ChessBoard executeMove() {
            return null;
        }
    }
    // getDestinationCrd() returns the destination coordinate (tile index number) of the current move.
    public int getDestinationCrd() {
        return this.destinationCrd;
    }
}
