package pieces;

import board.Board;
import board.Position;
import java.io.Serializable;
import java.util.List;
import utils.Color;

public abstract class Piece implements Serializable {
    protected Color color;
    protected Position position;

    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Every specific piece (Pawn, Knight, etc.) must implement this to define how it moves.
     * We pass the Board so the piece knows where other pieces are (to block paths or capture).
     */
    public abstract List<Position> possibleMoves(Board board);
}