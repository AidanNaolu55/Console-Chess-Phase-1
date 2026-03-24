package pieces;

import board.Position;
import utils.Color;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        // TODO: Add Queen movement logic (straight lines and diagonals)
        return moves;
    }
}