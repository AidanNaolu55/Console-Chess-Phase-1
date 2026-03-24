package pieces;

import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Bishop extends Piece {

    public Bishop(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        // TODO: Add Bishop movement logic (diagonals)
        return moves;
    }
}