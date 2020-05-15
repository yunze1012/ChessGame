package com.chessgame.gui;

import com.chessgame.board.ChessBoard;
import com.chessgame.board.ChessTile;
import com.chessgame.movement.Move;
import com.chessgame.pieces.ChessPiece;
import com.chessgame.movement.BoardUpdate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table {
    private final ChessBoardPanel boardPanel;
    private final CapturedPieces capturedPiecesPanel;
    private ChessBoard chessBoard;
    private final static Dimension MAIN_FRAME_DIMENSION = new Dimension(900, 900);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(600, 525);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(15, 15);
    private final Color whiteColor = Color.decode("#FDF9F9");
    private final Color blackColor = Color.decode("#737272");
    private final Color redColor = Color.decode("#FD603F");
    private final static String piecesImagesPath = "images/pieces/";
    private ChessTile currentTile;
    private ChessTile destTile;
    private ChessPiece onClickMovedPiece;
    private BoardOrientation boardOrientation;
    private final MoveHistory moveHistory;

    public Table() {
        JFrame mainFrame = new JFrame("Chess");
        mainFrame.setLayout(new BorderLayout());
        this.chessBoard = ChessBoard.gameInitialize();
        this.boardOrientation = BoardOrientation.WHITESIDE;
        final JMenuBar tableMenu = generateOptionsBar();
        mainFrame.setJMenuBar(tableMenu);
        mainFrame.setSize(MAIN_FRAME_DIMENSION);
        this.capturedPiecesPanel = new CapturedPieces();
        this.boardPanel = new ChessBoardPanel();
        this.moveHistory = new MoveHistory();
        mainFrame.add(this.boardPanel, BorderLayout.CENTER);
        mainFrame.add(this.capturedPiecesPanel, BorderLayout.WEST);
        mainFrame.setVisible(true);
    }

    // generateOptionsBar() creates and returns the options bar at the top of the main frame.
    private JMenuBar generateOptionsBar() {
        final JMenuBar tableMenu = new JMenuBar();
        tableMenu.add(createFileMenu());
        tableMenu.add(createCustomizeMenu());
        return tableMenu;
    }
    // createFileMenu() creates and returns the File menu option inside the options bar.
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem importPGN = new JMenuItem("Import PGN");
        // TEST PURPOSE:
        importPGN.addActionListener(e -> System.out.println("Import PGN File"));
        fileMenu.add(importPGN);
        final JMenuItem quitGame = new JMenuItem("Quit");
        quitGame.addActionListener(e -> System.exit(0));
        fileMenu.add(quitGame);
        return fileMenu;
    }

    // createPreferencesMenu() creates and returns the Preferences menu option inside the options bar.
    private JMenu createCustomizeMenu() {
        final JMenu customizeMenu = new JMenu("Customize");
        final JMenuItem changeSideMenuItem = new JMenuItem("Change Side");
        changeSideMenuItem.addActionListener(e -> {
            boardOrientation = boardOrientation.flip();
            boardPanel.displayBoard(chessBoard);
        });
        customizeMenu.add(changeSideMenuItem);
        return customizeMenu;
    }

    // visual component representing the chess board (map to ChessBoard):
    private class ChessBoardPanel extends JPanel {
        final List<ChessTilePanel> tiles; // all the 64 tiles

        ChessBoardPanel() {
            super(new GridLayout(8, 8)); // 8 x 8 chess board
            this.tiles = new ArrayList<>();
            // adding each individual ChessTilePanel to a case of ChessBoardPanel and keep track of each individual tile:
            for(int i = 0; i < 64; i++) {
                final ChessTilePanel tile = new ChessTilePanel(i);
                this.tiles.add(tile);
                add(tile);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        // displayBoard() displays the new chess board on the main frame.
        public void displayBoard(final ChessBoard board) {
            removeAll(); // clear frame first
            // displays each individual chess tiles on the new board (on main frame):
            for(final ChessTilePanel tile : boardOrientation.orientedTiles(tiles)) {
                tile.displayTile(board);
                add(tile);
            }
            validate();
            repaint();
        }
    }

    // visual component representing the chess tiles (map to ChessTile):
    private class ChessTilePanel extends JPanel {
        private final int tileIndex;

        ChessTilePanel(final int index) {
            super(new GridBagLayout());
            this.tileIndex = index;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignChessPiece(chessBoard);
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    // left mouse click events:
                    if(isLeftMouseButton(e)) {
                        // if there is no current clicked tile yet (first click):
                        if(currentTile == null) {
                            // then assign the next clicked tile's corresponding tile index to currentTile.
                            currentTile = chessBoard.getTile(tileIndex);
                            onClickMovedPiece = currentTile.getPiece(); // get the piece on that tile
                            // if the clicked tile has no piece on it (is empty), then reset everything back to initial
                            //  state (nothing happens):
                            if(onClickMovedPiece == null) {
                                currentTile = null;
                            }
                        }
                        // if there is already a current clicked tile (second click):
                        else {
                            // then assign the next clicked tile's corresponding tile index to destTile.
                            destTile = chessBoard.getTile(tileIndex);
                            // Create a new move given a start and a destination that is of course, contained in the
                            //  list of legal moves.
                            final Move move = Move.MoveCreator.createMove(chessBoard, currentTile.getTileCoordinates(),
                                    destTile.getTileCoordinates());
                            // MoveUpdate updates the current board by creating a new board after executing the move:
                            final BoardUpdate boardUpdate = chessBoard.getCurrentMovingPlayer().makeMove(move);
                            // After the move is completed, assign the new chessgame board (after executing the move) to
                            //  the Table's chessBoard:
                            if(boardUpdate.getMoveStatus().isCompleted()) {
                                chessBoard = boardUpdate.getUpdatedBoard();
                                moveHistory.addMove(move);
                            }
                            // Clear everything back to initial state once again:
                            currentTile = null;
                            destTile = null;
                            onClickMovedPiece = null;
                        }
                        // Display the new chess board:
                        SwingUtilities.invokeLater(() -> {
                            boardPanel.displayBoard(chessBoard);
                            capturedPiecesPanel.redraw(moveHistory);
                        });
                    }
                    // right mouse click event, cancel everything (cancel move):
                    else if (isRightMouseButton(e)) {
                        currentTile = null;
                        destTile = null;
                        onClickMovedPiece = null;
                    }
                }

                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();
        }

        // assignTileColor() gives a color (black or white) to an individual chess tile panel.
        private void assignTileColor() {
            // if either player is in Check and their King is on the current tile, then color the current tile in red
            if(chessBoard.getBlackPlayer().isCheck() &&
                    chessBoard.getBlackPlayer().getKing().getPiecePosition() == this.tileIndex) {
                setBackground(redColor);
            }
            else if(chessBoard.getWhitePlayer().isCheck() &&
                    chessBoard.getWhitePlayer().getKing().getPiecePosition() == this.tileIndex) {
                setBackground(redColor);
            }
            // Row No 1, 3, 5, 7 has a white case if the index is even and black case if the index if odd.
            // ie. It follows the white, black, white, black, ... pattern.
            else if(ChessBoard.FIRST_ROW[this.tileIndex] || ChessBoard.THIRD_ROW[this.tileIndex] ||
                    ChessBoard.FIFTH_ROW[this.tileIndex] || ChessBoard.SEVENTH_ROW[this.tileIndex]) {
                // if even, white:
                if(this.tileIndex % 2 == 0) {
                    setBackground(whiteColor);
                }
                // if odd, black:
                else {
                    setBackground(blackColor);
                }
            }
            // Row No 2, 4, 6, 8 has a white case if the index is odd and black case if the index is even.
            // ie. It follows the black, white, black, white, ... pattern.
            else if(ChessBoard.SECOND_ROW[this.tileIndex] || ChessBoard.FOURTH_ROW[this.tileIndex] ||
                        ChessBoard.SIXTH_ROW[this.tileIndex] || ChessBoard.LAST_ROW[this.tileIndex]) {
                // if even, black:
                if(this.tileIndex % 2 == 0) {
                    setBackground(blackColor);
                }
                // if odd, white:
                else {
                    setBackground(whiteColor);
                }
            }
        }
        // assignChessPiece(board) assigns each chess piece on the current board to the chess board JPanel
        //  (to draw and display it).
        private void assignChessPiece(final ChessBoard board) {
            // We have to remove all the tiles drawn on the current board JPanel first to redraw the new board panel.
            this.removeAll();
            // putting each piece image on the chess board panel on each individual chess tile panel:
            if(board.getTile(this.tileIndex).isTileOccupied()) {
                try {
                    // this is just the image name convention in the directory:
                    final BufferedImage image =
                            ImageIO.read(new File(piecesImagesPath +
                                    board.getTile(this.tileIndex).getPiece().getPieceTeam().toString() +
                                    board.getTile(this.tileIndex).getPiece().toString() + ".jpg"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // displayTile() displays the new chess tile on the chess board on the main frame.
        public void displayTile(final ChessBoard board) {
            assignTileColor(); // paint tile
            assignChessPiece(board); // put pieces on corresponding tiles on the current chess board
            highlightLegalTiles(board);
            validate();
            repaint();
        }
        // highlightLegalTiles() highlights the tile panel if the tile is a legal destination for the current piece.
        private void highlightLegalTiles(final ChessBoard board) {
            // for each legal move of the current selected piece:
            for(final Move move : movingPieceLegalMoves(board)) {
                // if the legal move's destination coordinate is the same as the current tile's coordinate, then
                //  highlight the current tile panel:
                if(move.getDestinationCrd() == this.tileIndex) {
                    try {
                        add(new JLabel(new ImageIcon(ImageIO.read(new File("images/blue_square.jpg")))));
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // movingPieceLegalMoves() returns the legal moves for the current moving piece.
        private Collection<Move> movingPieceLegalMoves(final ChessBoard board) {
            // all legal moves of the current moving player:
            final Iterable<Move> allLegalMoves = board.getCurrentMovingPlayer().getLegalMoves();
            // legal moves of the current moving piece:
            final List<Move> movingPieceLegalMoves = new ArrayList<>();
            // for all current moving player's legal moves, add all moves corresponding to the current moving piece to
            //  the list:
            for(final Move move : allLegalMoves) {
                if(move.getMovingPiece() == onClickMovedPiece) {
                    movingPieceLegalMoves.add(move);
                }
            }
            return ImmutableList.copyOf(movingPieceLegalMoves);
        }
    }

    // The class MoveHistory displays all the moves that have been executed before.
    public static class MoveHistory {
        private final List<Move> moves;

        MoveHistory() {
            this.moves = new ArrayList<>();
        }

        // getMoves() returns the list of moves that has been executed.
        public List<Move> getMoves() {
            return this.moves;
        }
        // addMove() adds a move to the list of executed moves.
        public void addMove(final Move move) {
            this.moves.add(move);
        }
    }

    // the orientation of the chess board, whether on the white side or the black side
    public enum BoardOrientation {
        WHITESIDE {
            @Override
            List<ChessTilePanel> orientedTiles(List<ChessTilePanel> tiles) {
                return tiles;
            }

            @Override
            BoardOrientation flip() {
                return BLACKSIDE;
            }
        },
        BLACKSIDE {
            @Override
            List<ChessTilePanel> orientedTiles(List<ChessTilePanel> tiles) {
                return Lists.reverse(tiles);
            }

            @Override
            BoardOrientation flip() {
                return WHITESIDE;
            }
        };
        // orientedTiles() returns the list of tiles ordered according to the current orientation (whether normal order
        //  or reversed order).
        abstract List<ChessTilePanel> orientedTiles(final List<ChessTilePanel> tiles);
        // flip() returns the flipped orientation of the board.
        abstract BoardOrientation flip();
    }
}
