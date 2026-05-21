package com.redcyrus.common;

public class Queen extends Piece {
    public Queen(boolean isWhite) { super(isWhite); }

    @Override
    public String getSymbol() { return isWhite ? "q" : "Q"; }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);
        
        if (startX != endX && startY != endY && dx != dy) return false;

        int xDir = Integer.compare(endX, startX);
        int yDir = Integer.compare(endY, startY);
        
        int currX = startX + xDir;
        int currY = startY + yDir;
        
        while (currX != endX || currY != endY) {
            if (board[currX][currY] != null) return false; 
            currX += xDir;
            currY += yDir;
        }
        return board[endX][endY] == null || board[endX][endY].isWhite() != this.isWhite;
    }
}