package com.chess.engine.board;

public class BoardUtils {
    public static final int TOTAL_TILES = 64;
    // Index holds true if the corresponding tile index of the board falls on the corresponding columns of the board.
    //  Otherwise, index holds false:
    public static final boolean[] FIRST_COLUMN = createColumn(0);
    public static final boolean[] SECOND_COLUMN = createColumn(1);
    public static final boolean[] SEVENTH_COLUMN = createColumn(6);
    public static final boolean[] LAST_COLUMN = createColumn(7);

    private BoardUtils() {
        throw new RuntimeException("Not Instantiatable!");
    }
    // isValidTileCoordinate(coordinate) checks if the parameter coordinate is a valid chess board coordinate.
    public static boolean isValidTileCoordinate(final int coordinate) {
        // Tile coordinates (indexes) goes from 0 to 63, with a total of 64 tiles
        return coordinate >= 0 && coordinate <= 63;
    }

    // createColumn(columnNum) sets a specified column number and all tiles in that column are all set to true, the rest of
    //  tiles are all false.
    private static boolean[] createColumn(int column) {
        final boolean[] board = new boolean[TOTAL_TILES];
        do {
            board[column] = true;
            column += 8; // change to next tile in the column
        } while(column < TOTAL_TILES);
        return board;
    }
}
