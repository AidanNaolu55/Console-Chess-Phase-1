package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares = new JButton[8][8];

    public ChessGUI() {
        // 1. Set up the main window
        frame = new JFrame("Chess Game - Phase 2");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 2. Set up the panel that will hold the grid
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8)); // Creates the 8x8 grid 

        initializeBoard();

        // 3. Add the board to the window and make it visible
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Fills the board with buttons and colors them in a checkerboard pattern.
     */
    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setOpaque(true);
                square.setBorderPainted(false);

                // If the sum of the row and column is even, it's a light square. Otherwise, dark. 
                // Note: We use java.awt.Color explicitly here so it doesn't confuse Java 
                // with your custom utils.Color enum!
                if ((row + col) % 2 == 0) {
                    square.setBackground(java.awt.Color.decode("#F0D9B5")); // Light wood color
                } else {
                    square.setBackground(java.awt.Color.decode("#B58863")); // Dark wood color
                }

                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
    }
}