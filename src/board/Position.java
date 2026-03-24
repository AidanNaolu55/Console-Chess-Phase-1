package board;

/**
 * Represents a specific square on the 8x8 chessboard.
 */
public class Position {
    
    private int row;
    private int column;

    // constructor
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    // read the row and column
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}