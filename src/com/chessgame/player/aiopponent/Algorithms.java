package com.chessgame.player.aiopponent;

import com.chessgame.board.ChessBoard;
import com.chessgame.board.Move;

public interface Algorithms {

    Move runAlgorithm(ChessBoard board, int treeLevel);
}
