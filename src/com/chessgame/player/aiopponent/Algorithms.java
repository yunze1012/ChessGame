package com.chessgame.player.aiopponent;

import com.chessgame.board.ChessBoard;
import com.chessgame.movement.Move;

public interface Algorithms {

    Move runAlgorithm(ChessBoard board, int treeLevel);
}
