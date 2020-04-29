package com.chess.engine;

public enum Team {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }
    };
    // getDirection() returns the direction in which the chess pieces of each team should move.
    public abstract int getDirection();
    // isWhite() checks if the piece is in the white team.
    public abstract boolean isWhite();
    // isBlack() checks if the piece is in the black team.
    public abstract boolean isBlack();
}
