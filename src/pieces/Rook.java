package pieces;

import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Rook extends Piece {

    public Rook(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        // TODO: Add Rook movement logic (straight lines)
        return moves;
    }
}