package com.chess.engine.player;

public enum MoveStatus {
    COMPLETED {
        @Override
        public boolean isCompleted() {
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isCompleted() {
            return false;
        }
    },
    IN_CHECK {
        @Override
        public boolean isCompleted() {
            return false;
        }
    };
    // isCompleted() checks if the move is completed;
    public abstract boolean isCompleted();
}
