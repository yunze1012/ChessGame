package com.chessgame.player.aiopponent;

import com.chessgame.board.ChessBoard;

public interface BoardScore {
    // NOTE:
    // A positive score means that the human player (WHITE TEAM) is currently winning.
    // A negative score means that the AI opponent (BLACK TEAM) is currently winning.
    // A neutral score means that the game is currently equal.
    int score(ChessBoard board, int treeLevel);
}
