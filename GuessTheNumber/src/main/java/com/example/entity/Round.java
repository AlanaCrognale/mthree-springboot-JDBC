package com.example.entity;

import java.time.LocalDateTime;

public class Round {
    private int roundId;
    private Game game;
    private int guess;
    private LocalDateTime time;
    private String result;

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getGuess() {
        return guess;
    }

    public void setGuess(int guess) {
        this.guess = guess;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object obj) {

        if(this == obj) return true;
        if (!(obj instanceof Round)) return false;

        Round r = (Round) obj;
        return (r.roundId == this.roundId &&
                r.game.equals(this.game) &&
                r.guess == this.guess &&
                r.time.equals(this.time) &&
                r.result.equals(this.result));
    }

    @Override
    public int hashCode() {
        return this.roundId;
    }

}
