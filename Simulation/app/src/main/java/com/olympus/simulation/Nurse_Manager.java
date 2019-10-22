package com.olympus.simulation;

import java.util.ArrayList;

public class Nurse_Manager {
    private static ArrayList<Nurse> nurses;
    private static ArrayList<ActorLogCSV> csvList;
    private static String currTime;

    private static int postProcedureTime;

    public Nurse_Manager() {
        nurses = new ArrayList<Nurse>();
        csvList = new ArrayList<>();
        postProcedureTime = 1;

        nurses.add(new Nurse());
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
        for(Nurse n : nurses) {
            ActorLogCSV logItem = new ActorLogCSV("Nurse", nurses.indexOf(n) + "", "Nurse " + nurses.indexOf(n), startTime, endTime, "", "", State.stateNames[0]);
            csvList.add(logItem);
        }
    }

    public void finalizeCSVList(String endTime) {
        for(Nurse n : nurses) {
            ActorLogCSV logItem = new ActorLogCSV();
            logItem.setName("Nurse " + nurses.indexOf(n));
            csvList.get(getLastActorLogEntry(logItem)).setTimeOut(endTime);
        }
    }

    public static void handleStateSwitch(Nurse n) {
        if(csvList.size() == 0) {
            return;
        }
        String state = State.stateNames[n.getState()];
        ActorLogCSV temp = new ActorLogCSV();
        temp.setName("Nurse " + nurses.indexOf(n));
        if(getLastActorLogEntry(temp) == -1) {
            return;
        }
        String proc = csvList.get(getLastActorLogEntry(temp)).procedure;
        String station = "";
        ActorLogCSV logItem = new ActorLogCSV("Nurse", nurses.indexOf(n) + "", "Nurse " + nurses.indexOf(n), currTime, currTime, proc, station, state);
        addActorLogCSV(logItem);
    }

    public ArrayList<ActorLogCSV> getCsvList() {
        return csvList;
    }

    public void runTick(){
        currTime = Time.convertToString(Time.convertToInt(currTime) + 1);
        for(int i = 0; i < this.nurses.size(); i++){
            this.nurses.get(i).tick();
        }
    }

    public int getNurseNum() {
        return nurses.size();
    }

    public Nurse getNurse() {
        for (int i=0; i < nurses.size(); i++) {
            if (nurses.get(i).getState() == State.STATE_WAIT) {
                Nurse nurse = nurses.get(i);
                return nurse;
            }
        }
        return null;
    }

    public ArrayList<Nurse> getNurseList() {
        return nurses;
    }

    public void operationComplete(Nurse nurse) {
        nurse.startPostProcedure(postProcedureTime);
    }

    public static int getPostProcedureTime() {
        return postProcedureTime;
    }

    public void setPostProcedureTime(int postProcedureTime) {
        this.postProcedureTime = postProcedureTime;
    }
    public void setNurseNum(int num) {
        nurses = new ArrayList<Nurse>();
        for (int i=0; i < num; i++) {
            nurses.add(new Nurse());
        }
    }


    public Boolean isEverythingDone() {
        boolean done = true;
        for (Nurse n : nurses) {
            done &= n.getState() == State.STATE_WAIT;
        }
        return done;
    }
    public int getFreeNurses() {
        int count = 0;
        for (int i=0; i < nurses.size(); i++) {
            if (nurses.get(i).getState() == State.STATE_WAIT) {
                count++;
            }
        }
        return count;
    }
}
