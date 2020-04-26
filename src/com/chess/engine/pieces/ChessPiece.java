package com.chess.engine.pieces;
public class ChessPiece {
    protected final int piecePosition;
    protected final Alliance pieceAlliance;

    public ChessPiece(final int posn, final Alliance alliance) {
        this.piecePosition = posn;
        this.pieceAlliance = alliance;
    }
}
