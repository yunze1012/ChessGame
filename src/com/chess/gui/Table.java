package com.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Table {
    private final JFrame mainFrame;
    private static Dimension MAIN_FRAME_DIMENSION = new Dimension(600, 600);

    public Table() {
        this.mainFrame = new JFrame("Chess");
        final JMenuBar tableMenuBar = new JMenuBar();
        generateOptionsBar(tableMenuBar); // generate the option and setting bar at the top
        this.mainFrame.setJMenuBar(tableMenuBar);
        this.mainFrame.setSize(MAIN_FRAME_DIMENSION);
        this.mainFrame.setVisible(true);
    }

    private void generateOptionsBar(final JMenuBar tableMenuBar) {
        tableMenuBar.add(createFileMenu());
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open PGN File");
            }
        });
        fileMenu.add(openPGN);
        return fileMenu;
    }
}
