package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pieces.ChessPiece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.chess.gui.Table.*;

public class CapturedPieces extends JPanel {
    private static final Color PANEL_COLOUR = Color.decode("#DAB0B0");
    private static final Dimension PANEL_DIMENSION = new Dimension(80, 80);
    private static final EtchedBorder BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private final JPanel topSide;
    private final JPanel bottomSide;

    public CapturedPieces() {
        super(new BorderLayout());
        setBackground(PANEL_COLOUR);
        setBorder(BORDER);
        this.topSide = new JPanel(new GridLayout(8, 2));
        this.topSide.setBackground(PANEL_COLOUR);
        this.bottomSide = new JPanel(new GridLayout(8, 2));
        this.bottomSide.setBackground(PANEL_COLOUR);
        add(this.topSide, BorderLayout.NORTH);
        add(this.bottomSide, BorderLayout.SOUTH);
        setPreferredSize(PANEL_DIMENSION);
    }

    // redraw(moveHistory) redraws the captured pieces panel after a new move is made.
    public void redraw(final MoveHistory moveHistory) {
        // first, remove everything inside the old panel:
        this.topSide.removeAll();
        this.bottomSide.removeAll();
        final List<ChessPiece> whiteCapturedPieces = new ArrayList<>();
        final List<ChessPiece> blackCapturedPieces = new ArrayList<>();

        // for all the killer moves, separate each individual captured piece into their corresponding team's list:
        for (final Move move : moveHistory.getMoves()) {
            if(move.isKillerMove()) {
                final ChessPiece capturedPiece = move.getTargetedPiece();
                if(capturedPiece.getPieceTeam().isBlack()) {
                    blackCapturedPieces.add(capturedPiece);
                }
                else {
                    whiteCapturedPieces.add(capturedPiece);
                }
            }
        }
        // now, sort each pieces in the captured pieces list according to their value points in a Chess game
        //  (from small to big) so that the display can be made from small pieces to big pieces captured:
        Collections.sort(whiteCapturedPieces, new Comparator<ChessPiece>() {
            @Override
            public int compare(ChessPiece o1, ChessPiece o2) {
                return Ints.compare(o1.getPiecePoints(), o2.getPiecePoints());
            }
        });
        Collections.sort(blackCapturedPieces, new Comparator<ChessPiece>() {
            @Override
            public int compare(ChessPiece o1, ChessPiece o2) {
                return Ints.compare(o1.getPiecePoints(), o2.getPiecePoints());
            }
        });
        // add each corresponding piece's image to the captured pieces panel for each team
        // (black captured pieces at bottom -> white side of board, and white captured pieces on top -> black side of board):
        for(final ChessPiece piece : whiteCapturedPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("images/pieces/" +
                        piece.getPieceTeam().toString() + piece.toString() + ".jpg"));
                final ImageIcon icon= new ImageIcon(image);
                final JLabel label = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 500, icon.getIconWidth() - 500, Image.SCALE_SMOOTH)));
                this.topSide.add(label);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        for(final ChessPiece piece : blackCapturedPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("images/pieces/" +
                        piece.getPieceTeam().toString() + piece.toString() + ".jpg"));
                final ImageIcon icon= new ImageIcon(image);
                final JLabel label = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(
                        icon.getIconWidth() - 500, icon.getIconWidth() - 500, Image.SCALE_SMOOTH)));
                this.bottomSide.add(label);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}
