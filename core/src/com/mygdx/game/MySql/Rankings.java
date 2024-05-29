package com.mygdx.game.MySql;

public class Rankings {
    private String username;
    private int score;
    private int rank;

    public Rankings(String username, int score, int rank) {
        this.username = username;
        this.score = score;
        this.rank = rank;
    }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for score
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // Getter and Setter for rank
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
