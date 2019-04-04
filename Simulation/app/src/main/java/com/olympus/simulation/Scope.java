package com.olympus.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scope implements Comparable<Scope> {
    private String name;
    private ArrayList<Procedure> procedureList;
    private int state;
    private int price;
    private int timeLeft;
    private int cleaningTime;

    public Scope(String n, int p, int c) {
        this.name = n;
        this.procedureList = new ArrayList<Procedure>();
        this.state = State_Scope.STATE_FREE;
        this.price = p;
        this.timeLeft = 0;
        this.cleaningTime = c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean addProcedure(Procedure p) {
        if(procedureList.contains(p)) {
            return false;
        }

        this.procedureList.add(p);
        return true;
    }

    public boolean removeProcedure(Procedure p) {
        return procedureList.remove(p);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
        for(int i = 0; i < this.procedureList.size(); i++){
            names.add(this.procedureList.get(i).getName());
        }

        return names;
    }

    public boolean checkProcedure(Procedure p) {
        return this.procedureList.contains(p);
    }

    public int compareTo(Scope o) {
        return this.getState() - o.getState();
    }
}
