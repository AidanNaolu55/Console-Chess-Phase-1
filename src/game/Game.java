package game;

import board.Board;
import java.io.Serializable;
import utils.Color;

public class Game implements Serializable {
    private Board board;
    private Color currentTurn;

    public Game() {
        this.board = new Board();
        this.currentTurn = Color.WHITE; // White always moves first in chess
    }

    public Board getBoard() {
        return board;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Switches the turn to the other player.
     */
    public void switchTurn() {
        if (currentTurn == Color.WHITE) {
            currentTurn = Color.BLACK;
        } else {
            currentTurn = Color.WHITE;
        }
    }

    /**
     * Main method to launch the Phase 3 GUI version of the game.
     */
    public static void main(String[] args) {
        // We updated ChessGUI to initialize the Game object internally,
        // so all we need to do is fire up the GUI!
        new gui.ChessGUI();
    }
}