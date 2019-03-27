package com.olympus.simulation;

import java.io.Serializable;

public class ProcedureRoom implements Serializable {

    //whether the room is currently being used or not
    private boolean occupied;
    //how much time it takes to travel to this room from the client waiting room
    private int travelTime;
    //the time needed after an operation for the room to be available again
    private int cooldownTime;
    //time left in cooldown
    private int cooldownTimeLeft;

    private Client client;

    public ProcedureRoom(int travelTime, int cooldownTime) {
        this.occupied = false;
        this.travelTime = travelTime;
        this.cooldownTime = cooldownTime;
        this.cooldownTimeLeft = 0;
        this.client = null;
    }

    //cooldown time decreases with each tick of time
    public void tick() {
        cooldownTimeLeft--;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        setOccupied(true);
        this.client = client;
    }

    public void removeClient() {
        setOccupied(false);
        this.client = null;
        startCooldown();
    }

    //Starts the process of the cooldown by setting the timeLeft to the cooldown time
    public void startCooldown() {
        cooldownTimeLeft = cooldownTime;
    }

    //Returns true of the room is available for a client
    public boolean isAvailable() {
        return !isOccupied() && cooldownTimeLeft == 0;
    }

    //Returns true of the room is occupied by another client
    public boolean isOccupied() {
        return occupied;
    }

    //Returns the amount of cooldown time left.
    public int getCooldownTimeLeft() {
        return cooldownTimeLeft;
    }

    //Sets the room to be either occupied or unoccupied based on the send in value
    //True = Occupied, False = Unoccupied
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    //Returns the travel time from the waiting room to the procedure room
    public int getTravelTime() {
        return travelTime;
    }

    //Sets the travel time to the waiting room
    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    //Returns the cooldown time that the room must go through every time it completes.
    public int getCooldownTime() {
        return cooldownTime;
    }

    //Sets the cooldown time that the room must go through every time it completes.
    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }


}
