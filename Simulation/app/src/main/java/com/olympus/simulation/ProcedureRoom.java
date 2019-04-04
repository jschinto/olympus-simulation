package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProcedureRoom implements Serializable {
    //TODO: IMPLEMENT DIFFERENT TRAVEL TIMES FOR SCOPES AND PATIENTS

    //whether the room is currently being used or not
    private boolean occupied;
    //whether the room is ready to serve the customer
    private boolean ready;
    //how much time it takes to travel to this room from the client waiting room
    private int travelTime;
    //the time needed after an operation for the room to be available again
    private int cooldownTime;
    //time left in cooldown
    private int cooldownTimeLeft;

    private Client client;
    private ArrayList<Scope> scope;

    public ProcedureRoom(int travelTime, int cooldownTime) {
        this.occupied = false;
        this.ready = false;
        this.travelTime = travelTime;
        this.cooldownTime = cooldownTime;
        this.cooldownTimeLeft = 0;
        this.client = null;
        this.scope = new ArrayList<Scope>();
    }

    public void claimScope(ArrayList<Scope> list) {
        this.scope = list;
        for(Scope s : list) {
            s.claim(this.travelTime);
        }
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

    public void removeScope() {
        setReady(false);
        for(Scope s : this.scope){
            s.freeScope();
        }
        this.scope.clear();
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

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
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

    public ArrayList<Scope> getScopeList() {
        return scope;
    }
}
