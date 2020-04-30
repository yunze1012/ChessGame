package com.chess.engine;

import com.chess.engine.board.ChessBoard;

public class GameDrive {

    public static void main(String[] args) {

        ChessBoard board = ChessBoard.gameInitialize();
        System.out.println(board);
    }
}
