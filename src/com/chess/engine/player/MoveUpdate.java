package com.chess.engine.player;

import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;

public class MoveUpdate {
    private final ChessBoard updatedBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveUpdate(final ChessBoard updatedBoard, final Move move, final MoveStatus moveStatus) {
        this.updatedBoard = updatedBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
}
