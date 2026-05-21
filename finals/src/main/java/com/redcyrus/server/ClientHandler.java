package com.redcyrus.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final boolean isWhite;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, boolean isWhite) {
        this.socket = socket;
        this.isWhite = isWhite;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("[THREAD INITIALIZATION ERROR]: " + e.getMessage());
        }
    }

    public void sendMessage(String msg) {
        if (out != null) {
            out.print(msg + (msg.endsWith("\n") ? "" : "\n"));
            out.flush();
        }
    }

    @Override
    public void run() {
        try {
            String clientInput;
            while ((clientInput = in.readLine()) != null) {
                // Route stream requests directly inside server synchronization engine
                ChessServer.handlePlayerMove(clientInput, isWhite);
            }
        } catch (IOException e) {
            System.out.println("[CONNECTION TERMINATED]: A player abruptly dropped connection.");
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (!socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket resources: " + e.getMessage());
        }
    }
}