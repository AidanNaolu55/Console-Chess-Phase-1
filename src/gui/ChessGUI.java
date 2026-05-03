package gui;

import board.Position;
import game.Game;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.*;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import pieces.*;
import utils.Color;

/**
 * Represents the Graphical User Interface for the Chess Game.
 */
public class ChessGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares = new JButton[8][8];
    
    // --- UPDATED: NOW USES GAME INSTEAD OF BOARD ---
    private Game backendGame;
    
    private Position selectedPosition = null;

    private JTextArea moveHistoryArea;
    private Stack<byte[]> undoStack = new Stack<>();

    // --- UPDATED CONSTRUCTOR ---
    public ChessGUI() {
        this.backendGame = new Game(); // Initialize the game controller

        frame = new JFrame("Chess Game - Phase 3");
        frame.setSize(1000, 800); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(8, 8)); 

        initializeBoard();
        refreshBoard(); 
        
        setupMenuBar();
        setupHistoryPanel(); 

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

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
            backendGame = new Game(); // --- UPDATED ---
            selectedPosition = null;    
            undoStack.clear();          
            moveHistoryArea.setText(""); 
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
            Piece clickedPiece = backendGame.getBoard().getPiece(clickedPos);
            
            if (clickedPiece != null) { 
                // --- NEW: CHECK IF IT IS THIS PLAYER'S TURN ---
                if (clickedPiece.getColor() != backendGame.getCurrentTurn()) {
                    JOptionPane.showMessageDialog(frame, 
                        "It is " + backendGame.getCurrentTurn() + "'s turn!", 
                        "Not Your Turn", 
                        JOptionPane.WARNING_MESSAGE);
                    return; 
                }

                selectedPosition = clickedPos;
                squares[row][col].setBackground(java.awt.Color.decode("#FFFFA0")); 
            }
        } 
        // Click 2: Moving the selected piece to a destination
        else {
            Piece movingPiece = backendGame.getBoard().getPiece(selectedPosition);
            java.util.List<Position> legalMoves = movingPiece.possibleMoves(backendGame.getBoard());
            
            // --- NEW: FILTER OUT MOVES THAT LEAVE THE KING IN CHECK ---
            legalMoves.removeIf(pos -> backendGame.getBoard().testMoveLeavesKingInCheck(selectedPosition, pos, movingPiece.getColor()));
            
            if (legalMoves.contains(clickedPos)) {
                Piece targetPiece = backendGame.getBoard().getPiece(clickedPos);

                // Take snapshot of the GAME before moving
                undoStack.push(takeGameSnapshot());

                String startLoc = "" + (char)('A' + selectedPosition.getColumn()) + (8 - selectedPosition.getRow());
                String endLoc = "" + (char)('A' + clickedPos.getColumn()) + (8 - clickedPos.getRow());
                String pieceName = movingPiece.getClass().getSimpleName();
                String moveText = pieceName + " moved: " + startLoc + " -> " + endLoc;
                
                if (targetPiece != null) {
                    moveText += " (Captured " + targetPiece.getClass().getSimpleName() + ")";
                }
                moveHistoryArea.append(moveText + "\n");

                // Execute physical move
                backendGame.getBoard().movePiece(selectedPosition, clickedPos);
                
                // Switch turns after successful move
                backendGame.switchTurn();

                selectedPosition = null; 
                resetBoardColors();      
                refreshBoard();          

                // --- NEW: CHECKMATE AND CHECK NOTIFICATIONS ---
                Color nextTurnColor = backendGame.getCurrentTurn();
                
                if (backendGame.getBoard().isCheckmate(nextTurnColor)) {
                    String winner = (nextTurnColor == Color.WHITE) ? "Black" : "White";
                    JOptionPane.showMessageDialog(frame, 
                        "Checkmate! " + winner + " wins!", 
                        "Game Over", 
                        JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0); 
                } else if (backendGame.getBoard().isCheck(nextTurnColor)) {
                    JOptionPane.showMessageDialog(frame, 
                        nextTurnColor + " is in Check!", 
                        "Check", 
                        JOptionPane.WARNING_MESSAGE);
                }
                
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Invalid move! You cannot move there, or it leaves your King in check.", 
                    "Illegal Move", 
                    JOptionPane.WARNING_MESSAGE);
                selectedPosition = null; 
                resetBoardColors();      
            }
        }
    }

    private void undoMove() {
        if (!undoStack.isEmpty()) {
            byte[] previousState = undoStack.pop();
            backendGame = restoreGameSnapshot(previousState);
            
            selectedPosition = null;
            resetBoardColors();
            refreshBoard();
            
            moveHistoryArea.append(">> Move Undone\n");
        }
    }

    // --- UPDATED TO SAVE/LOAD GAME INSTEAD OF BOARD ---
    private byte[] takeGameSnapshot() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(backendGame);
            out.flush();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Game restoreGameSnapshot(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(bis);
            return (Game) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Game(); 
        }
    }

    private void saveGameToFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                out.writeObject(backendGame);
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
                backendGame = (Game) in.readObject();
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
                Piece piece = backendGame.getBoard().getPiece(new Position(row, col));
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