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
    
    // Tracks the piece currently selected by the user
    private Position selectedPosition = null;

    public ChessGUI(Board board) {
        this.backendBoard = board;

        frame = new JFrame("Chess Game - Phase 2");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8)); 

        initializeBoard();
        refreshBoard(); 

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // These final variables are required so the lambda expression 
                // inside the ActionListener knows exactly which coordinates it holds
                final int r = row;
                final int c = col;

                JButton square = new JButton();
                square.setOpaque(true);
                square.setBorderPainted(false);
                square.setFont(new Font("SansSerif", Font.PLAIN, 60));
                square.setHorizontalAlignment(SwingConstants.CENTER);

                // Add the click listener
                square.addActionListener(e -> handleSquareClick(r, c));

                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
        resetBoardColors(); // Sets the initial wood colors
    }

    /**
     * Handles the logic for a two-click movement system.
     */
    private void handleSquareClick(int row, int col) {
        Position clickedPos = new Position(row, col);

        // Click 1: Selecting a piece
        if (selectedPosition == null) {
            Piece clickedPiece = backendBoard.getPiece(clickedPos);
            
            // Only select if the square actually has a piece in it
            if (clickedPiece != null) { 
                selectedPosition = clickedPos;
                squares[row][col].setBackground(java.awt.Color.decode("#FFFFA0")); // Highlight yellow
            }
        } 
        // Click 2: Moving the selected piece to a destination
        else {
            // Note: Phase 2 instructions say "Movement validation is not required". 
            // So we blindly move the piece. If an opponent is there, movePiece() 
            // naturally overwrites them, fulfilling the capture requirement!
            backendBoard.movePiece(selectedPosition, clickedPos);
            
            selectedPosition = null; // Clear the selection state
            resetBoardColors();      // Remove the yellow highlight
            refreshBoard();          // Redraw the pieces in their new squares
        }
    }

    /**
     * Resets all squares back to their alternating dark and light wood colors.
     */
    private void resetBoardColors() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(java.awt.Color.decode("#F0D9B5")); 
                } else {
                    squares[row][col].setBackground(java.awt.Color.decode("#B58863")); 
                }
            }
        }
    }

    public void refreshBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = backendBoard.getPiece(new Position(row, col));
                if (piece != null) {
                    squares[row][col].setText(getPieceSymbol(piece));
                } else {
                    squares[row][col].setText(""); 
                }
            }
        }
    }

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