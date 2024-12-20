package org.example;

public class Rating {

    private final String username;
    private final int score;
    private final String comments;

    public Rating(String username, int score, String comments) {
        this.username = username;
        this.score = score;
        this.comments = comments;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

}
