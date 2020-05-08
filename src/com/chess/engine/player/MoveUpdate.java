package com.chess.engine.player;

import com.chess.engine.board.ChessBoard;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.ChessPiece;

public class MoveUpdate {
    private final ChessBoard updatedBoard;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveUpdate(final ChessBoard updatedBoard, final Move move, final MoveStatus moveStatus) {
        this.updatedBoard = updatedBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    // getMoveStatus() returns the current move status.
    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
    // getUpdatedBoard() returns the updated board after executing the move.
    public ChessBoard getUpdatedBoard() {
        return this.updatedBoard;
    }
}
