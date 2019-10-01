package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scope implements Comparable<Scope>, Serializable {
    private Scope_Type type;
    private int state;
    private int timeLeft;
    private boolean uiUpdated;
    private ProcedureRoom room;
    private ManualCleaningStation station;
    private int id;

    private boolean tempGrab;

    public boolean getTempGrab() {
        return tempGrab;
    }

    public void setTempGrab(boolean tempGrab) {
        this.tempGrab = tempGrab;
    }

    public int getCleaningTime() {
        return type.getCleaningTime();
    }

    public Scope(Scope_Type t) {
        this.type = t;
        this.state = State_Scope.STATE_FREE;
        this.timeLeft = 0;
        this.uiUpdated = false;
        this.room = null;
        this.tempGrab = false;
    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        setuiUpdated(false);
    }

    public void claim(ProcedureRoom room, int time) {
        setState(State_Scope.STATE_TRAVEL);
        this.room = room;
        this.timeLeft = time;
        this.tempGrab = false;
    }

    public void tick() {
        this.timeLeft--;
        if (timeLeft < 0) {
            timeLeft = 0;
        }
        if (this.timeLeft == 0) {
            //Scope has finished being cleaned
            if (this.state == State_Scope.STATE_CLEANING) {
                this.station.setCurrentScope(null);
                this.station = null;
                setState(State_Scope.STATE_FREE);
            }
            //Scope has arrived at its destination
            if (this.state == State_Scope.STATE_TRAVEL) {
                setState(State_Scope.STATE_USE);
            }
            if(this.state == State_Scope.STATE_DIRTY) {
                this.station = ManualCleaningStation_Manager.getFreeStation();
                if(this.station != null){
                    this.station.setCurrentScope(this);
                    setTimeLeft(this.getCleaningTime() + this.station.getCurrentLeakTester().getTimeToComplete());
                    setState(State_Scope.STATE_CLEANING);
                }
            }
        }
    }

    public void freeScope() {
        this.timeLeft = this.room.getTravelTime();
        this.room = null;
        setState(State_Scope.STATE_DIRTY);
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public ArrayList<String> getProcedureList() {
        ArrayList<String> names = new ArrayList<String>();
        for (int i = 0; i < type.getProcedureList().size(); i++) {
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

    public boolean getuiUpdated() {
        return uiUpdated;
    }

    public void setuiUpdated(boolean uiUpdated) {
        this.uiUpdated = uiUpdated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProcedureRoom getRoom() {
        return room;
    }


}
