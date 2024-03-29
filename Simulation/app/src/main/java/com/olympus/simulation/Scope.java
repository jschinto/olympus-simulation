package com.olympus.simulation;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scope extends Element implements Comparable<Scope>, Serializable, EquipmentCSV.Equipment {
    private Scope_Type type;
    public int state;
    private int timeLeft;
    private boolean uiUpdated;
    private ProcedureRoom room;
    private ManualCleaningStation station;
    private Reprocessor repro;
    private int id;
    private EquipmentCSV equipmentCSV;
    private Technician holding;
    private int timeFree;
    private boolean inReprocessor;
    private int reprocessorLoadDelay;

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
        this.element = ELEMENT_SCOPE;
        this.type = t;
        this.state = State_Scope.STATE_FREE;
        this.timeLeft = 0;
        this.uiUpdated = false;
        this.room = null;
        this.repro = null;
        this.tempGrab = false;
        this.holding = null;
        this.inReprocessor = false;
        this.reprocessorLoadDelay = -1;

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
        Scope_Manager.onScopeStateChange(this);
    }

    public void setRepro(Reprocessor repro) {
        this.repro = repro;
        Scope_Manager.onScopeStateChange(this);
    }

    public void claim(ProcedureRoom room, int time) {
        setState(State_Scope.STATE_TRAVEL);
        this.room = room;
        this.timeLeft = time;
        this.tempGrab = false;
    }

    public int tick() {
        if(this.state == State_Scope.STATE_CLEANING && this.holding != null){
            if(this.holding.getState() != State.STATE_OPERATION && this.holding.getState() != State.STATE_CLEANSCOPE){
                return this.getState();
            }
            if(this.timeLeft == this.timeFree){
                setHolding(null, -1);
            }
            else {
                this.holding.setState(State.STATE_CLEANSCOPE);
            }
        }
        this.timeLeft--;
        if (timeLeft < 0) {
            timeLeft = 0;
        }

        if (this.timeLeft == 0) {
            //Scope has finished being cleaned
            if (this.state == State_Scope.STATE_CLEANING && this.station != null) {
                this.station.setCurrentScope(null);
                this.station = null;
                this.timeFree = 0;
                setState(State_Scope.STATE_DONE);
                return this.state;
            }
            //Scope has arrived at its destination
            if (this.state == State_Scope.STATE_TRAVEL) {
                setHolding(null, -1);
                setState(State_Scope.STATE_INROOM);
                return State_Scope.STATE_INROOM;
            }
            if(this.state == State_Scope.STATE_DIRTY) {
                setHolding(null, -1);
                setState(State_Scope.STATE_CLEANING);
                return this.state;
            }
            if(this.state == State_Scope.STATE_DONE){
                if(this.holding == null) {
                    Technician tech = Technician_Manager.getTechnician();
                    if (tech == null) {
                        return this.state;
                    } else {
                        tech.setDestination(this);
                        tech.startTravel(5);
                        this.holding = tech;
                    }
                }
                if(this.holding != null && this.holding.getState() == State.STATE_OPERATION){
                    this.holding.startTravel(5);
                    setTimeLeft(5);
                    setState(State_Scope.STATE_TOREPROCESS);
                    return this.state;
                }
            }
            if(this.state == State_Scope.STATE_DONEREPROCESSING){
                if(this.holding == null) {
                    Technician tech = Technician_Manager.getTechnician();
                    if (tech == null) {
                        return this.state;
                    } else {
                        tech.setDestination(this);
                        tech.startTravel(5);
                        this.holding = tech;
                    }
                }
                if(this.holding != null && this.holding.getState() == State.STATE_OPERATION){
                    this.holding.startTravel(5);
                    setTimeLeft(5);
                    setState(State_Scope.STATE_RETURNING);
                    return this.state;
                }
            }
            if(this.state == State_Scope.STATE_TOREPROCESS){
                setState(State_Scope.STATE_REPROCESSING);
                return this.state;
            }
            if(this.state == State_Scope.STATE_RETURNING){
                setHolding(null, -1);
                setState(State_Scope.STATE_FREE);
                return State_Scope.STATE_FREE;
            }
        }
        return this.state;
    }

    public void startClean(ManualCleaningStation station){
        this.station = station;
        this.station.setCurrentScope(this);
        setTimeLeft(this.getCleaningTime() + this.station.getCurrentLeakTester().getTimeToComplete());
        this.timeFree = getTimeLeft() - station.getCurrentLeakTester().getRequiredAttentionTime();
        Scope_Manager.onScopeStateChange(this);
    }

    public void freeScope(String currTime) {
        this.holding.startTravel(this.room.getTravelTimeTechnician());
        this.timeLeft = this.room.getTravelTimeTechnician();
        this.room = null;
        setState(State_Scope.STATE_DIRTY);
        Scope_Manager.addDirtyScopeLog(this, currTime);
    }

    public void finishReprocessing(){
        this.setInReprocessor(false);
        this.setRepro(null);
        this.setState(State_Scope.STATE_DONEREPROCESSING);
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

    public ManualCleaningStation getStation() {
        return station;
    }

    public Reprocessor getRepro() {
        return repro;
    }

    @Override
    public EquipmentCSV getEquipmentCSV() {
        return equipmentCSV;
    }

    public Technician getHolding() {
        return holding;
    }

    public void setHolding(Technician holding, int travelTime) {
        if(this.holding != null){
            this.holding.startTravel(-1);
        }
        if(holding != null){
            holding.startTravel(travelTime);
        }
        this.holding = holding;
    }

    public boolean canTravel(){
        if(this.holding == null || this.holding.getState() != State.STATE_OPERATION){
            return false;
        }

        return true;
    }

    public boolean isInReprocessor() {
        return inReprocessor;
    }

    public void setInReprocessor(boolean inReprocessor) {
        this.inReprocessor = inReprocessor;
    }

    public int getReprocessorLoadDelay() {
        return reprocessorLoadDelay;
    }

    public void setReprocessorLoadDelay(int reprocessorLoadDelay) {
        this.reprocessorLoadDelay = reprocessorLoadDelay;
    }

    public void decrementReprocessorLoadDelay() {
        this.reprocessorLoadDelay--;
    }

    public void startReprocessorLoadDelay() {
        this.reprocessorLoadDelay = this.type.getReprocessorLoadDelay();
    }

}
