package pieces;

import board.Position;
import utils.Color;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        // TODO: Add King movement logic (one square any direction)
        return moves;
    }
}