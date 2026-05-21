package com.redcyrus.common;



public class Pawn extends Piece {
    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public String getSymbol() {
        return isWhite ? "p" : "P";
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int direction = isWhite ? -1 : 1;
        int startRow = isWhite ? 6 : 1;

        // Move forward 1 square
        if (startY == endY && endX == startX + direction && board[endX][endY] == null) {
            return true;
        }
        // Move forward 2 squares from initial position
        if (startY == endY && startX == startRow && endX == startX + (2 * direction) 
                && board[startX + direction][startY] == null && board[endX][endY] == null) {
            return true;
        }
        // Diagonal Capture
        if (Math.abs(startY - endY) == 1 && endX == startX + direction && board[endX][endY] != null) {
            return board[endX][endY].isWhite() != this.isWhite;
        }

        return false;
    }
}