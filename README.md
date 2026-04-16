# Console-Based Chess Game (Phase 1)

## Project Overview
This is a console-based chess game written in Java. Phase 1 implements the foundational Object-Oriented backend, including the board representation, piece inheritance, and an interactive command-line interface.

## How to Run the Game
1. Ensure you have the **Java Development Kit (JDK)** installed.
2. Open the project in **VS Code** with the *Extension Pack for Java* installed.
3. Navigate to `src/game/Game.java`.
4. Click the **Play / Run** button in the top right corner.
5. Alternatively, compile and run via terminal from the `src` directory:
   - `javac board/*.java pieces/*.java utils/*.java game/*.java`
   - `java game.Game`

## How to Play
- The game will prompt the current player (White or Black) for a move.
- Enter moves using standard coordinate notation: `[FROM] [TO]`.
- **Example:** `E2 E4`
- Type `quit` at any time to exit the game.

## Feature Summary
### Implemented Features
- **Board Display:** 8x8 grid rendering with proper standard coordinates (A-H, 1-8).
- **OOP Architecture:** Abstract `Piece` class with specific subclasses (Pawn, Rook, Knight, etc.).
- **Game Loop:** Interactive turn-based loop using `Scanner`.
- **Basic Legality Checks:** - Prevents moving empty squares.
  - Prevents moving the opponent's pieces.
  - Prevents capturing your own pieces (friendly fire).
  - Out-of-bounds input protection.

### Not Yet Implemented (Planned for Future Phases)
- Specific piece movement rules (e.g., Knight's L-shape, Bishop's diagonals).
- Advanced rules (Castling, En Passant, Pawn Promotion).
- Check and Checkmate detection.