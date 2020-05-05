package com.chess.engine.player;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.ChessPiece;
import com.chess.engine.pieces.King;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final ChessBoard board;
    protected final King king;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player (final ChessBoard board, final Collection<Move> myMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.king = setKing();
        this.legalMoves = myMoves;
        // if there is a possible attack on the tile where the King is, then the player is in Check.
        this.isInCheck = !Player.attackOnTile(this.king.getPiecePosition(), opponentMoves).isEmpty();
    }

    // attackOnTile() returns all the possible attack moves on the selected tile.
    protected static Collection<Move> attackOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMoves = new ArrayList<>();
        // checks all moves in the collection, and if there is a move in which its destination has the same tile
        //  coordinate (tile index number) as the piece's position (piece coordinate index), then the move is attacking
        //  the piece (the move is defined as an attack move):
        for (final Move move: moves) {
            if (piecePosition == move.getDestinationCrd()) {
                attackMoves.add(move);
            }
        }
        return ImmutableList.copyOf(attackMoves);
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
    // getKing() returns the current player's King.
    public King getKing() {
        return this.king;
    }
    // getLegalMoves() returns the collection of legal moves of the current player.
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    // isLegalMove(move) checks if the parameter Move is a legal move.
    public boolean isLegalMove(final Move move) {
        return this.legalMoves.contains(move);
    }
    // isCheck() checks if the current player is in check.
    public boolean isCheck() {
        return this.isInCheck;
    }
    // isCheckMate() checks if the current player is in check mate.
    public boolean isCheckMate() {
        return this.isInCheck && !hasEscapeMove();
    }
    // isStaleMate() checks if the current game is in stale mate (draw game).
    public boolean isStaleMate() {
        return !this.isInCheck && !hasEscapeMove();
    }

    // hasEscapeMove() checks if the current player has an escape move.
    protected boolean hasEscapeMove() {
        // We verify individually each move in all the potential legal moves, and if there exists a move that is
        //  updatable (can be completed), then the player has a possible escape move:
        for (final Move move : this.legalMoves) {
            // imaginary move to verify if it can be completed:
            final MoveUpdate verificationTransition = makeMove(move);
            if(verificationTransition.getMoveStatus().isCompleted()) {
                return true;
            }
        }
        return false;
    }
    // TODO isCastled() function implementation:
    // isCastled() checks if the player has used his castling move.
    public boolean isCastled() {
        return false;
    }

    // makeMove(move) makes a move on the chess board (updated the current ChessBoard with a new ChessBoard with the
    //  move already executed using the MoveUpdate class).
    public MoveUpdate makeMove(final Move move) {
        // if the move is not a legal move, then "update" its status to ILLEGAL_MOVE and the move is not executed.
        if (!isLegalMove(move)){
            return new MoveUpdate(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        // POTENTIAL UPDATED new ChessBoard after making the move:
        final ChessBoard updateBoard = move.executeMove();
        // all the enemy attacks on the current player's King on the CURRENT chess board:
        final Collection<Move> attacksOnKing =
                Player.attackOnTile(updateBoard.getCurrentMovingPlayer().getOpponent().getKing().getPiecePosition(),
                        updateBoard.getCurrentMovingPlayer().getLegalMoves());
        // if there is at least one attack on the current player's King, then the current player is in Check and the
        //  move is not executed:
        if(!attacksOnKing.isEmpty()) {
            return new MoveUpdate(this.board, move, MoveStatus.IN_CHECK);
        }
        // otherwise, the current ChessBoard is updated to the new ChessBoard with the move COMPLETED and executed on
        //  the current ChessBoard:
        return new MoveUpdate(updateBoard, move, MoveStatus.COMPLETED);
    }

    // calculateCastlingMoves() calculates all the castling moves available for the current player on the board.
    protected abstract Collection<Move> calculateCastlingMoves(Collection<Move> legalMoves, Collection<Move> opponentLegalMoves);
}
