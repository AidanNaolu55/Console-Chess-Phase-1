package board;

// import pieces and colors
import pieces.*;
import utils.Color;

public class Board {
    // This is our 8x8 grid. It is an array of arrays that holds 'Piece' objects.
    private Piece[][] grid;

    // Constructor: When a new Board is created, build the grid and set up the pieces
    public Board() {
        grid = new Piece[8][8]; // 8 rows, 8 columns
        initializeBoard();
    }

    /**
     * Sets up the board with pieces in their standard starting positions.
     */
    private void initializeBoard() {
        // 1. Place Black Pieces (Top of the board: Row 0 and Row 1)
        grid[0][0] = new Rook(Color.BLACK, new Position(0, 0));
        grid[0][1] = new Knight(Color.BLACK, new Position(0, 1));
        grid[0][2] = new Bishop(Color.BLACK, new Position(0, 2));
        grid[0][3] = new Queen(Color.BLACK, new Position(0, 3));
        grid[0][4] = new King(Color.BLACK, new Position(0, 4));
        grid[0][5] = new Bishop(Color.BLACK, new Position(0, 5));
        grid[0][6] = new Knight(Color.BLACK, new Position(0, 6));
        grid[0][7] = new Rook(Color.BLACK, new Position(0, 7));

        // Place 8 Black Pawns in Row 1
        for (int col = 0; col < 8; col++) {
            grid[1][col] = new Pawn(Color.BLACK, new Position(1, col));
        }

        // 2. Place White Pieces (Bottom of the board: Row 6 and Row 7)
        // Place 8 White Pawns in Row 6
        for (int col = 0; col < 8; col++) {
            grid[6][col] = new Pawn(Color.WHITE, new Position(6, col));
        }

        grid[7][0] = new Rook(Color.WHITE, new Position(7, 0));
        grid[7][1] = new Knight(Color.WHITE, new Position(7, 1));
        grid[7][2] = new Bishop(Color.WHITE, new Position(7, 2));
        grid[7][3] = new Queen(Color.WHITE, new Position(7, 3));
        grid[7][4] = new King(Color.WHITE, new Position(7, 4));
        grid[7][5] = new Bishop(Color.WHITE, new Position(7, 5));
        grid[7][6] = new Knight(Color.WHITE, new Position(7, 6));
        grid[7][7] = new Rook(Color.WHITE, new Position(7, 7));
    }

    /**
     * Prints the current state of the board to the console.
     */
    public void display() {
        System.out.println("   A  B  C  D  E  F  G  H"); // Top letters
        
        for (int row = 0; row < 8; row++) {
            // Print the row number on the left side (8 down to 1)
            System.out.print((8 - row) + " "); 

            for (int col = 0; col < 8; col++) {
                Piece piece = grid[row][col];
                
                // If the cubby is empty, print two pound signs
                if (piece == null) {
                    System.out.print(" ##");
                } else {
                    // Figure out the letter to print based on the piece type
                    String pieceString = "";
                    if (piece.getColor() == Color.WHITE) pieceString += "w";
                    else pieceString += "b";

                    if (piece instanceof Pawn) pieceString += "p";
                    else if (piece instanceof Rook) pieceString += "R";
                    else if (piece instanceof Knight) pieceString += "N";
                    else if (piece instanceof Bishop) pieceString += "B";
                    else if (piece instanceof Queen) pieceString += "Q";
                    else if (piece instanceof King) pieceString += "K";

                    System.out.print(" " + pieceString);
                }
            }
            System.out.println(); // Move to the next line after finishing a row
        }
    }

    /**
     * Returns the piece at the specified position.
     */
    public Piece getPiece(Position position) {
        return grid[position.getRow()][position.getColumn()];
    }
    
    /**
     * Moves a piece from one position to another.
     * For Phase 1, this just physically moves the piece in the array 
     * without checking complex chess rules.
     */
    public boolean movePiece(Position from, Position to) {
        // Find the piece at the starting square
        Piece piece = grid[from.getRow()][from.getColumn()];
        
        if (piece == null) {
            System.out.println("Error: There is no piece at the starting position!");
            return false;
        }

        // Put the piece in the new square
        grid[to.getRow()][to.getColumn()] = piece;
        // Empty out the old square
        grid[from.getRow()][from.getColumn()] = null;
        
        // Update the piece's internal record of where it lives
        piece.setPosition(to); 
        
        return true;
    }
}