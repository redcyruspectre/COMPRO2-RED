package com.redcyrus;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatServer {
    private static final int PORT = 8080;
    private final List<ClientHandler> clients = new ArrayList<>();
    private final AtomicInteger userCounter = new AtomicInteger(1);

    public static int getPort() {
        return PORT;
    }

    public static void main(String[] args) {
        new ChatServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("--- Gossip Room Server Started ---");

            while (true) {
                Socket socket = serverSocket.accept(); // Accept new connection
                String defaultName = "User" + userCounter.getAndIncrement();

                ClientHandler handler = new ClientHandler(socket, this, defaultName);
                synchronized (clients) {
                    clients.add(handler);
                }

                new Thread(handler).start(); // Launch handler thread
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sends message to all active clients except the original sender
    public void broadcast(Message msg, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(msg);
                }
            }
        }
    }

    public void removeClient(ClientHandler handler) {
        synchronized (clients) {
            clients.remove(handler);

            // Automatically close if everyone leaves
            if (clients.isEmpty()) {
                System.out.println("No active users. Shutting down server...");
                System.exit(0);
            }
        }
    }
}