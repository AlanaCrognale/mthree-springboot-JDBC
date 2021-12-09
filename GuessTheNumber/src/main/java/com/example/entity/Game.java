package com.example.entity;

public class Game {

    private int gameId;
    private int answer;
    private String status;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if (!(obj instanceof Game)) return false;
        Game g = (Game) obj;
        return (g.gameId == this.gameId && g.answer == this.answer && g.status.equals(this.status));
    }

    @Override
    public int hashCode() {
        return this.gameId;
    }
}
