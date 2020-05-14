package com.chessgame.player.aiopponent;

import com.chessgame.board.ChessBoard;
import com.chessgame.board.Move;

public class Minimax implements Algorithms{
    private final BoardScore boardScore;

    public Minimax() {
        this.boardScore = null;
    }

    @Override
    public Move runAlgorithm(ChessBoard board, int treeLevel) {
        return null;
    }

    @Override
    public String toString() {
        return "Minimax";
    }
}
