package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scope implements Comparable<Scope>, Serializable, EquipmentCSV.Equipment {
    private Scope_Type type;
    private int state;
    private int timeLeft;
    private boolean uiUpdated;
    private ProcedureRoom room;
    private int id;
    private EquipmentCSV equipmentCSV;

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

        String typeName = "";
        if(t != null) {
            typeName = t.getName();
        }
        this.equipmentCSV = new EquipmentCSV("Scope", typeName, "");
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

    public int tick() {
        this.timeLeft--;
        if (timeLeft < 0) {
            timeLeft = 0;
        }
        if (this.timeLeft == 0) {
            //Scope has finished being cleaned
            if (this.state == State_Scope.STATE_DIRTY) {
                setState(State_Scope.STATE_FREE);
                return State_Scope.STATE_FREE;
            }
            //Scope has arrived at its destination
            else if (this.state == State_Scope.STATE_TRAVEL) {
                setState(State_Scope.STATE_USE);
                return State_Scope.STATE_USE;
            }

        }
        return this.state;
    }

    //TEMPORARY CODE UNTIL WE WORK ON CLEANING
    public void freeScope(String currTime) {
        this.room = null;
        setState(State_Scope.STATE_DIRTY);
        this.timeLeft = this.type.getCleaningTime();
        Scope_Manager.addDirtyScopeLog(this, currTime);
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
        String typeName = "";
        if(type != null) {
            typeName = type.getName();
        }
        this.equipmentCSV.setModel(typeName);
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
        equipmentCSV.setSerialNum(Integer.toString(id));
    }

    public ProcedureRoom getRoom() {
        return room;
    }


    @Override
    public EquipmentCSV getEquipmentCSV() {
        return equipmentCSV;
    }
}
