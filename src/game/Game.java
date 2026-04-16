package game;

import board.Board;
import board.Position;
import java.util.Scanner; // We need this to check piece colors!
import pieces.Piece;
import utils.Color;

public class Game {
    private Board board;
    // Keep track of whose turn it is, starting with White
    private Color currentTurn;

    public Game() {
        board = new Board();
        currentTurn = Color.WHITE;
    }

    public void start() {
        System.out.println("Welcome to Console Chess!");
        System.out.println("=========================");
        board.display();
        play(); 
    }

    private void play() {
        Scanner scanner = new Scanner(System.in); 
        
        while (true) {
            System.out.println();
            // Announce whose turn it is!
            System.out.println(currentTurn + "'s Turn");
            System.out.print("Enter your move (e.g., E2 E4) or type 'quit' to exit: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing!");
                break;
            }

            if (input.length() != 5 || input.charAt(2) != ' ') {
                System.out.println("Error: Invalid format. Please use the format 'E2 E4'.");
                continue; 
            }

            String[] parts = input.split(" ");
            Position from = parsePosition(parts[0]);
            Position to = parsePosition(parts[1]);

            if (from == null || to == null) {
                System.out.println("Error: Invalid coordinates. Use letters A-H and numbers 1-8.");
                continue; 
            }

            // --- NEW LEGALITY CHECKS ---
            
            // 1. Identify the piece the user is trying to move
            Piece pieceToMove = board.getPiece(from);
            
            if (pieceToMove == null) {
                System.out.println("Error: There is no piece at the starting position!");
                continue;
            }

            // 2. Make sure they are moving their own piece
            if (pieceToMove.getColor() != currentTurn) {
                System.out.println("Error: It is " + currentTurn + "'s turn. You cannot move that piece.");
                continue;
            }

            // 3. Make sure they aren't trying to capture their own piece
            Piece destinationPiece = board.getPiece(to);
            if (destinationPiece != null && destinationPiece.getColor() == currentTurn) {
                System.out.println("Error: You cannot capture your own piece!");
                continue;
            }

            // If it passes all checks, move the piece!
            boolean success = board.movePiece(from, to);
            if (success) {
                // Swap the turn to the other player
                if (currentTurn == Color.WHITE) {
                    currentTurn = Color.BLACK;
                } else {
                    currentTurn = Color.WHITE;
                }
                
                System.out.println();
                board.display();
            }
        }
        scanner.close();
    }

    private Position parsePosition(String pos) {
        char colChar = Character.toUpperCase(pos.charAt(0)); 
        char rowChar = pos.charAt(1);

        if (colChar < 'A' || colChar > 'H' || rowChar < '1' || rowChar > '8') {
            return null;
        }

        int col = colChar - 'A';
        int row = 8 - Character.getNumericValue(rowChar);

        return new Position(row, col);
    }

    // Don't forget to import your new class at the top of the file!
    // import gui.ChessGUI;

    public static void main(String[] args) {
        // Create the backend board first
        board.Board myBoard = new board.Board();
        
        // Pass it to the GUI so it can draw the pieces
        new gui.ChessGUI(myBoard);
    }
}