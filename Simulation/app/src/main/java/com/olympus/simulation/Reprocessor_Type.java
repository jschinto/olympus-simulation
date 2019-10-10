package com.olympus.simulation;

public class Reprocessor_Type {
    private int numScopes;
    private int cycleTime;
    private int price;

    public Reprocessor_Type(int numScopes, int cycleTime, int price){
        this.numScopes = numScopes;
        this.cycleTime = cycleTime;
        this.price = price;
    }

    public int getNumScopes() {
        return numScopes;
    }

    public void setNumScopes(int numScopes) {
        this.numScopes = numScopes;
    }

    public int getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(int cycleTime) {
        this.cycleTime = cycleTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
