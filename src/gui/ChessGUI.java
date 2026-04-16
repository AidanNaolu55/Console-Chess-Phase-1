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
import javax.swing.JOptionPane;
// --- NEW IMPORTS FOR MENU BAR ---
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import javax.swing.JFileChooser;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Represents the Graphical User Interface for the Console Chess Game.
 */
public class ChessGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares = new JButton[8][8];
    private Board backendBoard;
    
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
        
        // --- ADDED MENU BAR INITIALIZATION ---
        setupMenuBar();

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Builds the top menu bar with Game controls.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");

        // Wire up the New Game button
        newGameItem.addActionListener(e -> {
            backendBoard = new Board(); // Create a brand new backend board
            selectedPosition = null;    // Clear any selections
            resetBoardColors();         // Fix visual highlights
            refreshBoard();             // Redraw the starting pieces
        });

        // Wire up the Save and Load buttons
        saveGameItem.addActionListener(e -> saveGameToFile());
        loadGameItem.addActionListener(e -> loadGameFromFile());

        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        menuBar.add(gameMenu);

        frame.setJMenuBar(menuBar);
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                final int r = row;
                final int c = col;

                JButton square = new JButton();
                square.setOpaque(true);
                square.setBorderPainted(false);
                square.setFont(new Font("SansSerif", Font.PLAIN, 60));
                square.setHorizontalAlignment(SwingConstants.CENTER);

                square.addActionListener(e -> handleSquareClick(r, c));

                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
        resetBoardColors();
    }

    private void handleSquareClick(int row, int col) {
        Position clickedPos = new Position(row, col);

        // Click 1: Selecting a piece
        if (selectedPosition == null) {
            Piece clickedPiece = backendBoard.getPiece(clickedPos);
            if (clickedPiece != null) { 
                selectedPosition = clickedPos;
                squares[row][col].setBackground(java.awt.Color.decode("#FFFFA0")); 
            }
        } 
        // Click 2: Moving the selected piece to a destination
        else {
            Piece movingPiece = backendBoard.getPiece(selectedPosition);
            Piece targetPiece = backendBoard.getPiece(clickedPos);
            
            boolean isKingCaptured = (targetPiece instanceof King);

            backendBoard.movePiece(selectedPosition, clickedPos);
            
            selectedPosition = null; 
            resetBoardColors();      
            refreshBoard();          

            if (isKingCaptured) {
                String winner = (movingPiece.getColor() == Color.WHITE) ? "White" : "Black";
                JOptionPane.showMessageDialog(frame, 
                    winner + " wins! The King has been captured.", 
                    "Game Over", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                System.exit(0); 
            }
        }
    }

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

    /**
     * Opens a file dialog and saves the current Board object to a file.
     */
    private void saveGameToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);
        
        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(fileChooser.getSelectedFile()))) {
                
                out.writeObject(backendBoard);
                JOptionPane.showMessageDialog(frame, "Game saved successfully!");
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Error saving game: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Opens a file dialog, loads a Board object from a file, and redraws the GUI.
     */
    private void loadGameFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        
        if (option == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(fileChooser.getSelectedFile()))) {
                
                backendBoard = (Board) in.readObject();
                
                // Reset the GUI state to match the loaded board
                selectedPosition = null;
                resetBoardColors();
                refreshBoard();
                
                JOptionPane.showMessageDialog(frame, "Game loaded successfully!");
                
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, 
                    "Error loading game: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}