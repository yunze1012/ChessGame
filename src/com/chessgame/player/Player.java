package com.chessgame.player;

import com.chessgame.board.ChessBoard;
import com.chessgame.movement.Move;
import com.chessgame.movement.BoardUpdate;
import com.chessgame.pieces.ChessPiece;
import com.chessgame.pieces.King;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {
    protected final ChessBoard board;
    protected final King king;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;
    private boolean hasCastled;

    public Player (final ChessBoard board, final Collection<Move> myMoves, final Collection<Move> opponentMoves) {
        this.board = board;
        this.king = setKing();
        // Get all our possible normal legal moves and all our possible castling moves:
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(myMoves, calculateCastlingMoves(myMoves, opponentMoves)));
        // if there is a possible attack on the tile where the King is, then the player is in Check.
        this.isInCheck = !Player.attackOnTile(this.king.getPiecePosition(), opponentMoves).isEmpty();
        this.hasCastled = false;
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

    // castled() changes hasCastled to true when executing the castling move.
    public void castled() {
        this.hasCastled = true;
    }

    // isCastled() checks if the current player has used his castling move.
    public boolean isCastled() { //TODO complete implementation
        return this.hasCastled;
    }

    // hasEscapeMove() checks if the current player has an escape move.
    protected boolean hasEscapeMove() {
        // We verify individually each move in all the potential legal moves, and if there exists a move that is
        //  updatable (can be completed), then the player has a possible escape move:
        for (final Move move : this.legalMoves) {
            // imaginary move to verify if it can be completed:
            final BoardUpdate verificationTransition = makeMove(move);
            if(verificationTransition.getMoveStatus().isCompleted()) {
                return true;
            }
        }
        return false;
    }

    // makeMove(move) makes a move on the chess board (updated the current ChessBoard with a new ChessBoard with the
    //  move already executed using the MoveUpdate class).
    public BoardUpdate makeMove(final Move move) {
        // if the move is not a legal move, then "update" its status to ILLEGAL_MOVE and the move is not executed.
        if (!isLegalMove(move)){
            return new BoardUpdate(this.board, Move.MoveStatus.ILLEGAL_MOVE);
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
            return new BoardUpdate(this.board, Move.MoveStatus.IN_CHECK);
        }
        // otherwise, the current ChessBoard is updated to the new ChessBoard with the move COMPLETED and executed on
        //  the current ChessBoard:
        return new BoardUpdate(updateBoard, Move.MoveStatus.COMPLETED);
    }

    // calculateCastlingMoves() calculates all the castling moves available for the current player on the board.
    protected abstract Collection<Move> calculateCastlingMoves(Collection<Move> legalMoves, Collection<Move> opponentLegalMoves);

    // getActivePieces() returns all the current active pieces on the chess board for this player.
    public abstract Collection<ChessPiece> getActivePieces();

    // getTeam() returns the team of the current player.
    public abstract Team getTeam();

    // getOpponent() returns the opponent player.
    public abstract Player getOpponent();
}
