package model;



import java.io.*;
import java.util.*;

public class Leaderboard {
    private static final String LEADERBOARD_FILE = "leaderboard.dat";

    public static void displayLeaderboard() {
        List<Match> matches = loadMatches();

        if (matches.isEmpty()) {
            System.out.println("No match history found.");
            return;
        }

        System.out.println("\n========== LEADERBOARD ==========");
        System.out.println("Match History:\n");

        Map<String, PlayerStats> stats = new HashMap<>();

        for (Match match : matches) {
            String p1Name = match.getPlayer1().getName();
            String p2Name = match.getPlayer2().getName();

            stats.putIfAbsent(p1Name, new PlayerStats(p1Name));
            stats.putIfAbsent(p2Name, new PlayerStats(p2Name));

            PlayerStats p1Stats = stats.get(p1Name);
            PlayerStats p2Stats = stats.get(p2Name);

            int p1Wins = match.getPlayer1Wins();
            int p2Wins = match.getPlayer2Wins();

            p1Stats.addMatch(p1Wins, p2Wins, match.getDrawCount());
            p2Stats.addMatch(p2Wins, p1Wins, match.getDrawCount());

            System.out.println("Match: " + p1Name + " vs " + p2Name);
            System.out.println("  " + p1Name + ": " + p1Wins + " wins");
            System.out.println("  " + p2Name + " : " + p2Wins + " wins");
            System.out.println("  Winner: " + match.getWinner());
            System.out.println("  Date: " + match.getMatchDate());
            System.out.println();
        }

        System.out.println("\n--- Overall Player Statistics ---");
        List<PlayerStats> sortedStats = new ArrayList<>(stats.values());
        sortedStats.sort((a, b) -> Integer.compare(b.getWins(), a.getWins()));

        for (PlayerStats stat : sortedStats) {
            System.out.println(stat);
        }
        System.out.println("==================================\n");
    }

    public static List<Match> loadMatches() {
        List<Match> matches = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(LEADERBOARD_FILE))) {
            while (true) {
                try {
                    Match match = (Match) ois.readObject();
                    matches.add(match);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet
            return matches;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
        }

        return matches;
    }

    // Helper class for player statistics
    static class PlayerStats {
        String name;
        int wins;
        int losses;
        int draws;

        PlayerStats(String name) {
            this.name = name;
            this.wins = 0;
            this.losses = 0;
            this.draws = 0;
        }

        void addMatch(int matchWins, int matchLosses, int matchDraws) {
            if (matchWins > matchLosses) {
                this.wins++;
            } else if (matchLosses > matchWins) {
                this.losses++;
            } else {
                this.draws++;
            }
        }

        int getWins() {
            return wins;
        }

        @Override
        public String toString() {
            return name + ": " + wins + "W - " + losses + "L - " + draws + "D";
        }
    }
}
