public class Knight extends Piece {
    public Knight(int row, int col, boolean isWhite) {
        super(row, col, isWhite);
    }

    @Override
    public boolean isValidMove(int targetRow, int targetCol, Piece[][] board) {
        int rowDiff = Math.abs(this.row - targetRow);
        int colDiff = Math.abs(this.col - targetCol);
        
        // L-shape: (2x1) or (1x2)
        boolean isLShape = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
        
        if (!isLShape) return false;

        // Destination must be empty or contain an enemy
        Piece target = board[targetRow][targetCol];
        return target == null || target.isWhite != this.isWhite;
    }

    @Override
    public String toString() {
        return getColorPrefix() + "N";
    }
}