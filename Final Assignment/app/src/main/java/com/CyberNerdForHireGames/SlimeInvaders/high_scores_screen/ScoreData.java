package com.CyberNerdForHireGames.SlimeInvaders.high_scores_screen;


public class ScoreData {
    private String rank;
    private String highscoreUsername;
    private String score;

    public ScoreData(){

    }

    public ScoreData(String r, String username, String s){
        this.rank = r;
        this.highscoreUsername = username;
        this.score = s;
    }

    public String getRank() {
        return rank;
    }

    public String getHighscoreUsername() {
        return highscoreUsername;
    }

    public String getScore() {
        return score;
    }


}
