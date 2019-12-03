package com.olympus.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Doctor_Manager {
    private static ArrayList<Doctor> doctors;
    private static ArrayList<ActorLogCSV> csvList;
    private static String currTime;


    public Doctor_Manager() {
        csvList = new ArrayList<>();
        doctors = new ArrayList<Doctor>();
    }

    public void setIds() {
        for (int i=0; i < doctors.size(); i++) {
            doctors.get(i).setId(i+1);
        }
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
       // int id = 1;
        for(Doctor d : doctors) {
            String procedure = "";
            for(String p : d.getProceduresNames()) {
                procedure += p;
                procedure += "/";
            }
           // d.setId(id);
           // id++;
            procedure = procedure.substring(0,procedure.length() - 1);
            ActorLogCSV logItem = new ActorLogCSV("Doctor", d.getId() + "", "Doctor " + d.getId(), startTime, endTime, procedure, "", State.stateNames[0]);
            csvList.add(logItem);
        }
    }

    public void finalizeCSVList(String endTime) {
        for(Doctor d : doctors) {
            ActorLogCSV logItem = new ActorLogCSV();
            logItem.setName("Doctor " + d.getId());
            csvList.get(getLastActorLogEntry(logItem)).setTimeOut(endTime);
        }
    }

    public static void handleStateSwitch(Doctor n) {
        if(csvList.size() == 0) {
            return;
        }
        String state = State.stateNames[n.getState()];
        ActorLogCSV temp = new ActorLogCSV();
        temp.setName("Doctor " + n.getId());
        if(getLastActorLogEntry(temp) == -1) {
            return;
        }
        String proc = csvList.get(getLastActorLogEntry(temp)).procedure;
        String station = n.getDestinationName();
        ActorLogCSV logItem = new ActorLogCSV("Doctor", n.getId() + "", "Doctor " + n.getId(), currTime, currTime, proc, station, state);
        addActorLogCSV(logItem);
    }

    public ArrayList<ActorLogCSV> getCsvList() {
        return csvList;
    }

    public void runTick(){
        currTime = Time.convertToString(Time.convertToInt(currTime) + 1);
        for(int i = 0; i < this.doctors.size(); i++){
            this.doctors.get(i).tick();
        }
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public int getDoctorNum() {
        return doctors.size();
    }

    public Doctor getDoctorByIndex(int i) {
        return doctors.get(i);
    }

    public void removeDoctorByIndex(int i) {
        doctors.remove(i);
    }

    public Doctor getDoctor(ArrayList<Procedure> procList) {
        for (int i=0; i < doctors.size(); i++) {
            ArrayList<Procedure> docList = doctors.get(i).getProcedures();
            if (doctors.get(i).getState() == State.STATE_WAIT && docList.containsAll(procList)) {
                Doctor doctor = doctors.get(i);
                return doctor;
            }
        }
        return null;
    }

    public ArrayList<Doctor> getAllDoctors() {
        return doctors;
    }

    public ArrayList<Doctor> getFreeDoctors() {
        ArrayList<Doctor> freeDoctors = new ArrayList<Doctor>();
        for (int i=0; i < doctors.size(); i++) {
            if (doctors.get(i).getState() == State.STATE_WAIT) {
                freeDoctors.add(doctors.get(i));
            }
        }
        return freeDoctors;
    }

    public Boolean isEverythingDone() {
        boolean done = true;
        for(Doctor d:doctors){
            done &= d.getState() == State.STATE_WAIT;
        }
        return done;
    }
}
