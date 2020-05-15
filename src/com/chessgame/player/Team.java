package com.chessgame.player;

import com.chessgame.board.ChessBoard;

public enum Team {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getEnemyDirection() {
            return 1;
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

        @Override
        public boolean isPromotionTile(final int coordinate) {
            return ChessBoard.FIRST_ROW[coordinate];
        }
    },
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getEnemyDirection() {
            return -1;
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

        @Override
        public boolean isPromotionTile(final int coordinate) {
            return ChessBoard.LAST_ROW[coordinate];
        }
    };
    // getDirection() returns the direction in which the chess pieces of each team should move.
    public abstract int getDirection();

    // getOppositeDirection() returns the enemy's direction in which the chess pieces of the enemy team should move.
    public abstract int getEnemyDirection();

    // isWhite() checks if the piece is in the white team.
    public abstract boolean isWhite();

    // isBlack() checks if the piece is in the black team.
    public abstract boolean isBlack();

    // selectPlayer() returns the corresponding player associated with the specific Team.
    public abstract Player selectPlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer);

    // isPromotionTile(coordinate) determines if the tile with given coordinate is a pawn promotion possible tile.
    public abstract boolean isPromotionTile(final int coordinate);
}
