package pieces;

import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Knight extends Piece {

    public Knight(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        // TODO: Add Knight movement logic (L-shapes)
        return moves;
    }
}