package com.olympus.simulation;

import java.io.Serializable;

public class ManualCleaningStation extends Element implements Serializable, StationCSV.Station {
    Scope currentScope;
    LeakTester_Type currentLeakTester;
    int travelTime;

    private int id;

    public ManualCleaningStation(LeakTester_Type type){
        this.element = ELEMENT_SINK;
        this.currentScope = null;
        this.currentLeakTester = type;
    }

    public Scope getCurrentScope() {
        return currentScope;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return this.id;
    }

    public void setCurrentScope(Scope currentScope) {
        this.currentScope = currentScope;
    }

    public LeakTester_Type getCurrentLeakTester() {
        return currentLeakTester;
    }

    public void setCurrentLeakTester(LeakTester_Type currentLeakTester) {
        this.currentLeakTester = currentLeakTester;
    }

    public boolean validate() {
        if(this.currentLeakTester != null){
            return true;
        }

        return false;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }

    @Override
    public StationCSV getStationCSV() {
        return new StationCSV("Sink", id + "", "Sink " + id);
    }
}
