package pieces;

import board.Board;
import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Knight extends Piece {

    public Knight(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Board board) {
        List<Position> moves = new ArrayList<>();
        // All 8 possible "L" shape jumps
        int[][] jumps = {
            {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2},
            {1, -2}, {1, 2}, {2, -1}, {2, 1}
        };

        for (int[] jump : jumps) {
            int row = position.getRow() + jump[0];
            int col = position.getColumn() + jump[1];
            Position nextPos = new Position(row, col);

            if (board.isWithinBounds(nextPos)) {
                Piece pieceAtNext = board.getPiece(nextPos);
                // Can move if square is empty or has an enemy
                if (pieceAtNext == null || pieceAtNext.getColor() != this.color) {
                    moves.add(nextPos);
                }
            }
        }
        return moves;
    }
}