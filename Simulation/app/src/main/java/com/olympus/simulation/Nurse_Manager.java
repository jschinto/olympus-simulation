package com.olympus.simulation;

import java.util.ArrayList;

public class Nurse_Manager {
    private ArrayList<Nurse> nurses;

    private int postProcedureTime;

    public Nurse_Manager() {
        nurses = new ArrayList<Nurse>();
        postProcedureTime = 1;

        nurses.add(new Nurse());
    }

    public int getNurseNum() {
        return nurses.size();
    }

    public Nurse getNurse(int travelTime) {
        for (int i=0; i < nurses.size(); i++) {
            if (nurses.get(i).getState() == State.STATE_WAIT) {
                Nurse nurse = nurses.get(i);
                return nurse;
            }
        }
        return null;
    }

    public void operationComplete(Nurse nurse) {
        nurse.startPostProcedure(postProcedureTime);
    }

    public int getPostProcedureTime() {
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
}
