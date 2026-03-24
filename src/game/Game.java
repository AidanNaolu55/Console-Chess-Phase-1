package game;

import board.Board;
import board.Position;
import java.util.Scanner;

/**
 * The main engine of the chess game.
 */
public class Game {
    private Board board;

    public Game() {
        board = new Board();
    }

    public void start() {
        System.out.println("Welcome to Console Chess!");
        System.out.println("=========================");
        board.display();
        play(); // Start the interactive loop!
    }

    /**
     * The main loop that asks for moves and updates the board.
     */
    private void play() {
        // Scanner listens to your keyboard
        Scanner scanner = new Scanner(System.in); 
        
        // 'while(true)' means this loop runs forever until we tell it to 'break'
        while (true) {
            System.out.println();
            System.out.print("Enter your move (e.g., E2 E4) or type 'quit' to exit: ");
            String input = scanner.nextLine().trim(); // Read what the user typed

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing!");
                break; // Stop the loop
            }

            // Basic validation: Make sure they typed exactly 5 characters with a space in the middle
            if (input.length() != 5 || input.charAt(2) != ' ') {
                System.out.println("Error: Invalid format. Please use the format 'E2 E4'.");
                continue; // Skip the rest of this loop and ask again
            }

            // Split the text into two parts: "E2" and "E4"
            String[] parts = input.split(" ");
            Position from = parsePosition(parts[0]);
            Position to = parsePosition(parts[1]);

            // If the translator failed, ask again
            if (from == null || to == null) {
                System.out.println("Error: Invalid coordinates. Use letters A-H and numbers 1-8.");
                continue; 
            }

            // Tell the board to move the piece. If successful, print the new board!
            boolean success = board.movePiece(from, to);
            if (success) {
                System.out.println();
                board.display();
            }
        }
        scanner.close(); // Clean up the scanner when we are done
    }

    /**
     * Translates human text like "E2" into array coordinates like row 6, col 4.
     */
    private Position parsePosition(String pos) {
        // Make the letter uppercase just in case they typed "e2"
        char colChar = Character.toUpperCase(pos.charAt(0)); 
        char rowChar = pos.charAt(1);

        // Make sure they didn't type something crazy like "Z9"
        if (colChar < 'A' || colChar > 'H' || rowChar < '1' || rowChar > '8') {
            return null;
        }

        // Math trick: Subtracting 'A' from 'A' gets 0. Subtracting 'A' from 'E' gets 4.
        int col = colChar - 'A';
        // Math trick: The board is upside down in our array (row 8 is array index 0)
        int row = 8 - Character.getNumericValue(rowChar);

        return new Position(row, col);
    }

    public static void main(String[] args) {
        Game chessGame = new Game();
        chessGame.start();
    }
}