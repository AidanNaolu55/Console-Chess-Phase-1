package pieces;

import board.Board;
import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Pawn extends Piece {

    public Pawn(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        
        // White pawns move UP the array (row decreases), Black moves DOWN (row increases)
        int direction = (this.color == Color.WHITE) ? -1 : 1;
        int startRow = (this.color == Color.WHITE) ? 6 : 1;

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        // 1. Move forward one square
        Position forwardOne = new Position(currentRow + direction, currentCol);
        if (board.isWithinBounds(forwardOne) && board.getPiece(forwardOne) == null) {
            moves.add(forwardOne);

            // 2. Move forward two squares (only if it hasn't moved AND path is clear)
            if (currentRow == startRow) {
                Position forwardTwo = new Position(currentRow + (direction * 2), currentCol);
                if (board.getPiece(forwardTwo) == null) {
                    moves.add(forwardTwo);
                }
            }
        }

        // 3. Capturing diagonally
        int[] captureCols = {currentCol - 1, currentCol + 1};
        for (int col : captureCols) {
            Position diagonal = new Position(currentRow + direction, col);
            if (board.isWithinBounds(diagonal)) {
                Piece target = board.getPiece(diagonal);
                // Must be an enemy piece to move diagonally
                if (target != null && target.getColor() != this.color) {
                    moves.add(diagonal);
                }
            }
        }

        return moves;
    }
}