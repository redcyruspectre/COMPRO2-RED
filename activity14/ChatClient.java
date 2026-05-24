package com.redcyrus;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static final int WIDTH = 55;

    private static final String RESET = "\u001B[0m";
    private static final String GRAY = "\u001B[90m";
    private static final String BLUE = "\u001B[34m";
    private static final String WHITE = "\u001B[37m";

    private static String lastSender = "";
    private static String lastTime = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String myUsername = scanner.nextLine();

        try (Socket socket = new Socket("localhost", ChatServer.getPort())) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // sending data
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // reading data

            // Register username with server
            out.println(myUsername);

            // process incoming messages from the server
            Thread listener = new Thread(() -> {
                try {
                    String sender;
                    while ((sender = in.readLine()) != null) {
                        String content = in.readLine();
                        String time = in.readLine();

                        System.out.print("\r" + " ".repeat(WIDTH) + "\r");
                        displayMessengerStyle(new Message(sender, content, time), myUsername);
                        System.out.print("> ");
                        System.out.flush();
                    }
                } catch (Exception e) {
                    System.out.println("\nDisconnected.");
                }
            });
            listener.setDaemon(true);
            listener.start();

            while (true) {
                System.out.print("> ");
                System.out.flush();
                String text = scanner.nextLine();

                if (text.equalsIgnoreCase("bye")) break; // Exit condition

                Message myMsg = new Message(myUsername, text);
                displayMessengerStyle(myMsg, myUsername);

                out.println(myMsg.getSender());
                out.println(myMsg.getContent());
                out.println(myMsg.getTimestamp());
            }
        } catch (IOException e) {
            System.err.println("Server unreachable.");
        }
    }

    private static void displayMessengerStyle(Message msg, String myUsername) {
        boolean isMe = msg.getSender().equalsIgnoreCase(myUsername);

        // Display timestamp header if it changes
        if (!msg.getTimestamp().equals(lastTime)) {
            printCenter(GRAY + "--- " + msg.getTimestamp() + " ---" + RESET);
            lastTime = msg.getTimestamp();
        }

        if (msg.getSender().equals("SERVER")) {
            printCenter(GRAY + "(" + msg.getContent() + ")" + RESET);
        } else if (isMe) {
            // My messages appear in blue
            printRight(BLUE + "┃ " + msg.getContent() + RESET);
        } else {
            // Other users appear in white
            if (!msg.getSender().equals(lastSender)) {
                System.out.println(WHITE + msg.getSender() + RESET);
            }
            System.out.println(WHITE + "┃ " + msg.getContent() + RESET);
        }
        lastSender = msg.getSender();
    }

    private static void printRight(String text) {
        String plain = stripColors(text);
        int padding = Math.max(0, WIDTH - plain.length());
        System.out.print("\r" + " ".repeat(padding) + text + "\n");
    }

    private static void printCenter(String text) {
        String plain = stripColors(text);
        int padding = Math.max(0, (WIDTH / 2) - (plain.length() / 2));
        System.out.print("\r" + " ".repeat(padding) + text + "\n");
    }

    // Removes ANSI codes to get true text length for alignment
    private static String stripColors(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}