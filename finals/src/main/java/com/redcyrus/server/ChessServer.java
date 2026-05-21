package com.redcyrus.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.redcyrus.common.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChessServer {
    private static final int PORT = 8080;
    private static final String HISTORY_FILE = "match_history.json";
     
    private static Piece[][] board = new Piece[8][8];
    private static boolean isWhiteTurn = true;
    private static final List<String> moveHistory = new ArrayList<>();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static ClientHandler whitePlayer;
    private static ClientHandler blackPlayer;

    public static void main(String[] args) {
        initializeBoard();
        System.out.println("[SERVER] GrandmasterSync Engine Booted. Awaiting Sockets...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            // Player 1 Connection
            Socket s1 = serverSocket.accept();
            whitePlayer = new ClientHandler(s1, true);
            System.out.println("[SERVER] White Player connected from " + s1.getRemoteSocketAddress());
            whitePlayer.sendMessage("WELCOME White");

            // Player 2 Connection
            Socket s2 = serverSocket.accept();
            blackPlayer = new ClientHandler(s2, false);
            System.out.println("[SERVER] Black Player connected from " + s2.getRemoteSocketAddress());
            blackPlayer.sendMessage("WELCOME Black");

            // Fire up individual client execution threads
            new Thread(whitePlayer).start();
            new Thread(blackPlayer).start();

            // Broadcast baseline state
            broadcastBoard();
            updateTurnPrompts();

        } catch (IOException e) {
            System.err.println("[SERVER SYSTEM ERROR]: " + e.getMessage());
        }
    }

    private static void initializeBoard() {
        // This spawn black pieces on Top of the board in there own board
        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][3] = new Queen(false);
        board[0][4] = new King(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);
        
        // Spawn 8 Black Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(false);
        }

        // This spawn the white pieces on bottom of the board
        // same with black pawns, spawn 8 white pawns in the row
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(true);
        }

        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][3] = new Queen(true);
        board[7][4] = new King(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);
    }

    // Thread-safe game loop validation state modifier with Game Over Logic
    public static synchronized void handlePlayerMove(String rawMove, boolean isWhitePlayer) {
        if (isWhitePlayer != isWhiteTurn) {
            getClient(isWhitePlayer).sendMessage("ERROR Not your turn!");
            updateTurnPrompts();
            return;
        }

        try {
            String[] parts = rawMove.split(" ");
            if (parts.length != 2) throw new InvalidMoveException("Format required: 'e2 e4'");

            int startY = parts[0].charAt(0) - 'a';
            int startX = 8 - Character.getNumericValue(parts[0].charAt(1));
            int endY = parts[1].charAt(0) - 'a';
            int endX = 8 - Character.getNumericValue(parts[1].charAt(1));

            if (startX < 0 || startX > 7 || startY < 0 || startY > 7 || endX < 0 || endX > 7 || endY < 0 || endY > 7) {
                throw new InvalidMoveException("It is invalid move because the player is trying to target a illegal move completely outside of the board.");
            }

            Piece p = board[startX][startY];
            if (p == null || p.isWhite() != isWhitePlayer) {
                throw new InvalidMoveException("Target selection slot is empty or contains opposing unit!");
            }

            if (!p.isValidMove(startX, startY, endX, endY, board)) {
                throw new InvalidMoveException("Invalid algebraic trajectory for this piece subclass design pattern!");
            }

            // CHECK IF THE KING IS ABOUT TO BE CAPTURED
            Piece targetPiece = board[endX][endY];
            boolean isGameOver = (targetPiece instanceof King);

            // Apply Move
            board[endX][endY] = p;
            board[startX][startY] = null;
            
            // Append Move details to memory history system
            moveHistory.add((isWhitePlayer ? "White: " : "Black: ") + rawMove + (isGameOver ? " (KING CAPTURED)" : ""));
            saveMatchHistory();

            // END THE GAME IF KING IS DEAD
            if (isGameOver) {
                broadcastBoard();
                String winner = isWhitePlayer ? "WHITE" : "BLACK";
                
                // NEW FIX: Add "ERROR " to the start so the client UI reads it!
                String gameOverMsg = "ERROR *** GAME OVER! " + winner + " WINS BY CAPTURING THE KING! ***";
                
                whitePlayer.sendMessage(gameOverMsg);
                blackPlayer.sendMessage(gameOverMsg);
                System.out.println("[SERVER] Match concluded. " + winner + " wins.");
                
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {}
                
                System.exit(0); // Safely shuts down the server
            }

            // Switch Turns
            isWhiteTurn = !isWhiteTurn;
            broadcastBoard();
            updateTurnPrompts();

        } catch (Exception e) {
            getClient(isWhitePlayer).sendMessage("ERROR " + e.getMessage());
            getClient(isWhitePlayer).sendMessage("YOUR_TURN");
        }
    }

    public static void broadcastBoard() {
        StringBuilder sb = new StringBuilder("BOARD\n");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sb.append(board[i][j] == null ? "." : board[i][j].getSymbol()).append(" ");
            }
            sb.append("\n");
        }
        String data = sb.toString();
        if (whitePlayer != null) whitePlayer.sendMessage(data);
        if (blackPlayer != null) blackPlayer.sendMessage(data);
    }

    public static void updateTurnPrompts() {
        if (isWhiteTurn) {
            whitePlayer.sendMessage("YOUR_TURN");
            blackPlayer.sendMessage("WAIT");
        } else {
            blackPlayer.sendMessage("YOUR_TURN");
            whitePlayer.sendMessage("WAIT");
        }
    }

    private static ClientHandler getClient(boolean isWhite) {
        return isWhite ? whitePlayer : blackPlayer;
    }

    private static void saveMatchHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            gson.toJson(moveHistory, writer);
        } catch (IOException e) {
            System.err.println("[PERSISTENCE CRITICAL ERROR]: Failed to serialize match data! " + e.getMessage());
        }
    }
}