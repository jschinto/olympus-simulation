package com.olympus.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Doctor_Manager {
    private ArrayList<Doctor> doctors;

    private static int postProcedureTime;

    public Doctor_Manager() {
        doctors = new ArrayList<Doctor>();
        postProcedureTime = 1;
    }

    public void runTick(){
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
            if (!doctors.get(i).validate()) {
                continue;
            }
            ArrayList<Procedure> docList = doctors.get(i).getProcedures();
            if (doctors.get(i).getState() == State.STATE_WAIT && docList.containsAll(procList)) {
                Doctor doctor = doctors.get(i);
                return doctor;
            }
        }
        return null;
    }

    public void operationComplete(Doctor doctor) {
        doctor.startPostProcedure(postProcedureTime);
    }

    public static int getPostProcedureTime() {
        return postProcedureTime;
    }
    public void setPostProcedureTime(int postProcedureTime) {
        this.postProcedureTime = postProcedureTime;
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
