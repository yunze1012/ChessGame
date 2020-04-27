package com.chess.engine.board;

public class BoardUtils {
    // Index holds true if the corresponding tile index of the board falls on the corresponding columns of the board.
    //  Otherwise, index holds false:
    public static final boolean[] FIRST_COLUMN = null;
    public static final boolean[] SECOND_COLUMN = null;
    public static final boolean[] SEVENTH_COLUMN = null;
    public static final boolean[] LAST_COLUMN = null;

    private BoardUtils() {
        throw new RuntimeException("Not Instantiatable!");
    }
    // isValidTileCoordinate(coordinate) checks if the parameter coordinate is a valid chess board coordinate.
    public static boolean isValidTileCoordinate(int coordinate) {
        // Tile coordinates (indexes) goes from 0 to 63, with a total of 64 tiles
        return coordinate >= 0 && coordinate <= 63;
    }
}
