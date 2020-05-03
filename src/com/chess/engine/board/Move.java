package com.chess.engine.board;

import com.chess.engine.pieces.ChessPiece;

import static com.chess.engine.board.ChessBoard.*;

public abstract class Move {
    final ChessBoard curBoard;
    final ChessPiece movingPiece; // current moving piece
    final int destinationCrd; // coordinate of the destination tile

    public Move (final ChessBoard board, final ChessPiece piece, final int destCrd) {
        this.curBoard = board;
        this.movingPiece = piece;
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
            final Builder builder = new Builder(); // new board after making the move
            // place all the current moving player's pieces on the new board:
            for (final ChessPiece piece : this.curBoard.getCurrentMovingPlayer().getActivePieces()) {
                // if the piece that is being checked right now is not the moving piece, then put it at the same place:
                //TODO this is incomplete
                if(!this.movingPiece.equals(piece)) {
                    builder.putPiece(piece);
                }
            }
            // place all the enemy pieces on the new board:
            for (final ChessPiece piece: this.curBoard.getCurrentMovingPlayer().getOpponent().getActivePieces()) {
                builder.putPiece(piece);
            }
            // the piece that is being moved is placed on the new board now:
            builder.putPiece(this.movingPiece.movePiece(this));
            // the move maker is now the opponent (it is the opponent's turn):
            builder.setMover(this.curBoard.getCurrentMovingPlayer().getOpponent().getTeam());
            return builder.build();
        }
    }
    // getDestinationCrd() returns the destination coordinate (tile index number) of the current move.
    public int getDestinationCrd() {
        return this.destinationCrd;
    }
    // getMovingPiece() returns the current moving piece.
    public ChessPiece getMovingPiece() {
        return this.movingPiece;
    }
}
