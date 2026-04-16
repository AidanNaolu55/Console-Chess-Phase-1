package gui;

import board.Board;
import board.Position;
import pieces.*;
import utils.Color;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Represents the Graphical User Interface for the Console Chess Game.
 */
public class ChessGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares = new JButton[8][8];
    private Board backendBoard;

    /**
     * Constructor for the ChessGUI.
     * @param board The backend chess board containing the game state.
     */
    public ChessGUI(Board board) {
        this.backendBoard = board;

        frame = new JFrame("Chess Game - Phase 2");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8)); 

        initializeBoard();
        refreshBoard(); // Draw the pieces for the first time

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Fills the board panel with JButton squares and colors them.
     */
    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setOpaque(true);
                square.setBorderPainted(false);
                // Set a large font so the Unicode chess characters are visible
                square.setFont(new Font("SansSerif", Font.PLAIN, 60));
                square.setHorizontalAlignment(SwingConstants.CENTER);

                if ((row + col) % 2 == 0) {
                    square.setBackground(java.awt.Color.decode("#F0D9B5")); 
                } else {
                    square.setBackground(java.awt.Color.decode("#B58863")); 
                }

                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
    }

    /**
     * Syncs the visual GUI board with the backend Board state.
     */
    public void refreshBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = backendBoard.getPiece(new Position(row, col));
                if (piece != null) {
                    squares[row][col].setText(getPieceSymbol(piece));
                } else {
                    squares[row][col].setText(""); // Empty square
                }
            }
        }
    }

    /**
     * Translates a backend Piece object into its corresponding Unicode character.
     * @param piece The piece to translate.
     * @return The Unicode string representation of the piece.
     */
    private String getPieceSymbol(Piece piece) {
        boolean isWhite = piece.getColor() == Color.WHITE;

        if (piece instanceof King) return isWhite ? "\u2654" : "\u265A";
        if (piece instanceof Queen) return isWhite ? "\u2655" : "\u265B";
        if (piece instanceof Rook) return isWhite ? "\u2656" : "\u265C";
        if (piece instanceof Bishop) return isWhite ? "\u2657" : "\u265D";
        if (piece instanceof Knight) return isWhite ? "\u2658" : "\u265E";
        if (piece instanceof Pawn) return isWhite ? "\u2659" : "\u265F";
        
        return "";
    }
}