package model;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Server {
    private static final int PORT = 8000;
    private static final int MAX_ROUNDS = 10;
    private static final String ACCOUNTS_FILE = "name.csv";
    private static final String LEADERBOARD_FILE = "leaderboard.csv";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // --- Player 1 (Host) Login ---
        System.out.println("=== HOST LOGIN ===");
        String player1Name = "";
        while (true) {
            System.out.print("Username: ");
            player1Name = scanner.nextLine();
            System.out.print("Password: ");
            String player1Pass = scanner.nextLine();

            if (authenticate(player1Name, player1Pass)) {
                System.out.println("Login Successful! Welcome, " + player1Name + ".");
                break;
            } else {
                System.out.println("Invalid username or password. Please try again.\n");
            }
        }
        Player player1 = new Player("P1", player1Name);

        // --- Start Server ---
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("\nRPS Server started on port " + PORT + ". Waiting for Player 2 to connect...");

            try (Socket player2Socket = serverSocket.accept()) {
                PrintWriter outToP2 = new PrintWriter(player2Socket.getOutputStream(), true);
                BufferedReader inFromP2 = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));

                // --- Player 2 (Client) Login ---
                outToP2.println("ENTER_USERNAME");
                String player2Name = inFromP2.readLine();
                
                outToP2.println("ENTER_PASSWORD");
                String player2Pass = inFromP2.readLine();

                if (!authenticate(player2Name, player2Pass)) {
                    outToP2.println("LOGIN_FAILED");
                    System.out.println("Connection dropped: Player 2 failed to login.");
                    return; // End the connection
                }
                
                outToP2.println("LOGIN_SUCCESS");
                System.out.println("Player 2 (" + player2Name + ") connected and authenticated successfully!");
                Player player2 = new Player("P2", player2Name);

                // --- Game Setup ---
                Match match = new Match(player1, player2, MAX_ROUNDS);
                System.out.println("\n--- Game Start: " + player1Name + " vs " + player2Name + " ---");
                outToP2.println("GAME_START:" + player1Name);

                // --- Game Loop (Simultaneous Input) ---
                for (int round = 1; round <= MAX_ROUNDS; round++) {
                    System.out.println("\n--- Round " + round + " ---");

                    // 1. Tell Player 2 to start typing
                    outToP2.println("YOUR_CHOICE"); 

                    // 2. Start listening for Player 2's answer in the BACKGROUND
                    CompletableFuture<Integer> p2Future = CompletableFuture.supplyAsync(() -> {
                        try {
                            String response = inFromP2.readLine();
                            return response != null ? Integer.parseInt(response) : -1;
                        } catch (Exception e) {
                            return -1;
                        }
                    });

                    // 3. Ask Player 1 for their answer at the exact same time
                    System.out.print("Player 1  \n[1]:Rock \n[2]:Paper \n[3]:Scissors \n choose:  ");
                    int player1Choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    // 4. Wait for Player 2 if they haven't finished typing yet
                    int player2Choice = -1;
                    try {
                        if (!p2Future.isDone()) {
                            System.out.println("Waiting for " + player2Name + " to choose...");
                        }
                        player2Choice = p2Future.get();
                    } catch (Exception e) {
                        System.out.println("Error reading Player 2's choice.");
                    }

                    if (player2Choice == -1) {
                        System.out.println("Player 2 disconnected.");
                        break;
                    }

                    // --- Result Calculation ---
                    String result = determineWinner(player1Choice, player2Choice);
                    Round roundData = new Round(round, player1Choice, player2Choice, result);
                    match.addRound(roundData);

                    System.out.println("Your choice: " + choiceToString(player1Choice));
                    System.out.println(player2Name + "'s choice: " + choiceToString(player2Choice));
                    System.out.println("Round result: " + result);

                    outToP2.println("RESULT:" + result + "," + player1Choice + "," + player2Choice);

                    if (round < MAX_ROUNDS) {
                        outToP2.println("NEXT_ROUND");
                    } else {
                        outToP2.println("GAME_OVER");
                    }
                }

                match.determineMatchWinner();
                displayMatchSummary(match);
                saveLeaderboard(match);
                Leaderboard.displayLeaderboard();

                outToP2.println("MATCH_SUMMARY:" + match.getWinner());
                scanner.close();

            } catch (IOException e) {
                System.out.println("Error with Player 2 connection: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    // --- Authentication Method ---
    public static boolean authenticate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Check if the row has at least a user and password, and if they match
                if (parts.length >= 2) {
                    if (parts[0].equals(username) && parts[1].equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading " + ACCOUNTS_FILE + ": " + e.getMessage());
        }
        return false;
    }

    public static String determineWinner(int p1Choice, int p2Choice) {
        if (p1Choice == p2Choice) return "Draw";
        else if ((p1Choice == 1 && p2Choice == 3) || (p1Choice == 2 && p2Choice == 1) || (p1Choice == 3 && p2Choice == 2)) {
            return "Player 1 Wins";
        } else {
            return "Player 2 Wins";
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

    public static void displayMatchSummary(Match match) {
        System.out.println("\n========== MATCH SUMMARY ==========");
        System.out.println("Player 1 (" + match.getPlayer1().getName() + "): " + match.getPlayer1Wins() + " wins");
        System.out.println("Player 2 (" + match.getPlayer2().getName() + "): " + match.getPlayer2Wins() + " wins");
        System.out.println("Draws: " + match.getDrawCount());
        System.out.println("MATCH WINNER: " + match.getWinner());
        System.out.println("===================================\n");
    }

    public static void saveLeaderboard(Match match) {
        boolean fileExists = new File(LEADERBOARD_FILE).exists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(LEADERBOARD_FILE, true))) {
            if (!fileExists) writer.println("Player1Name,Player2Name,P1Wins,P2Wins,Draws,Winner,Date");
            String csvLine = String.format("%s,%s,%d,%d,%d,%s,%s",
                    match.getPlayer1().getName(), match.getPlayer2().getName(),
                    match.getPlayer1Wins(), match.getPlayer2Wins(),
                    match.getDrawCount(), match.getWinner(), match.getMatchDate().toString());
            writer.println(csvLine);
        } catch (IOException e) {
            System.err.println("Error saving match: " + e.getMessage());
        }
    }
}
