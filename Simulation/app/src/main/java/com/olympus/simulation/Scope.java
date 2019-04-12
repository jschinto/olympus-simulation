package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scope implements Comparable<Scope>, Serializable {
    private Scope_Type type;
    private int state;
    private int timeLeft;
    private int cleaningTime;

    public int getCleaningTime() {
        return cleaningTime;
    }

    public void setCleaningTime(int cleaningTime) {
        this.cleaningTime = cleaningTime;
    }

    public Scope(Scope_Type t, int c) {
        this.type = t;
        this.state = State_Scope.STATE_FREE;
        this.timeLeft = 0;
        this.cleaningTime = c;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void claim(int time) {
        this.state = State_Scope.STATE_TRAVEL;
        this.timeLeft = time;
    }

    public void tick() {
        this.timeLeft--;
        System.out.print("Scope: " + this.timeLeft + "\n");
        if(this.timeLeft == 0) {
            //Scope has arrived at its destination
            if(this.state == State_Scope.STATE_TRAVEL) {
                this.state = State_Scope.STATE_USE;
            }
            //Scope has finished being cleaned
            if(this.state == State_Scope.STATE_DIRTY) {
                this.state = State_Scope.STATE_FREE;
            }
        }
    }

    //TEMPORARY CODE UNTIL WE WORK ON CLEANING
    public void freeScope() {
        System.out.print("Freeing Scope\n");
        this.state = State_Scope.STATE_DIRTY;
        this.timeLeft = this.cleaningTime;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public ArrayList<String> getProcedureList() {
        ArrayList<String> names = new ArrayList<String>();
        for(int i = 0; i < type.getProcedureList().size(); i++){
            names.add(type.getProcedureList().get(i).getName());
        }

        return names;
    }

    public boolean checkProcedure(Procedure p) {
        return type.checkProcedure(p);
    }

    public int compareTo(Scope o) {
        return this.getState() - o.getState();
    }

    public Scope_Type getType() {
        return type;
    }

    public void setType(Scope_Type type) {
        this.type = type;
    }
}
