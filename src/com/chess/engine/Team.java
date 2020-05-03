package com.chess.engine;

import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

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

        @Override
        public Player selectPlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return whitePlayer;
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

        @Override
        public Player selectPlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };
    // getDirection() returns the direction in which the chess pieces of each team should move.
    public abstract int getDirection();
    // isWhite() checks if the piece is in the white team.
    public abstract boolean isWhite();
    // isBlack() checks if the piece is in the black team.
    public abstract boolean isBlack();
    // selectPlayer() returns the corresponding player associated with the specific Team.
    public abstract Player selectPlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);
}
