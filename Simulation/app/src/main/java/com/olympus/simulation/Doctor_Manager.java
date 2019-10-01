package com.olympus.simulation;

import java.util.ArrayList;

public class Doctor_Manager {
    private ArrayList<Doctor> doctors;

    private int postProcedureTime;

    public Doctor_Manager() {
        doctors = new ArrayList<Doctor>();
        postProcedureTime = 1;

        doctors.add(new Doctor());
    }

    public int getDoctorNum() {
        return doctors.size();
    }

    public boolean getDoctor() {
        for (int i=0; i < doctors.size(); i++) {
            if (doctors.get(i).getState() == State.STATE_WAIT) {
                return true;
            }
        }
        return false;
    }
    public void operationComplete(Doctor doctor) {
        doctor.startPostProcedure(postProcedureTime);
    }

    public int getPostProcedureTime() {
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
