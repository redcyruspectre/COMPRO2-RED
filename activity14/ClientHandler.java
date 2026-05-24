package com.redcyrus;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ChatServer server;
    private String clientName;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, ChatServer server, String clientName) {
        this.socket = socket;
        this.server = server;
        this.clientName = clientName;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Identify user and announce join
            String nameInput = in.readLine();
            if (nameInput != null) this.clientName = nameInput;
            server.broadcast(new Message("SERVER", clientName + " joined the group."), null);

            // Forward incoming data to all clients
            String sender;
            while ((sender = in.readLine()) != null) {
                String content = in.readLine();
                String time = in.readLine();
                server.broadcast(new Message(sender, content, time), this);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            System.out.println(clientName + " exited.");
            // Cleanup on disconnect
            server.removeClient(this);
            server.broadcast(new Message("SERVER", clientName + " left the group."), null);
            cleanup();
        }
    }

    // Sends formatted message components to the connected client
    public void sendMessage(Message msg) {
        out.println(msg.getSender());
        out.println(msg.getContent());
        out.println(msg.getTimestamp());
    }

    private void cleanup() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
        }
    }
}