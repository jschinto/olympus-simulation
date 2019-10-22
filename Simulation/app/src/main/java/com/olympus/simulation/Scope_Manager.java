package com.olympus.simulation;

import java.util.ArrayList;
import java.util.Collections;

public class Scope_Manager {
    private ArrayList<Scope> list;
    private static ArrayList<ScopeLogCSV> csvList;

    public Scope_Manager(){
        list = new ArrayList<Scope>();
        csvList = new ArrayList<>();
        //types = new ArrayList<Scope_Type>();
    }

    public Scope getScopeByIndex(int index) {
        return list.get(index);
    }

    public int getScopeNum() {
        return list.size();
    }

    public void addScope(Scope s) {
        list.add(s);
        sortList();
    }

    public boolean removeScope(Scope s) {
       boolean success = list.remove(s);
       sortList();
       return success;
    }

    public void removeScopeByIndex(int i) {

        list.remove(i);
        sortList();
    }

    private static int getLastScopeLogEntry(ScopeLogCSV logItem) {
        for(int i = csvList.size() - 1; i >= 0; i--) {
            if (csvList.get(i).equals(logItem)) {
                return i;
            }
        }
        return -1;
    }

    public static void addScopeLogCSV(ScopeLogCSV logItem) {
        csvList.get(getLastScopeLogEntry(logItem)).setTimeOut(logItem.timeIn);
        csvList.add(logItem);
    }

    public void initCSVList(String startTime, String endTime) {
        csvList.clear();
        for(Scope s : list) {
            ScopeLogCSV logItem = new ScopeLogCSV(s.getType().getName(), s.getId() + "", "Scope " + s.getId(), startTime, endTime, "", "Cabinet", State_Scope.stateNames[0]);
            csvList.add(logItem);
        }
    }

    public void finalizeCSVList(String endTime) {
        for(Scope s : list) {
            ScopeLogCSV logItem = new ScopeLogCSV();
            logItem.setSerialNum(s.getId() + "");
            csvList.get(getLastScopeLogEntry(logItem)).setTimeOut(endTime);
        }
    }

    public void runTick(String currTime) {
        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).getState() == State_Scope.STATE_TRAVEL || list.get(i).getState() == State_Scope.STATE_DIRTY || list.get(i).getState() == State_Scope.STATE_CLEANING || list.get(i).getState() == State_Scope.STATE_DONE || list.get(i).getState() == State_Scope.STATE_RETURNING) {
                if(list.get(i).getState() != list.get(i).tick()) {
                    Scope s = list.get(i);
                    String state = State_Scope.stateNames[s.getState()];
                    ScopeLogCSV temp = new ScopeLogCSV();
                    temp.setSerialNum(s.getId() + "");
                    String proc = csvList.get(getLastScopeLogEntry(temp)).procedure;
                    String station = "";
                    if(state.equals(State_Scope.stateNames[0])) {
                        proc = "";
                        station = "Cabinet";
                    }
                    if(s.getRoom() != null) {
                        station = "Room " + s.getRoom().getId();
                    }
                    ScopeLogCSV logItem = new ScopeLogCSV(s.getType().getName(), s.getId() + "", "Scope " + s.getId(), currTime, currTime, proc, station, state);
                    addScopeLogCSV(logItem);
                }
            }
        }
        sortList();
    }

    public Scope getAvaliableScope(Procedure p) {

        for(int i = 0; i < list.size(); i++) {
            if(list.get(i).checkProcedure(p) && list.get(i).getState() == State_Scope.STATE_FREE && !list.get(i).getTempGrab()) {
                return list.get(i);
            }
        }

        return null;
    }

    public void sortList(){
        Collections.sort(list);
    }

    public void setIDs() {
        for(int i = 0; i < list.size(); i++) {
            list.get(i).setId(i + 1);
        }
    }

    public ArrayList<Scope> getScopes() {
        return list;
    }

    public ArrayList<ScopeLogCSV> getCsvList() {
        return csvList;
    }

    public static void addDirtyScopeLog(Scope s, String currTime) {
        String state = State_Scope.stateNames[s.getState()];
        ScopeLogCSV temp = new ScopeLogCSV();
        temp.setSerialNum(s.getId() + "");
        String proc = csvList.get(getLastScopeLogEntry(temp)).procedure;
        String station = "Sink";
        ScopeLogCSV logItem = new ScopeLogCSV(s.getType().getName(), s.getId() + "", "Scope " + s.getId(), currTime, currTime, proc, station, state);
        addScopeLogCSV(logItem);
    }

    public Boolean isEverythingDone() {
        boolean done = true;

        for(Scope s:list) {
            done &= s.getState() == State_Scope.STATE_FREE;
        }

        return done;
    }
}
