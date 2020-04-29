package com.chess.engine.board;

import com.chess.engine.Team;
import com.chess.engine.pieces.ChessPiece;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

public class ChessBoard {
    private final List<ChessTile> board;
    private final static int TOTAL_TILES = 64;

    private ChessBoard(Builder builder) {
        this.board = createBoard(builder);
    }
    // createBoard() creates a new chess board with 64 tiles (list of 64 tiles).
    private static List<ChessTile> createBoard(final Builder builder) {
        final ChessTile[] tiles = new ChessTile[TOTAL_TILES];
        // process of creating all tiles of the current state of game:
        for (int i = 0; i < TOTAL_TILES; i++) {
            // PROCESS: get the piece from piecesPosition associated with current tile index and build a tile from it.
            //  Repeat for each possible tile on the board.
            tiles[i] = ChessTile.createTile(i, builder.piecesPosition.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }
    // createInitialBoard() creates a new chess board with 64 tiles, but in the initial chess game state (game reset).
    public static ChessBoard createInitialBoard() {
        return null; //TODO complete implementation
    }
    // A chess board object builder:
    public static class Builder {
        Map<Integer, ChessPiece> piecesPosition; // all chess pieces coordinate on the current board
        Team nextMover; // next moving team

        public Builder() {

        }
        // build() creates and returns a new ChessBoard object.
        public ChessBoard build() {
            return new ChessBoard(this);
        }
        // putPiece(piece) puts a piece on the chess board.
        public Builder putPiece(final ChessPiece piece) {
            this.piecesPosition.put(piece.getPiecePosition(), piece);
            return this;
        }
        // setMover() sets a new Team that will move next (White or Black).
        public Builder setMover(final Team team) {
            this.nextMover = team;
            return this;
        }
    }

    // getTile() returns the ChessTile at the given coordinate on the current ChessBoard.
    public ChessTile getTile(final int coordinate) {
        return null; // PLACEHOLDER
    }
}
