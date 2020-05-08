package com.chess.engine.board;

import com.chess.engine.pieces.ChessPiece;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Rook;

import static com.chess.engine.board.ChessBoard.*;

public abstract class Move {
    final ChessBoard curBoard;
    final ChessPiece movingPiece; // current moving piece
    final int destinationCrd; // coordinate of the destination tile
    public static final Move INVALID_MOVE = new invalidMove();

    public Move (final ChessBoard board, final ChessPiece piece, final int destCrd) {
        this.curBoard = board;
        this.movingPiece = piece;
        this.destinationCrd = destCrd;
    }

    // executeMove() returns a new ChessBoard with the current move executed on the old ChessBoard.
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

    // This subclass defines a killing move (piece removal).
    public static class killerMove extends Move {
        final ChessPiece targetedPiece; // the enemy chess piece that is being attacked by our current piece
        public killerMove(final ChessBoard board, final ChessPiece piece, final int destCrd, final ChessPiece target) {
            super(board, piece, destCrd);
            this.targetedPiece = target;
        }

        @Override
        public ChessBoard executeMove() {
            return null;
        }
        @Override
        public boolean isKillerMove() {
            return true;
        }
        @Override
        public ChessPiece getTargetedPiece() {
            return this.targetedPiece;
        }
        @Override
        public int hashCode() {
            return this.targetedPiece.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object compared) {
            if(this == compared) {
                return true;
            }
            if(!(compared instanceof killerMove)) {
                return false;
            }
            final killerMove comparedMove = (killerMove) compared;
            // checks if the current move and target piece are equal.
            return super.equals(comparedMove) && getTargetedPiece().equals(comparedMove.getTargetedPiece());
        }
    }
    // This subclass defines a normal moving move.
    public static final class normalMove extends Move {

        public normalMove (final ChessBoard board, final ChessPiece piece, final int destCrd) {
            super(board, piece, destCrd);
        }

    }
    // This subclass defines a move made by a Pawn.
    public static final class pawnMove extends Move {

        public pawnMove(final ChessBoard board, final ChessPiece piece, final int destCrd) {
            super(board, piece, destCrd);
        }
    }
    // This subclass defines a killing move made by a Pawn.
    public static class pawnKillerMove extends killerMove {

        public pawnKillerMove(final ChessBoard board, final ChessPiece piece, final int destCrd,
                              final ChessPiece target) {
            super(board, piece, destCrd, target);
        }
    }
    // This subclass defines the En Passant move.
    public static final class enPassantMove extends pawnKillerMove {

        public enPassantMove(final ChessBoard board, final ChessPiece piece, final int destCrd,
                             final ChessPiece target) {
            super(board, piece, destCrd, target);
        }
    }
    // This subclass defines the Pawn's double tile move (moving 2 tiles).
    public static final class pawnDoubleMove extends Move {

        public pawnDoubleMove(final ChessBoard board, final ChessPiece piece, final int destCrd) {
            super(board, piece, destCrd);
        }

        @Override
        public ChessBoard executeMove() {
            // same concept as above original function
            final Builder builder = new Builder();
            for(final ChessPiece piece : this.curBoard.getCurrentMovingPlayer().getActivePieces()) {
                if(!this.movingPiece.equals(piece)) {
                    builder.putPiece(piece);
                }
            }
            for (final ChessPiece piece: this.curBoard.getCurrentMovingPlayer().getOpponent().getActivePieces()) {
                builder.putPiece(piece);
            }
            final Pawn movingPawn = (Pawn)this.movingPiece.movePiece(this);
            builder.putPiece(movingPawn);
            builder.setEnPassant(movingPawn); // NEW: after moving 2 tiles up, this pawn is now a potential en passant move
            builder.setMover(this.curBoard.getCurrentMovingPlayer().getOpponent().getTeam());
            return builder.build();
        }
    }
    // This subclass defines the Castle move.
    static abstract class castleMove extends Move {
        protected final Rook rook;
        protected final int rookCurCrd; // current rook coordinate
        protected final int rookDestCrd; // rook destination coordinate

        public castleMove(final ChessBoard board, final ChessPiece piece, final int destCrd, final Rook rook,
                          final int rookCurCrd, final int rookDestCrd) {
            super(board, piece, destCrd);
            this.rook = rook;
            this.rookCurCrd = rookCurCrd;
            this.rookDestCrd = rookDestCrd;
        }

