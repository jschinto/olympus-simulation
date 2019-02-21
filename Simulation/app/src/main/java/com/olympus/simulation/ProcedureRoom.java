package com.olympus.simulation;

public class ProcedureRoom {

    //whether the room is currently being used or not
    private boolean occupied;
    //how much time it takes to travel to this room from the client waiting room
    private int travelTime;
    //the time needed after an operation for the room to be available again
    private int cooldownTime;

    //cooldown time decreases with each tick of time
    public void tick() {
        cooldownTime--;
    }

    public boolean isAvailable() {
        return !isOccupied() && cooldownTime == 0;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    public int getCooldownTime() {
        return cooldownTime;
    }

    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }


}
