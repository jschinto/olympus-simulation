package com.olympus.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scope implements Comparable<Scope> {
    private Scope_Type type;
    private int state;
    private int timeLeft;
    private int cleaningTime;

    public Scope(Scope_Type t, int p, int c) {
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
        return type.getProcedureList().contains(p);
    }

    public int compareTo(Scope o) {
        return this.getState() - o.getState();
    }
}
