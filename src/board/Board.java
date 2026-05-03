package board;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import pieces.*;
import utils.Color;

public class Board implements Serializable {
    private Piece[][] grid;
    private List<Piece> capturedPieces; // Phase 1 requirement [cite: 60]

    public Board() {
        grid = new Piece[8][8];
        capturedPieces = new ArrayList<>();
        setupBoard();
    }

    /**
     * Initializes the standard chess starting positions.
     */
    private void setupBoard() {
        // Initialize Pawns
        for (int col = 0; col < 8; col++) {
            grid[1][col] = new Pawn(Color.BLACK, new Position(1, col));
            grid[6][col] = new Pawn(Color.WHITE, new Position(6, col));
        }

        // Initialize Rooks
        grid[0][0] = new Rook(Color.BLACK, new Position(0, 0));
        grid[0][7] = new Rook(Color.BLACK, new Position(0, 7));
        grid[7][0] = new Rook(Color.WHITE, new Position(7, 0));
        grid[7][7] = new Rook(Color.WHITE, new Position(7, 7));

        // Initialize Knights
        grid[0][1] = new Knight(Color.BLACK, new Position(0, 1));
        grid[0][6] = new Knight(Color.BLACK, new Position(0, 6));
        grid[7][1] = new Knight(Color.WHITE, new Position(7, 1));
        grid[7][6] = new Knight(Color.WHITE, new Position(7, 6));

        // Initialize Bishops
        grid[0][2] = new Bishop(Color.BLACK, new Position(0, 2));
        grid[0][5] = new Bishop(Color.BLACK, new Position(0, 5));
        grid[7][2] = new Bishop(Color.WHITE, new Position(7, 2));
        grid[7][5] = new Bishop(Color.WHITE, new Position(7, 5));

        // Initialize Queens
        grid[0][3] = new Queen(Color.BLACK, new Position(0, 3));
        grid[7][3] = new Queen(Color.WHITE, new Position(7, 3));

        // Initialize Kings
        grid[0][4] = new King(Color.BLACK, new Position(0, 4));
        grid[7][4] = new King(Color.WHITE, new Position(7, 4));
    }

    public Piece getPiece(Position position) {
        if (isWithinBounds(position)) {
            return grid[position.getRow()][position.getColumn()];
        }
        return null;
    }

    /**
     * Physically moves a piece on the board and handles basic capturing[cite: 63, 139].
     * (Move validation happens BEFORE calling this method).
     */
    public void movePiece(Position from, Position to) {
        Piece movingPiece = getPiece(from);
        Piece targetPiece = getPiece(to);

        if (movingPiece != null) {
            // If there's an opponent piece there, capture it
            if (targetPiece != null) {
                capturedPieces.add(targetPiece);
            }
            
            // Update the 2D array
            grid[to.getRow()][to.getColumn()] = movingPiece;
            grid[from.getRow()][from.getColumn()] = null;
            
            // Update the piece's internal tracker
            movingPiece.setPosition(to);
        }
    }

    // Helper methods for the pieces to check for legal moves
    public boolean isWithinBounds(Position pos) {
        return pos.getRow() >= 0 && pos.getRow() < 8 && pos.getColumn() >= 0 && pos.getColumn() < 8;
    }
    
    public boolean isSquareEmpty(Position pos) {
        return isWithinBounds(pos) && getPiece(pos) == null;
    }

    // --- NEW METHODS FOR CHECK AND CHECKMATE ---

    public Position getKingPosition(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    public boolean isCheck(Color color) {
        Position kingPos = getKingPosition(color);
        if (kingPos == null) return false;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                // If it's an opponent's piece, see if it can attack the King
                if (piece != null && piece.getColor() != color) {
                    java.util.List<Position> moves = piece.possibleMoves(this);
                    if (moves.contains(kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean testMoveLeavesKingInCheck(Position from, Position to, Color color) {
        Piece movingPiece = getPiece(from);
        Piece targetPiece = getPiece(to);

        // Simulate the move temporarily
        grid[to.getRow()][to.getColumn()] = movingPiece;
        grid[from.getRow()][from.getColumn()] = null;
        movingPiece.setPosition(to);

        boolean inCheck = isCheck(color);

        // Undo the temporary move
        grid[from.getRow()][from.getColumn()] = movingPiece;
        grid[to.getRow()][to.getColumn()] = targetPiece;
        movingPiece.setPosition(from);

        return inCheck;
    }

    public boolean isCheckmate(Color color) {
        
        if (!isCheck(color)) return false; 

        // Check if ANY piece has a valid move that gets the King out of check
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                if (piece != null && piece.getColor() == color) {
                    java.util.List<Position> moves = piece.possibleMoves(this);
                    for (Position move : moves) {
                        if (!testMoveLeavesKingInCheck(new Position(row, col), move, color)) {
                            // We found at least one move that saves the King!
                            return false; 
                        }
                    }
                }
            }
        }
        return true;
    }
}