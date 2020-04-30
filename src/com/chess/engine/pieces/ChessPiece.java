package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class ChessPiece {
    protected final int piecePosition;
    protected final Team pieceTeam;
    protected final boolean isFirstMove;

    public ChessPiece(final int posn, final Team team) {
        this.piecePosition = posn;
        this.pieceTeam = team;
        this.isFirstMove = false; // TO COMPLETE
    }
    // ifFirstMove() checks if it is this chess piece's first move on the chess board.
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    // getPieceTeam() returns the team the current piece is with (BLACK or WHITE).
    public Team getPieceTeam() {
        return this.pieceTeam;
    }
    // allowedMoves(board) calculates the allowed moves on the given parameter ChessBoard for the current ChessPiece.
    public abstract Collection<Move> allowedMoves(final ChessBoard board);
    // getPosition() returns the current piece coordinate on the chess board (index).
    public int getPiecePosition() {
        return this.piecePosition;
    }
    // All the types of chess piece:
    public enum pieceType {
        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        QUEEN("Q"),
        KING("K");

        private String pieceName;
        pieceType (final String pieceName) {
            this.pieceName = pieceName;
        }
        // printing piece type:
        @Override
        public String toString() {
            return this.pieceName;
        }
    }
}
