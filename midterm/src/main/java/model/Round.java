package model;

import java.io.Serializable;

public class Round implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int roundNumber;
    private int player1Choice;
    private int player2Choice;
    private String result; // "Player 1 Wins", "Player 2 Wins", or "Draw"
    
    public Round(int roundNumber, int player1Choice, int player2Choice, String result) {
        this.roundNumber = roundNumber;
        this.player1Choice = player1Choice;
        this.player2Choice = player2Choice;
        this.result = result;
    }
    
    public int getRoundNumber() {
        return roundNumber;
    }
    
    public int getPlayer1Choice() {
        return player1Choice;
    }
    
    public int getPlayer2Choice() {
        return player2Choice;
    }
    
    public String getResult() {
        return result;
    }
    
    public String getChoiceString(int choice) {
        switch (choice) {
            case 1:
                return "Rock";
            case 2:
                return "Paper";
            case 3:
                return "Scissors";
            default:
                return "Invalid";
        }
    }
    
    @Override
    public String toString() {
        return "Round " + roundNumber + ": " + 
               getChoiceString(player1Choice) + " vs " + 
               getChoiceString(player2Choice) + " -> " + result;
    }
}
