package com.olympus.simulation;

public class Reprocessor_Type {

    private String name;
    private int numScopes;
    private int cycleTime;
    private int waitTime;
    private int price;

    public Reprocessor_Type(String name, int numScopes, int cycleTime, int waitTime, int price){
        this.name = name;
        this.numScopes = numScopes;
        this.cycleTime = cycleTime;
        this.waitTime = waitTime;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}