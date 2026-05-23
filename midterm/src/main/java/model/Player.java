package model;

import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String id;
    private int wins;
    private int losses;
    private int draws;
    
    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getWins() {
        return wins;
    }
    
    public void addWin() {
        this.wins++;
    }
    
    public int getLosses() {
        return losses;
    }
    
    public void addLoss() {
        this.losses++;
    }
    
    public int getDraws() {
        return draws;
    }
    
    public void addDraw() {
        this.draws++;
    }
    
    public int getTotalScore() {
        return wins;
    }
    
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", wins=" + wins +
                ", losses=" + losses +
                ", draws=" + draws +
                '}';
    }
}
