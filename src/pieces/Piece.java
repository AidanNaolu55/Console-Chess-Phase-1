package pieces;


import board.Position;
import utils.Color;
import java.util.List;

/**
 * The abstract blueprint for all chess pieces.
 */
public abstract class Piece {
    
    protected Color color;
    protected Position position;

    // The Constructor
    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    // Getters
    public Color getColor() {
        return color;
    }

    public Position getPosition() {
        return position;
    }

    // Setter 
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Every specific piece must create its own version of this method 
     */
    public abstract List<Position> possibleMoves();
}