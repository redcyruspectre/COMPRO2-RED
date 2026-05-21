package com.redcyrus.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
 
public class ChessClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8080;
 
    public static void main(String[] args) {
        System.out.println("Connecting to network chess ecosystem server local node...");
        
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.nanoTime() > 0 ? System.in : InputStream.nullInputStream())) {

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("WELCOME")) {
                    System.out.println("\n=== GrandmasterSync Terminal ===");
                    System.out.println("Assigned Alliance Color: " + message.split(" ")[1].toUpperCase());
                } else if (message.equals("BOARD")) {
                    // Refresh terminal board structure layout directly inside console UI matrix 
                    System.out.print("\033[H\033[2J"); // ANSI escape sequence trick to clean console screen
                    System.out.flush();
                    
                    System.out.println("\n    a b c d e f g h");
                    System.out.println("  +-----------------+");
                    for (int i = 0; i < 8; i++) {
                        System.out.println((8 - i) + " | " + in.readLine() + "| " + (8 - i));
                    }
                    System.out.println("  +-----------------+");
                    System.out.println("    a b c d e f g h\n");
                } else if (message.equals("YOUR_TURN")) {
                    System.out.print("ENTER COORDINATE COMMAND MOVE (e.g. 'a2 a4'): ");
                    String userMove = scanner.nextLine();
                    out.println(userMove);
                } else if (message.equals("WAIT")) {
                    System.out.println("Status: Opponent is processing move data updates...");
                } else if (message.startsWith("ERROR")) {
                    System.out.println("⚠️ SYSTEM EXCEPTION REJECTED: " + message.substring(6));
                }
            }
        } catch (IOException e) {
            System.err.println("Fatal Connection Breakdown Error: " + e.getMessage());
        }
    }
}