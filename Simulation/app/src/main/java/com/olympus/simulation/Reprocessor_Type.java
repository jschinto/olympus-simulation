package com.olympus.simulation;


public class Reprocessor_Type extends Element {
    private int numScopes;
    private int cycleTime;
    private int waitTime;
    private int price;
    private String name;
    private int startupDelay;

    public Reprocessor_Type(String name, int numScopes, int cycleTime, int waitTime, int price) {
        this.element = ELEMENT_REPROCESSORTYPE;
        this.name = name;
        this.numScopes = numScopes;
        this.cycleTime = cycleTime;
        this.waitTime = waitTime;
        this.price = price;
        this.startupDelay = 3;
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

    public boolean validate() {
        return name!=null && !name.equals("") && numScopes > 0 && cycleTime > 0 && waitTime > 0 && price > 0;
    }

    public int getStartupDelay() {
        return startupDelay;
    }

    public void setStartupDelay(int startupDelay) {
        this.startupDelay = startupDelay;
    }
}