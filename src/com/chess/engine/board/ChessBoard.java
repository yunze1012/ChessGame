package com.chess.engine.board;

import com.chess.engine.Team;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class ChessBoard {
    private final List<ChessTile> board;
    private final Collection<ChessPiece> whitePieces;
    private final Collection<ChessPiece> blackPieces;
    private final static int TOTAL_TILES = 64;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentMovingPlayer;

    private ChessBoard(final Builder builder) {
        this.board = createBoard(builder);
        this.whitePieces = onBoardPieces(this.board, Team.WHITE);
        this.blackPieces = onBoardPieces(this.board, Team.BLACK);
        final Collection<Move> allWhiteLegalMoves = allLegalMoves(this.whitePieces);
        final Collection<Move> allBlackLegalMoves = allLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, allWhiteLegalMoves, allBlackLegalMoves);
        this.blackPlayer = new BlackPlayer(this, allWhiteLegalMoves, allBlackLegalMoves);
        this.currentMovingPlayer = builder.nextMover.selectPlayer(this.whitePlayer, this.blackPlayer);
    }
    // toString() is for debug printing purpose:
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < TOTAL_TILES; i++) {
            final String tilePrint = this.board.get(i).toString();
            builder.append(String.format("%3s", tilePrint));
            if ((i + 1) % 8 == 0) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    // allLegalMoves(teamPieces) returns all the possible legal moves given a collection of chess pieces of a specific
    //  team on the current board.
    private Collection<Move> allLegalMoves(Collection<ChessPiece> teamPieces) {
        final List<Move> legalMoves = new ArrayList<>();
        // adding all legal moves for each individual pieces from the team on the board:
        for (final ChessPiece piece : teamPieces) {
            legalMoves.addAll(piece.allowedMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    // onBoardPieces(curBoard, team) returns all the current chess pieces present on the chess board owned by the
    //  selected team.
    private static Collection<ChessPiece> onBoardPieces(final List<ChessTile> curBoard, final Team team) {
        final List<ChessPiece> pieces = new ArrayList<>(); // current pieces on the chess board of the selected team
        // checking all the tiles:
        for (final ChessTile tile : curBoard) {
            if(tile.isTileOccupied()) {
                final ChessPiece curPiece = tile.getPiece();
                // if the current tile is occupied AND it is occupied by a piece from the selected team:
                if (curPiece.getPieceTeam() == team) {
                    pieces.add(curPiece);
                }
            }
        }
        return ImmutableList.copyOf(pieces);
    }

    // createBoard() creates a new chess board with 64 tiles (list of 64 tiles).
    private static List<ChessTile> createBoard(final Builder builder) {
        final ChessTile[] tiles = new ChessTile[TOTAL_TILES];
        // process of creating all tiles of the current state of game:
        for (int i = 0; i < TOTAL_TILES; i++) {
            // PROCESS: get the piece from piecesPosition associated with current tile index and build a tile from it.
            //  Repeat for each possible tile on the board.
            tiles[i] = ChessTile.createTile(i, builder.piecesPosition.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }
    // gameInitialize() creates a new initialized chess board (game reset status) with 64 tiles with white team moving
    //  first.
    public static ChessBoard gameInitialize() {
        final Builder builder = new Builder();
        // BLACK TEAM PIECES:
        builder.putPiece(new Rook(0, Team.BLACK));
        builder.putPiece(new Knight(1, Team.BLACK));
        builder.putPiece(new Bishop(2, Team.BLACK));
        builder.putPiece(new Queen(3, Team.BLACK));
        builder.putPiece(new King(4, Team.BLACK));
        builder.putPiece(new Bishop(5, Team.BLACK));
        builder.putPiece(new Knight(6, Team.BLACK));
        builder.putPiece(new Rook(7, Team.BLACK));
        builder.putPiece(new Pawn(8, Team.BLACK));
        builder.putPiece(new Pawn(9, Team.BLACK));
        builder.putPiece(new Pawn(10, Team.BLACK));
        builder.putPiece(new Pawn(11, Team.BLACK));
        builder.putPiece(new Pawn(12, Team.BLACK));
        builder.putPiece(new Pawn(13, Team.BLACK));
        builder.putPiece(new Pawn(14, Team.BLACK));
        builder.putPiece(new Pawn(15, Team.BLACK));
        // WHITE TEAM PIECES:
        builder.putPiece(new Rook(56, Team.WHITE));
        builder.putPiece(new Knight(57, Team.WHITE));
        builder.putPiece(new Bishop(58, Team.WHITE));
        builder.putPiece(new Queen(59, Team.WHITE));
        builder.putPiece(new King(60, Team.WHITE));
        builder.putPiece(new Bishop(61, Team.WHITE));
        builder.putPiece(new Knight(62, Team.WHITE));
        builder.putPiece(new Rook(63, Team.WHITE));
        builder.putPiece(new Pawn(48, Team.WHITE));
        builder.putPiece(new Pawn(49, Team.WHITE));
        builder.putPiece(new Pawn(50, Team.WHITE));
        builder.putPiece(new Pawn(51, Team.WHITE));
        builder.putPiece(new Pawn(52, Team.WHITE));
        builder.putPiece(new Pawn(53, Team.WHITE));
        builder.putPiece(new Pawn(54, Team.WHITE));
        builder.putPiece(new Pawn(55, Team.WHITE));

        builder.setMover(Team.WHITE);
        return builder.build();
    }

    // getAllLegalMoves() returns all legal moves for all players on the current board.
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }

    // A chess board object builder:
    public static class Builder {
        Map<Integer, ChessPiece> piecesPosition; // all chess pieces coordinate on the current board
        Team nextMover; // next moving team
        Pawn enPassantPawn;

        public Builder() {
            this.piecesPosition = new HashMap<>();
        }
        // build() creates and returns a new ChessBoard object.
        public ChessBoard build() {
            return new ChessBoard(this);
        }
        // putPiece(piece) puts a piece on the chess board.
        public Builder putPiece(final ChessPiece piece) {
            this.piecesPosition.put(piece.getPiecePosition(), piece);
            return this;
        }
        // setMover() sets a new Team that will move next (White or Black).
        public Builder setMover(final Team team) {
            this.nextMover = team;
            return this;
        }
        // setEnPassant() sets a pawn as a possible en passant pawn.
        //TODO
        public void setEnPassant(Pawn movingPawn) {
        }
    }

    // getTile() returns the ChessTile at the given coordinate on the current ChessBoard.
    public ChessTile getTile(final int coordinate) {
        return board.get(coordinate);
    }
    // getBlackPieces() returns the collection of all the black pieces currently on the board.
    public Collection<ChessPiece> getBlackPieces() {
        return this.blackPieces;
    }
    // getWhitePieces() returns the collection of all the white pieces currently on the board.
    public Collection<ChessPiece> getWhitePieces() {
        return this.whitePieces;
    }
    // getWhitePlayer() returns the white player in the chess game.
    public Player getWhitePlayer() {
        return this.whitePlayer;
    }
    // getBlackPlayer() returns the black player in the chess game.
    public Player getBlackPlayer() {
        return this.blackPlayer;
    }
    // getCurrentMovingPlayer() returns the current moving player.
    public Player getCurrentMovingPlayer() {
        return this.currentMovingPlayer;
    }
}
