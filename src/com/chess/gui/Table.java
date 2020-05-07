package com.chess.gui;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.ChessBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final JFrame mainFrame;
    private final ChessBoardPanel boardPanel;
    private final static Dimension MAIN_FRAME_DIMENSION = new Dimension(600, 600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private final Color whiteColor = Color.decode("#FDF9F9");
    private final Color blackColor = Color.decode("#737272");

    public Table() {
        this.mainFrame = new JFrame("Chess");
        this.mainFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenu = generateOptionsBar();
        this.mainFrame.setJMenuBar(tableMenu);
        this.mainFrame.setSize(MAIN_FRAME_DIMENSION);
        this.boardPanel = new ChessBoardPanel();
        this.mainFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.mainFrame.setVisible(true);
    }

    // generateOptionsBar() creates and returns the options bar at the top of the main frame.
    private JMenuBar generateOptionsBar() {
        final JMenuBar tableMenu = new JMenuBar();
        tableMenu.add(createFileMenu());
        return tableMenu;
    }
    // createFileMenu() creates and returns the File menu option inside the options bar.
    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            // TEST PURPOSE:
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open PGN File");
            }
        });
        fileMenu.add(openPGN);
        final JMenuItem quitGame = new JMenuItem("Quit");
        quitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(quitGame);
        return fileMenu;
    }

    // visual component representing the chess board (map to ChessBoard):
    private class ChessBoardPanel extends JPanel {
        final List<ChessTilePanel> tiles; // all the 64 tiles

        ChessBoardPanel() {
            super(new GridLayout(8, 8)); // 8 x 8 chess board
            this.tiles = new ArrayList<>();
            // adding each individual ChessTilePanel to a case of ChessBoardPanel and keep track of each individual tile:
            for(int i = 0; i < 64; i++) {
                final ChessTilePanel tile = new ChessTilePanel(this, i);
                this.tiles.add(tile);
                add(tile);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();;
        }
    }
    // visual component representing the chess tiles (map to ChessTile):
    private class ChessTilePanel extends JPanel {
        private final int tileIndex;

        ChessTilePanel(final ChessBoardPanel board, final int index) {
            super(new GridBagLayout());
            this.tileIndex = index;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            validate();
        }
        // assignTileColor() gives a color (black or white) to an individual chess tile panel.
        private void assignTileColor() {
            // Row No 1, 3, 5, 7 has a white case if the index is even and black case if the index if odd.
            // ie. It follows the white, black, white, black, ... pattern.
            if(BoardUtils.FIRST_ROW[this.tileIndex] || BoardUtils.THIRD_ROW[this.tileIndex] ||
                    BoardUtils.FIFTH_ROW[this.tileIndex] || BoardUtils.SEVENTH_ROW[this.tileIndex]) {
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
            else if(BoardUtils.SECOND_ROW[this.tileIndex] || BoardUtils.FOURTH_ROW[this.tileIndex] ||
                        BoardUtils.SIXTH_ROW[this.tileIndex] || BoardUtils.LAST_ROW[this.tileIndex]) {
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
                String pieceImagePath = ""; // path of the directory of the chess pieces images.
                try {
                    // this is just the image name convention in the directory:
                    final BufferedImage image =
                            ImageIO.read(new File(pieceImagePath +
                                    board.getTile(this.tileIndex).getPiece().getPieceTeam().toString().substring(0, 1) +
                                    board.getTile(this.tileIndex).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
