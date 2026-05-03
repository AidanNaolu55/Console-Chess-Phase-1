package pieces;

import board.Board;
import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class King extends Piece {

    public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        int[][] directions = { 
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        }; 

        for (int[] dir : directions) {
            int row = position.getRow() + dir[0];
            int col = position.getColumn() + dir[1];
            Position nextPos = new Position(row, col);

            if (board.isWithinBounds(nextPos)) {
                Piece pieceAtNext = board.getPiece(nextPos);
                if (pieceAtNext == null || pieceAtNext.getColor() != this.color) {
                    moves.add(nextPos);
                }
            }
        }
        return moves;
    }
}