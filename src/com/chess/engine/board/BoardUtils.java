package com.chess.engine.board;

public class BoardUtils {
    public static final int TOTAL_TILES = 64;
    // Index holds true if the corresponding tile index of the board falls on the corresponding columns of the board.
    //  Otherwise, index holds false:
    public static final boolean[] FIRST_COLUMN = createColumn(0);
    public static final boolean[] SECOND_COLUMN = createColumn(1);
    public static final boolean[] SEVENTH_COLUMN = createColumn(6);
    public static final boolean[] LAST_COLUMN = createColumn(7);
    // Index holds true if the corresponding tile index of the board falls on the corresponding rows of the board.
    //  Otherwise, index holds false:
    public static final boolean[] FIRST_ROW = createRow(0);
    public static final boolean[] SECOND_ROW = createRow(8);
    public static final boolean[] THIRD_ROW = createRow(16);
    public static final boolean[] FOURTH_ROW = createRow(24);
    public static final boolean[] FIFTH_ROW = createRow(32);
    public static final boolean[] SIXTH_ROW = createRow(40);
    public static final boolean[] SEVENTH_ROW = createRow(48);
    public static final boolean[] LAST_ROW = createRow(56);

    private BoardUtils() {
        throw new RuntimeException("Not Instantiatable!");
    }
    // isValidTileCoordinate(coordinate) checks if the parameter coordinate is a valid chess board coordinate.
    public static boolean isValidTileCoordinate(final int coordinate) {
        // Tile coordinates (indexes) goes from 0 to 63, with a total of 64 tiles
        return coordinate >= 0 && coordinate <= 63;
    }

    // createColumn(columnNum) sets a specified column number and all tiles in that column are all set to true, the rest
    //  of tiles are all false.
    private static boolean[] createColumn(int column) {
        final boolean[] board = new boolean[TOTAL_TILES];
        do {
            board[column] = true;
            column += 8; // change to next tile in the column
        } while(column < TOTAL_TILES);
        return board;
    }

    // createRow(rowIndex) sets a specific starting index of a row in the chess board and all tiles in that row are all
    //  set to true, the rest of tiles are all false.
    private static boolean[] createRow(int rowIndex) {
        final boolean[] board = new boolean[TOTAL_TILES];
        do {
            board[rowIndex] = true;
            rowIndex ++;
        } while(rowIndex % 8 != 0); // while still on this row
        return board;
    }
}
