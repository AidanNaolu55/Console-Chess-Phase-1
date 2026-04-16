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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
// --- NEW IMPORTS FOR HISTORY AND UNDO ---
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.util.Stack;
import java.io.*;

/**
 * Represents the Graphical User Interface for the Console Chess Game.
 */
public class ChessGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares = new JButton[8][8];
    private Board backendBoard;
    
    private Position selectedPosition = null;

    // --- NEW VARIABLES FOR HISTORY PANEL ---
    private JTextArea moveHistoryArea;
    private Stack<byte[]> undoStack = new Stack<>(); // Stores board snapshots

    public ChessGUI(Board board) {
        this.backendBoard = board;

        frame = new JFrame("Chess Game - Phase 2");
        frame.setSize(1000, 800); // Made the window a bit wider for the side panel
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8)); 

        initializeBoard();
        refreshBoard(); 
        
        setupMenuBar();
        setupHistoryPanel(); // --- ADDED HISTORY PANEL ---

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Builds the right-side panel tracking moves, captures, and the Undo button.
     */
    private void setupHistoryPanel() {
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());

        moveHistoryArea = new JTextArea(20, 25);
        moveHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);

        JButton undoButton = new JButton("Undo Last Move");
        undoButton.addActionListener(e -> undoMove());

        historyPanel.add(new JLabel(" Game History", SwingConstants.CENTER), BorderLayout.NORTH);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(undoButton, BorderLayout.SOUTH);

        frame.add(historyPanel, BorderLayout.EAST);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");

        newGameItem.addActionListener(e -> {
            backendBoard = new Board(); 
            selectedPosition = null;    
            undoStack.clear();          // Clear undo history
            moveHistoryArea.setText(""); // Clear text history
            resetBoardColors();         
            refreshBoard();             
        });

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
                square.setFont(new Font("SansSerif", Font.PLAIN, 45));
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
            
            boolean isKingCaptured = (targetPiece != null && targetPiece instanceof King);

            // --- TAKE A SNAPSHOT FOR THE UNDO STACK BEFORE MOVING ---
            undoStack.push(takeBoardSnapshot());

            // --- GENERATE MOVE HISTORY TEXT ---
            String startLoc = "" + (char)('A' + selectedPosition.getColumn()) + (8 - selectedPosition.getRow());
            String endLoc = "" + (char)('A' + clickedPos.getColumn()) + (8 - clickedPos.getRow());
            String pieceName = movingPiece.getClass().getSimpleName();
            String moveText = pieceName + " moved: " + startLoc + " -> " + endLoc;
            
            if (targetPiece != null) {
                moveText += " (Captured " + targetPiece.getClass().getSimpleName() + ")";
            }
            moveHistoryArea.append(moveText + "\n");

            // Execute the physical move
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

    /**
     * Reverts the board to the last saved snapshot in the undo stack.
     */
    private void undoMove() {
        if (!undoStack.isEmpty()) {
            byte[] previousState = undoStack.pop();
            backendBoard = restoreBoardSnapshot(previousState);
            
            selectedPosition = null;
            resetBoardColors();
            refreshBoard();
            
            moveHistoryArea.append(">> Move Undone\n");
        }
    }

    // --- HELPER METHODS TO CREATE DEEP COPIES USING SERIALIZATION ---

    private byte[] takeBoardSnapshot() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(backendBoard);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    private Board restoreBoardSnapshot(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            return (Board) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Board(); 
        }
    }

    // --- REST OF THE PREVIOUS HELPER METHODS ---

    private void saveGameToFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                out.writeObject(backendBoard);
                JOptionPane.showMessageDialog(frame, "Game saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error saving game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadGameFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                backendBoard = (Board) in.readObject();
                selectedPosition = null;
                undoStack.clear();
                moveHistoryArea.setText(">> Game Loaded\n");
                resetBoardColors();
                refreshBoard();
                JOptionPane.showMessageDialog(frame, "Game loaded successfully!");
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
}