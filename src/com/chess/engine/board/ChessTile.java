package com.chess.engine.board;

import com.chess.engine.pieces.ChessPiece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class ChessTile{
    protected final int tileCoordinates; // the current tile coordinates on the chess board

    private static final Map<Integer, emptyTile> EMPTY_TILES = createAllEmptyTiles();

    // Generates all possible tiles on an 8x8 chess board (64 tiles total).
    private static Map<Integer, emptyTile> createAllEmptyTiles() {
        final Map<Integer, emptyTile> emptyTileMap = new HashMap<>();
        final int TOTAL_TILES = 64;
        for (int i = 0; i < TOTAL_TILES; i++) {
            emptyTileMap.put(i, new emptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    public static ChessTile createTile(final int coordinates, final ChessPiece piece) {
        if (piece == null) {
            return EMPTY_TILES.get(coordinates);
        }
        return new occupiedTile(coordinates, piece);
    }

    private ChessTile(int coordinates) {
        this.tileCoordinates = coordinates;
    }
    // isTileOccupied() checks if the current tile is occupied by a chess piece.
    public abstract boolean isTileOccupied();
    // getPiece() gets the information of the chess piece on the current tile.
    public abstract ChessPiece getPiece();

    // Subclass provides the property of an empty tile to the class ChessTile.
    public static final class emptyTile extends ChessTile{

        public emptyTile(final int coordinate) {
            super(coordinate);
        }

        @Override
        public boolean isTileOccupied() {
            return false;
        }
        // There is no piece on an empty tile to return.
        @Override
        public ChessPiece getPiece() {
            return null;
        }
    }

    // Subclass provides the property of an occupied tile to the class ChessTile.
    public static final class occupiedTile extends ChessTile{
        private final ChessPiece currentPiece; // ChessPiece on the current tile.
        public occupiedTile(int coordinate, ChessPiece curPiece) {
            super(coordinate);
            this.currentPiece = curPiece;
        }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public ChessPiece getPiece() {
            return this.currentPiece;
        }
    }
}