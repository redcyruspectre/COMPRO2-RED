package com.redcyrus.common;

public class Bishop extends Piece {
    public Bishop(boolean isWhite) { super(isWhite); }

    @Override
    public String getSymbol() { return isWhite ? "b" : "B"; }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        if (Math.abs(startX - endX) != Math.abs(startY - endY)) return false; 
        if (startX == endX && startY == endY) return false;

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