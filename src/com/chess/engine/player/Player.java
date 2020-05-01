package com.chess.engine.player;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.ChessPiece;
import com.chess.engine.pieces.King;

import java.util.Collection;

public abstract class Player {
    protected final ChessBoard board;
    protected final King king;
    protected final Collection<Move> legalMoves;

    public Player (final ChessBoard board, final Collection<Move> myMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.king = setKing();
        this.legalMoves = myMoves;
    }

    // setKing() returns the King piece on the board of the current player.
    private King setKing() {
        for (final ChessPiece piece : getActivePieces()) {
            if(piece.getPieceType().isKing()) {
                return (King) piece;
            }
        }
        throw new RuntimeException("NO KING ON BOARD!");
    }

    // getActivePieces() returns all the current active pieces on the chess board for this player.
    public abstract Collection<ChessPiece> getActivePieces();
    // getTeam() returns the team of the current player.
    public abstract Team getTeam();
    // getOpponent() returns the opponent player.
    public abstract Player getOpponent();

    // isLegalMove(move) checks if the parameter Move is a legal move.
    public boolean isLegalMove(final Move move) {
        return this.legalMoves.contains(move);
    }
    // TODO following function implementations:
    // isCheck() checks if the current player is in check.
    public boolean isCheck() {
        return false;
    }
    // isCheckMate() checks if the current player is in check mate.
    public boolean isCheckMate() {
        return false;
    }
    public boolean isStaleMate() {
        return false;
    }
    public boolean isCastled() {
        return false;
    }
    // makeMove(move) makes a move on the chess board.
    public MoveUpdate makeMove(final Move move) {
        return null;
    }

}
