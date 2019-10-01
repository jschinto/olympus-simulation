package com.olympus.simulation;

import java.util.ArrayList;

public class Doctor_Manager {
    private ArrayList<Doctor> doctors;

    private static int postProcedureTime;

    public Doctor_Manager() {
        doctors = new ArrayList<Doctor>();
        postProcedureTime = 1;

        doctors.add(new Doctor());
    }

    public void runTick(){
        for(int i = 0; i < this.doctors.size(); i++){
            this.doctors.get(i).tick();
        }
    }

    public int getDoctorNum() {
        return doctors.size();
    }

    public Doctor getDoctor() {
        for (int i=0; i < doctors.size(); i++) {
            if (doctors.get(i).getState() == State.STATE_WAIT) {
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
    public void setDoctorNum(int num) {
        doctors = new ArrayList<Doctor>();
        for (int i=0; i < num; i++) {
            doctors.add(new Doctor());
        }
    }
}
