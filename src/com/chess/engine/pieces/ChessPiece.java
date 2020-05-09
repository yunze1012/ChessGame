package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;

import java.util.Collection;

public abstract class ChessPiece {
    protected final int piecePosition;
    protected final Team pieceTeam;
    protected final boolean isFirstMove;
    protected final pieceType typeOfPiece;
    private final int pieceHashCode;

    public ChessPiece(final pieceType typeOfPiece, final int posn, final Team team, final boolean isFirstMove) {
        this.piecePosition = posn;
        this.pieceTeam = team;
        this.isFirstMove = isFirstMove;
        this.typeOfPiece = typeOfPiece;
        this.pieceHashCode = calculateHashCode();
    }

    // calculateHashCode() calculates the Hash Code of the current ChessPiece.
    private int calculateHashCode() {
        int result = typeOfPiece.hashCode();
        result = 31 * result + pieceTeam.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
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
    // movePiece() returns a new ChessPiece with the move applied on the current chess piece:
    public abstract ChessPiece movePiece(Move move);

    // All the types of chess piece:
    public enum pieceType {
        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private String pieceName;
        pieceType (final String pieceName) {
            this.pieceName = pieceName;
        }
        // printing piece type:
        @Override
        public String toString() {
            return this.pieceName;
        }
        // isKing() checks if the current chess piece is a King.
        public abstract boolean isKing();
        // isRook() checks if the current chess piece is a Rook.
        public abstract boolean isRook();
    }

    // getPieceType() returns the type of the current chess piece.
    public pieceType getPieceType() {
        return this.typeOfPiece;
    }

    @Override
    public boolean equals(final Object compared) {
        if (this == compared) {
            return true;
        }
        // if the compared object is not a chess piece, then it is surely false:
        if (!(compared instanceof ChessPiece)) {
            return false;
        }
        final ChessPiece comparedPiece = (ChessPiece) compared;
        // checks if all the fields matches:
        return piecePosition == comparedPiece.getPiecePosition() && typeOfPiece == comparedPiece.getPieceType() &&
                pieceTeam == comparedPiece.getPieceTeam() && isFirstMove == comparedPiece.isFirstMove();
    }
    @Override
    public int hashCode() {
        return this.pieceHashCode;
    }
}
