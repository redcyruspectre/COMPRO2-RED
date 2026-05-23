package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Player player1;
    private Player player2;
    private List<Round> rounds;
    private LocalDateTime matchDate;
    private String winner;
    private int maxRounds;
    
    public Match(Player player1, Player player2, int maxRounds) {
        this.player1 = player1;
        this.player2 = player2;
        this.maxRounds = maxRounds;
        this.rounds = new ArrayList<>();
        this.matchDate = LocalDateTime.now();
    }
    
    public Player getPlayer1() {
        return player1;
    }
    
    public Player getPlayer2() {
        return player2;
    }
    
    public void addRound(Round round) {
        rounds.add(round);
    }
    
    public List<Round> getRounds() {
        return rounds;
    }
    
    public int getPlayer1Wins() {
        int count = 0;
        for (Round r : rounds) {
            if (r.getResult().equals("Player 1 Wins")) {
                count++;
            }
        }
        return count;
    }
    
    public int getPlayer2Wins() {
        int count = 0;
        for (Round r : rounds) {
            if (r.getResult().equals("Player 2 Wins")) {
                count++;
            }
        }
        return count;
    }
    
    public int getDrawCount() {
        int count = 0;
        for (Round r : rounds) {
            if (r.getResult().equals("Draw")) {
                count++;
            }
        }
        return count;
    }
    
    public void determineMatchWinner() {
        int p1Wins = getPlayer1Wins();
        int p2Wins = getPlayer2Wins();
        
        if (p1Wins > p2Wins) {
            winner = player1.getName();
            player1.addWin();
            player2.addLoss();
        } else if (p2Wins > p1Wins) {
            winner = player2.getName();
            player2.addWin();
            player1.addLoss();
        } else {
            winner = "Draw";
        }
    }
    
    public String getWinner() {
        return winner;
    }
    
    public LocalDateTime getMatchDate() {
        return matchDate;
    }
    
    public int getMaxRounds() {
        return maxRounds;
    }
    
    @Override
    public String toString() {
        return "Match{" +
                "player1=" + player1.getName() +
                ", player2=" + player2.getName() +
                ", rounds=" + rounds.size() + "/" + maxRounds +
                ", p1Wins=" + getPlayer1Wins() +
                ", p2Wins=" + getPlayer2Wins() +
                ", draws=" + getDrawCount() +
                ", winner='" + winner + '\'' +
                ", date=" + matchDate +
                '}';
    }
}
