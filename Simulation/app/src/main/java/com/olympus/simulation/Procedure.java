package com.olympus.simulation;

public class Procedure {

    //name given to the operation
    private String name;
    //the minimum and maximum time the operation will last, to account for variance in completion time
    private int minTime;
    private int maxTime;
    //needs? TODO: need a way to store and check a procedure's needs

    public Procedure(String name, int minTime, int maxTime) {
        this.name = name;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }





}