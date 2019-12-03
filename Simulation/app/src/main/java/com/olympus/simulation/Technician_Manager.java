package com.olympus.simulation;

import java.util.ArrayList;

public class Technician_Manager {
    private static ArrayList<Technician> technicians;
    private static ArrayList<ActorLogCSV> csvList;
    private static String currTime;

    public Technician_Manager(){
        technicians = new ArrayList<>();
        csvList = new ArrayList<>();
        technicians.add(new Technician());
    }

    private static int getLastActorLogEntry(ActorLogCSV logItem) {
        for(int i = csvList.size() - 1; i >= 0; i--) {
            if (csvList.get(i).equals(logItem)) {
                return i;
            }
        }
        return -1;
    }

    public static void addActorLogCSV(ActorLogCSV logItem) {
        csvList.get(getLastActorLogEntry(logItem)).setTimeOut(logItem.timeIn);
        csvList.add(logItem);
    }

    public void initCSVList(String startTime, String endTime) {
        currTime = startTime;
        csvList.clear();
        int id = 1;
        for(Technician t : technicians) {
            t.setId(id);
            id++;
            ActorLogCSV logItem = new ActorLogCSV("Technician", t.getId() + "", "Technician " + t.getId(), startTime, endTime, "", "Waiting Room", State.stateNames[0]);
            csvList.add(logItem);
        }
    }

    public void finalizeCSVList(String endTime) {
        for(Technician t : technicians) {
            ActorLogCSV logItem = new ActorLogCSV();
            logItem.setName("Technician " + t.getId());
            csvList.get(getLastActorLogEntry(logItem)).setTimeOut(endTime);
        }
    }

    public static void handleStateSwitch(Technician n) {
        if(csvList.size() == 0) {
            return;
        }
        String state = State.stateNames[n.getState()];
        ActorLogCSV temp = new ActorLogCSV();
        temp.setName("Technician " + n.getId());
        if(getLastActorLogEntry(temp) == -1) {
            return;
        }
        String proc = csvList.get(getLastActorLogEntry(temp)).procedure;
        String station = n.getDestinationName();
        if(state.equals("Traveling")) {
            station = "Hallway";
        }
        if(station == null || station.equals("")) {
            station = "Waiting Room";
        }
        ActorLogCSV logItem = new ActorLogCSV("Technician", n.getId() + "", "Technician " + n.getId(), currTime, currTime, proc, station, state);
        addActorLogCSV(logItem);
    }

    public ArrayList<ActorLogCSV> getCsvList() {
        return csvList;
    }

    public void runTick(){
        currTime = Time.convertToString(Time.convertToInt(currTime) + 1);
        for(int i = 0; i < technicians.size(); i++){
            technicians.get(i).tick();
        }
    }

    public int getTechnicianNum(){
        return technicians.size();
    }

    public static Technician getTechnician() {
        for(int i = 0; i < technicians.size(); i++){
            if(technicians.get(i).getState() == State.STATE_WAIT){
                Technician tech = technicians.get(i);
                return tech;
            }
        }
        return null;
    }

    public void setTechnicianNum(int num) {
        this.technicians = new ArrayList<Technician>();
        for (int i=0; i < num; i++) {
            this.technicians.add(new Technician());
        }
    }

    public int getFreeTechnicianNum(){
        int count = 0;
        for(int i = 0; i < technicians.size(); i++){
            if(technicians.get(i).getState() == State.STATE_WAIT){
                count++;
            }
        }
        return count;
    }

    public ArrayList<Technician> getTechnicianList() {
        return technicians;
    }

    public Boolean isEverythingDone() {
        return getTechnicianNum() == getFreeTechnicianNum();
    }
}
