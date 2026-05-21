package com.redcyrus.common;

public abstract class Piece {
    protected boolean isWhite;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract String getSymbol();
    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board);
}