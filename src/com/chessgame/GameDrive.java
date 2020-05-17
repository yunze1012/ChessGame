package com.chessgame;

import com.chessgame.board.ChessBoard;
import com.chessgame.gui.Table;

public class GameDrive {

    public static void main(String[] args) {

        ChessBoard board = ChessBoard.gameInitialize();
        System.out.println(board);

        Table.get().display();
    }
}
