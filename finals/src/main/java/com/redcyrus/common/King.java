package com.redcyrus.common;

public class King extends Piece {
    public King(boolean isWhite) { super(isWhite); }

    @Override
    public String getSymbol() { return isWhite ? "k" : "K"; }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        
        if (dx <= 1 && dy <= 1 && (dx != 0 || dy != 0)) {
            return board[endX][endY] == null || board[endX][endY].isWhite() != this.isWhite;
        }
        return false;
    }
}