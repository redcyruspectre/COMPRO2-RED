package com.redcyrus.common;

public class Knight extends Piece {
    public Knight(boolean isWhite) { super(isWhite); }

    @Override
    public String getSymbol() { return isWhite ? "n" : "N"; }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        
        if ((dx == 2 && dy == 1) || (dx == 1 && dy == 2)) {
            return board[endX][endY] == null || board[endX][endY].isWhite() != this.isWhite;
        }
        return false;
    }
}