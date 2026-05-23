package model;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             PrintWriter outToServer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server.");

            // --- Login Phase ---
            String serverPrompt = inFromServer.readLine();
            if ("ENTER_USERNAME".equals(serverPrompt)) {
                System.out.print("Username: ");
                String username = scanner.nextLine();
                outToServer.println(username);
            }

            serverPrompt = inFromServer.readLine();
            if ("ENTER_PASSWORD".equals(serverPrompt)) {
                System.out.print("Password: ");
                String password = scanner.nextLine();
                outToServer.println(password);
            }

            String authResponse = inFromServer.readLine();
            if ("LOGIN_FAILED".equals(authResponse)) {
                System.out.println("Login Failed: Incorrect username or password.");
                return; // Quit game
            } else if ("LOGIN_SUCCESS".equals(authResponse)) {
                System.out.println("Login Successful!");
            }

            // --- Game Start ---
            String gameStartMessage = inFromServer.readLine();
            if (gameStartMessage == null || !gameStartMessage.startsWith("GAME_START:")) {
                return;
            }
            String opponent = gameStartMessage.substring("GAME_START:".length());
            System.out.println("\n--- Game started! Opponent: " + opponent + " ---");

            // --- Game Loop (Simultaneous Input) ---
            boolean continuePlaying = true;
            int round = 1;
            
            while (continuePlaying) {
                String serverMessage = inFromServer.readLine();

                if (serverMessage == null) {
                    System.out.println("Server disconnected.");
                    break;
                }

                if (serverMessage.equals("YOUR_CHOICE")) {
                    System.out.println("\n--- Round " + round + " ---");
                    System.out.print("Player 2  \n[1]:Rock \n[2]:Paper \n[3]:Scissors \n choose: ");

                    int playerChoice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    
                    outToServer.println(playerChoice);
                    System.out.println("Waiting for " + opponent + "...");
                    round++;

                } else if (serverMessage.startsWith("RESULT:")) {
                    String[] parts = serverMessage.substring("RESULT:".length()).split(",");
                    String roundResult = parts[0];
                    int p1Choice = Integer.parseInt(parts[1]);
                    int p2Choice = Integer.parseInt(parts[2]);

                    System.out.println(opponent + "'s choice: " + choiceToString(p1Choice));
                    System.out.println("Your choice: " + choiceToString(p2Choice));
                    System.out.println("Result: " + roundResult);

                    String nextMessage = inFromServer.readLine();
                    if ("GAME_OVER".equals(nextMessage)) {
                        continuePlaying = false;
                    }

                } else if (serverMessage.startsWith("MATCH_SUMMARY:")) {
                    String winner = serverMessage.substring("MATCH_SUMMARY:".length());
                    System.out.println("\n========== MATCH OVER ==========");
                    System.out.println("Winner: " + winner);
                    System.out.println("================================\n");
                    continuePlaying = false;
                }
            }
            System.out.println("Game session ended.");

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    public static String choiceToString(int choice) {
        switch (choice) {
            case 1: return "Rock";
            case 2: return "Paper";
            case 3: return "Scissors";
            default: return "Invalid";
        }
    }
}

