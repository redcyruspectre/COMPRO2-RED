package project;

public class Board {
    private Piece[][] grid;

    public Board() {
        grid = new Piece[8][8];
        setupBoard();
    }

    private void setupBoard() {
       
        grid[0][1] = new Knight(0, 1, false); // Black Knight
        grid[7][1] = new Knight(7, 1, true);  // White Knight
        // ... add other pieces
    }

    public boolean executeMove(int startR, int startC, int endR, int endC) throws InvalidMoveException {
        Piece p = grid[startR][startC];
        
        if (p == null) throw new InvalidMoveException("No piece at start position.");
        
        if (p.isValidMove(endR, endC, grid)) {
            grid[endR][endC] = p;
            grid[startR][startC] = null;
            return true;
        }
        return false;
    }
}