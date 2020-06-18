package com.petermarshall;

public class BetPlaced {
    private double oddsOffered;
    private double stake;
    private boolean betSuccessful;
    private double balance;

    public BetPlaced(double oddsOffered, double stake, boolean betSuccessful, double balance) {
        this.oddsOffered = oddsOffered;
        this.stake = stake;
        this.betSuccessful = betSuccessful;
        this.balance = balance;
    }

    public double getOddsOffered() {
        return oddsOffered;
    }

    public double getStake() {
        return stake;
    }

    public boolean isBetSuccessful() {
        return betSuccessful;
    }

    public double getBalance() {
        return balance;
    }

    void setOddsOffered(double oddsOffered) {
        this.oddsOffered = oddsOffered;
    }

    void setBetSuccessful() {
        this.betSuccessful = true;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }
}
