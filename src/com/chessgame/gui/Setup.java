package com.chessgame.gui;

import com.chessgame.player.Player;
import com.chessgame.player.Team;

import javax.swing.*;
import java.awt.*;

public class Setup extends JDialog {
    private Table.PlayerType whitePlayerType;
    private Table.PlayerType blackPlayerType;

    private static final String HUMAN_STRING = "Human";
    private static final String AI_STRING = "AI";

    public Setup(final JFrame frame, final boolean modal) {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_STRING);
        final JRadioButton whiteComputerButton = new JRadioButton(AI_STRING);
        final JRadioButton blackHumanButton = new JRadioButton(HUMAN_STRING);
        final JRadioButton blackComputerButton = new JRadioButton(AI_STRING);
        whiteHumanButton.setActionCommand(HUMAN_STRING);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButton);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackComputerButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);

        final JButton cancelButton = new JButton("Cancel");
        final JButton confirmButton = new JButton("Confirm");

        confirmButton.addActionListener(e -> {
            whitePlayerType = whiteComputerButton.isSelected() ? Table.PlayerType.AI : Table.PlayerType.HUMAN;
            blackPlayerType = blackComputerButton.isSelected() ? Table.PlayerType.AI : Table.PlayerType.HUMAN;
            Setup.this.setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            System.out.println("Cancel");
            Setup.this.setVisible(false);
        });

        myPanel.add(cancelButton);
        myPanel.add(confirmButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    public void promptUser() {
        setVisible(true);
        repaint();
    }

    // isAIPlayer(player) checks if the given player is an AI player.
    public boolean isAIPlayer(final Player player) {
        if(player.getTeam() == Team.WHITE) {
            return getWhitePlayerType() == Table.PlayerType.AI;
        }
        return getBlackPlayerType() == Table.PlayerType.AI;
    }

    // getWhitePlayerType() returns the current white player type.
    public Table.PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    // getBlackPlayerType() returns the current black player type.
    public Table.PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }
}
