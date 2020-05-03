package com.chess.engine.player;

import com.chess.engine.Team;
import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.ChessPiece;

import java.util.Collection;

public class BlackPlayer extends Player {
    public BlackPlayer(final ChessBoard board, final Collection<Move> whiteMoves, final Collection<Move> blackMoves) {
        super(board, blackMoves, whiteMoves);
    }

    // for general function purpose, see Player class file:
    @Override
    public Collection<ChessPiece> getActivePieces() {
        return this.board.getBlackPieces();
    }
    @Override
    public Team getTeam() {
        return Team.BLACK;
    }
    @Override
    public Player getOpponent() {
        return this.board.getWhitePlayer();
    }
}
