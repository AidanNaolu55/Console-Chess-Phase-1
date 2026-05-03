package pieces;

import board.Board;
import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Bishop extends Piece {

    public Bishop(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // Bishops move in 4 diagonal directions
        int[][] directions = { {-1, -1}, {-1, 1}, {1, -1}, {1, 1} }; 

        for (int[] dir : directions) {
            int row = position.getRow() + dir[0];
            int col = position.getColumn() + dir[1];

            while (true) {
                Position nextPos = new Position(row, col);
                
                if (!board.isWithinBounds(nextPos)) break;

                Piece pieceAtNext = board.getPiece(nextPos);
                if (pieceAtNext == null) {
                    moves.add(nextPos); 
                } else {
                    if (pieceAtNext.getColor() != this.color) {
                        moves.add(nextPos); 
                    }
                    break; // Stop sliding when hitting any piece
                }
                
                row += dir[0];
                col += dir[1];
            }
        }
        return moves;
    }
}