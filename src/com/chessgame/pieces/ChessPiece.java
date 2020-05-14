package com.chessgame.pieces;

import com.chessgame.player.Team;
import com.chessgame.board.ChessBoard;
import com.chessgame.board.Move;

import java.util.Collection;

public abstract class ChessPiece {
    protected final int piecePosition;
    protected final Team pieceTeam;
    protected final boolean isFirstMove;
    protected final pieceType typeOfPiece;

    public ChessPiece(final pieceType typeOfPiece, final int posn, final Team team, final boolean isFirstMove) {
        this.piecePosition = posn;
        this.pieceTeam = team;
        this.isFirstMove = isFirstMove;
        this.typeOfPiece = typeOfPiece;
    }

    // ifFirstMove() checks if it is this chessgame piece's first move on the chessgame board.
    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    // getPieceTeam() returns the team the current piece is with (BLACK or WHITE).
    public Team getPieceTeam() {
        return this.pieceTeam;
    }

    // getPosition() returns the current piece coordinate on the chessgame board (index).
    public int getPiecePosition() {
        return this.piecePosition;
    }

    // getPieceType() returns the type of the current chessgame piece.
    public pieceType getPieceType() {
        return this.typeOfPiece;
    }

    // getPiecePoints() returns the points value of the current piece.
    public int getPiecePoints() {
        return this.getPieceType().getPiecePoints();
    }

    @Override
    public boolean equals(final Object compared) {
        if (this == compared) {
            return true;
        }
        // if the compared object is not a chessgame piece, then it is surely false:
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
        int result = typeOfPiece.hashCode();
        result = 31 * result + pieceTeam.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    // movePiece() returns a new ChessPiece with the move applied on the current chessgame piece:
    public abstract ChessPiece movePiece(Move move);

    // allowedMoves(board) calculates the allowed moves on the given parameter ChessBoard for the current ChessPiece.
    public abstract Collection<Move> allowedMoves(final ChessBoard board);

    // All the types of chessgame piece:
    public enum pieceType {
        PAWN("PAWN", 100){
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT("KNIGHT", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP("BISHOP", 300) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK("ROOK", 500) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        QUEEN("QUEEN", 900) {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KING("KING", 10000) {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };

        private final String pieceName;
        private final int piecePoints;

        pieceType (final String pieceName, final int piecePoints) {
            this.pieceName = pieceName;
            this.piecePoints = piecePoints;
        }
        // printing piece type:
        @Override
        public String toString() {
            return this.pieceName;
        }

        // isKing() checks if the current chessgame piece is a King.
        public abstract boolean isKing();

        // isRook() checks if the current chessgame piece is a Rook.
        public abstract boolean isRook();

        // getPiecePoints() returns the points value of the current piece type.
        public int getPiecePoints() {
            return this.piecePoints;
        }
    }
}
