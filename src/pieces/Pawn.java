package pieces;

import board.Position;
import java.util.ArrayList;
import java.util.List;
import utils.Color;

public class Pawn extends Piece {

    // The constructor matches the blueprint and passes the info up using 'super'
    public Pawn(Color color, Position position) {
        super(color, position);
    }

    
    @Override
    public List<Position> possibleMoves() {
        List<Position> moves = new ArrayList<>();
        // TODO: Add Pawn movement logic (forward 1 or 2, diagonal captures)
        return moves;
    }
}