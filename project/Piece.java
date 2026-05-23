public abstract class Piece {
    protected int row, col;
    protected final boolean isWhite;

    public Piece(int row, int col, boolean isWhite) {
        this.row = row;
        this.col = col;
        this.isWhite = isWhite;
    }

    // Every piece must define how it moves
    public abstract boolean isValidMove(int targetRow, int targetCol, Piece[][] board);

    public String getColorPrefix() {
        return isWhite ? "W" : "B";
    }
}