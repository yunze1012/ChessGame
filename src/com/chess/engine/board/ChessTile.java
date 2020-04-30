package com.chess.engine.board;

import com.chess.engine.pieces.ChessPiece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class ChessTile{
    protected final int tileCoordinates; // the current tile coordinates on the chess board

    // Holding all empty tiles in the Map.
    private static final Map<Integer, emptyTile> EMPTY_TILES = createAllEmptyTiles();

    // createAllEmptyTiles() generates all possible tiles on an 8x8 chess board (64 tiles total).
    private static Map<Integer, emptyTile> createAllEmptyTiles() {
        final Map<Integer, emptyTile> emptyTileMap = new HashMap<>();
        final int TOTAL_TILES = 64; // number of total tiles possible
        for (int i = 0; i < TOTAL_TILES; i++) {
            emptyTileMap.put(i, new emptyTile(i));
        }
        return ImmutableMap.copyOf(emptyTileMap);
    }

    // createTile(coordinates, piece) creates a new chess board tile with the given coordinates and chess piece
    //  (or null for empty tile).
    public static ChessTile createTile(final int coordinates, final ChessPiece piece) {
        // if there is no piece on the tile to create, the return an empty tile with the coordinates:
        if (piece == null) {
            return EMPTY_TILES.get(coordinates);
        }
        // otherwise, return an occupied tile with the exact chess piece at the coordinates:
        return new occupiedTile(coordinates, piece);
    }

    private ChessTile(final int coordinates) {
        this.tileCoordinates = coordinates;
    }
    // isTileOccupied() checks if the current tile is occupied by a chess piece.
    public abstract boolean isTileOccupied();
    // getPiece() gets the information of the chess piece on the current tile.
    public abstract ChessPiece getPiece();

    // Subclass provides the property of an empty tile to the class ChessTile.
    public static final class emptyTile extends ChessTile{

        private emptyTile(final int coordinate) {
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
        // TILE PRINTING PURPOSE:
        @Override
        public String toString() {
            return "-";
        }
    }

    // Subclass provides the property of an occupied tile to the class ChessTile.
    public static final class occupiedTile extends ChessTile{
        private final ChessPiece currentPiece; // ChessPiece on the current tile.
        private occupiedTile(int coordinate, final ChessPiece curPiece) {
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
        // TILE PRINTING PURPOSE:
        @Override
        public String toString() {
            if (getPiece().getPieceTeam().isBlack()) {
                return getPiece().toString().toLowerCase();
            }
            return getPiece().toString();
        }
    }
}