        // getCastleRook() returns the rook that is being castled.
        public Rook getCastleRook() {
            return rook;
        }
        @Override
        public boolean isCastlingMove() {
            return true;
        }
        @Override
        public ChessBoard executeMove() {
            // same concept as above original function
            final Builder builder = new Builder();
            for(final ChessPiece piece : this.curBoard.getCurrentMovingPlayer().getActivePieces()) {
                // NEW: if the piece being checked right now is not the moving piece AND is not the castling Rook.
                // The "old" Rook is removed from the board and movement will be handled at the bottom part of this function.
                if(!this.movingPiece.equals(piece) && !this.rook.equals(piece)) {
                    builder.putPiece(piece);
                }
            }
            for (final ChessPiece piece: this.curBoard.getCurrentMovingPlayer().getOpponent().getActivePieces()) {
                builder.putPiece(piece);
            }
            // The moving of the castling Rook and the King will be handled here:
            builder.putPiece(this.movingPiece.movePiece(this)); // Moving the king
            // Creating a new Rook at the destination coordinate:
            builder.putPiece(new Rook(this.rookDestCrd, this.rook.getPieceTeam()));
            builder.setMover(this.curBoard.getCurrentMovingPlayer().getOpponent().getTeam());
            return builder.build();
        }
    }
    // This subclass defines the Castle move on the King side.
    public static final class kingSideCastleMove extends castleMove {

        public kingSideCastleMove(final ChessBoard board, final ChessPiece piece, final int destCrd, final Rook rook,
                                  final int rookCurCrd, final int rookDestCrd) {
            super(board, piece, destCrd, rook, rookCurCrd, rookDestCrd);
        }

        @Override
        public String toString() {
            return "O-O"; // convention notation for King side castle
        }
    }
    // This subclass defines the Castle move on the Queen side.
    public static final class queenSideCastleMove extends castleMove {

        public queenSideCastleMove(final ChessBoard board, final ChessPiece piece, final int destCrd, final Rook rook,
                                   final int rookCurCrd, final int rookDestCrd) {
            super(board, piece, destCrd, rook, rookCurCrd, rookDestCrd);
        }

        @Override
        public String toString() {
            return "O-O-O"; // convention notation for Queen side castle
        }
    }
    // This subclass defines an invalid move on a chess board.
    public static final class invalidMove extends Move {

        public invalidMove() {
            super(null, null, -1);
        }

        @Override
        public ChessBoard executeMove() {
            throw new RuntimeException("INVALID MOVE!");
        }
    }
    // This subclass creates a move.
    public static class MoveCreator {

        private MoveCreator() {
            throw new RuntimeException("Not instantiable");
        }

        // createMove() returns a legal move given a chess board with the same starting coordinate (curCrd) and ending
        //  coordinate (destCrd).
        public static Move createMove(final ChessBoard board, final int curCrd ,final int destCrd) {
            // checks in all the possible legal moves for both players if there is a move with the same starting and
            //  ending coordinate:
            for (final Move move : board.getAllLegalMoves()) {
                // if the current and destination coordinate are the same, then this is the move we want:
                if(move.getCurrentCrd() == curCrd && move.getDestinationCrd() == destCrd) {
                    return move;
                }
            }
            // else, then there is no legal move that can make this movement. Thus, this is an invalid move:
            return INVALID_MOVE;
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
    // getCurrentCrd() returns the current coordinate of the moving piece.
    public int getCurrentCrd() {
        return this.getMovingPiece().getPiecePosition();
    }
    // isKillerMove() checks if the current move is a killer move.
    public boolean isKillerMove() {
        return false;
    }
    // isCastlingMove() checks if the current move is a castling move.
    public boolean isCastlingMove() {
        return false;
    }
    // getTargetedPiece() returns the current piece being targeted for attack.
    public ChessPiece getTargetedPiece() {
        return null;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + this.destinationCrd;
        result = 31 * result + this.movingPiece.hashCode();
        return result;
    }
    @Override
    public boolean equals(final Object compared) {
        if(this == compared) {
            return true;
        }
        if(!(compared instanceof Move)) {
            return false;
        }
        final Move comparedMove = (Move) compared;
        // compare if the moving piece is the same and if they move to the same destination:
        return getDestinationCrd() == comparedMove.getDestinationCrd() &&
                getMovingPiece().equals(comparedMove.getMovingPiece());
    }
}
