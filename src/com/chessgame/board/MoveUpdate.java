package com.chessgame.board;

public class MoveUpdate {
    private final ChessBoard updatedBoard;
    private final Move.MoveStatus moveStatus;

    public MoveUpdate(final ChessBoard updatedBoard, final Move.MoveStatus moveStatus) {
        this.updatedBoard = updatedBoard;
        this.moveStatus = moveStatus;
    }

    // getMoveStatus() returns the current move status.
    public Move.MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    // getUpdatedBoard() returns the updated board after executing the move.
    public ChessBoard getUpdatedBoard() {
        return this.updatedBoard;
    }
}
