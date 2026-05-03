package pieces;

import board.Board;
import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Rook extends Piece {

    public Rook(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // Rooks move in 4 straight directions: Up, Down, Left, Right
        int[][] directions = { {-1, 0}, {1, 0}, {0, -1}, {0, 1} }; 

        for (int[] dir : directions) {
            int row = position.getRow() + dir[0];
            int col = position.getColumn() + dir[1];

            while (true) {
                Position nextPos = new Position(row, col);
                
                // Stop if we hit the edge of the board
                if (!board.isWithinBounds(nextPos)) break;

                Piece pieceAtNext = board.getPiece(nextPos);
                if (pieceAtNext == null) {
                    moves.add(nextPos); // Square is empty, we can move here
                } else {
                    // We hit a piece. If it's an enemy, we can capture it.
                    if (pieceAtNext.getColor() != this.color) {
                        moves.add(nextPos); 
                    }
                    break; // Can't jump over pieces, so stop sliding in this direction
                }
                
                // Move one more square in the same direction
                row += dir[0];
                col += dir[1];
            }
        }
        return moves;
    }
}