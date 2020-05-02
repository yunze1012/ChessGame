package com.chess.engine.player;

public enum MoveStatus {
    COMPLETED {
        @Override
        boolean isCompleted() {
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        boolean isCompleted() {
            return false;
        }
    },
    IN_CHECK {
        @Override
        boolean isCompleted() {
            return false;
        }
    };
    // isCompleted() checks if the move is completed;
    abstract boolean isCompleted();
}
