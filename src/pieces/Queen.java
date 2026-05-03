package pieces;

import board.Board;
import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Queen extends Piece {

    public Queen(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // Queens move in all 8 directions
        int[][] directions = { 
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},   // Rook directions
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}  // Bishop directions
        }; 

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
                    break; 
                }
                
                row += dir[0];
                col += dir[1];
            }
        }
        return moves;
    }
}